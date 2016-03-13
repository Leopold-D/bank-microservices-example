package currencyconverter.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import currencyconverter.data.ResultData;
import currencyconverter.model.RateCouple;
import currencyconverter.model.RateValue;

@RestController
public class CurrencyConverterCompositeService {

	private static final Logger LOG = LoggerFactory.getLogger(CurrencyConverterCompositeService.class);

	@Autowired
	Util util;

	Map<RateCouple, RateValue> rates = new HashMap<RateCouple, RateValue>();
	
	@RequestMapping("/")
	public String mHello() {
		return "{\"timestamp\":\"" + new Date() + "\",\"content\":\"Hello from Currency Converter Service\"}";
	}

	
	
	@GET
	@RequestMapping("/rates")
	@Produces("application/json")
	public ResponseEntity<ResultData> mGetRates(@RequestParam(value = "from") String from,
			@RequestParam(value = "to") String to, @RequestParam(value = "value") float value) {

		if ((from.equals("USD") || from.equals("EUR") || from.equals("JPY"))
				&& (to.equals("USD") || to.equals("EUR") || to.equals("JPY"))) {
			
			RateCouple rateCouple = new RateCouple(from, to);
			
			if(!rates.containsKey(rateCouple) || !checkRateValueValidity(rates.get(rateCouple))){
				rates.put(rateCouple, fetchRate(rateCouple));
			}
			return util.createOkResponse(new ResultData(value*rates.get(rateCouple).getValue(),rateCouple,rates.get(rateCouple)));
		}
		return util.createResponse(null, HttpStatus.BAD_REQUEST);
	}
	
	
	public Boolean checkRateValueValidity(RateValue ratevalue){
		
		long diff = (new Date()).getTime() - ratevalue.getDate().getTime();
		
		return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) < 1L;
		
	}

	public RateValue fetchRate(RateCouple rateCouple) {

		try {
			//TODO: Cleanup
			InputStream in = new URL("http://api.fixer.io/latest?base=" + rateCouple.getFrom() + "&symbols=" + rateCouple.getTo()).openStream();

			ObjectMapper mapper = new ObjectMapper();
			String input = IOUtils.toString(in);

			Map<String, Object> map = new HashMap<String, Object>();

			map = mapper.readValue(input, new TypeReference<Map<String, Object>>() {
			});
			
			LOG.info("Response from rates service: "+map);


			LOG.info(String.valueOf(((LinkedHashMap<?, ?>) map.get("rates")).get(rateCouple.getTo())));
			
			return new RateValue(new Date(),((Number) ((LinkedHashMap<?, ?>) map.get("rates")).get(rateCouple.getTo())).doubleValue());


		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
		return null;
	}

}
