package currencyconverter.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.Produces;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class CurrencyConverterCompositeService {

	private static final Logger LOG = LoggerFactory.getLogger(CurrencyConverterCompositeService.class);

	@Autowired
	Util util;

	@RequestMapping("/")
	public String mHello() {
		return "{\"timestamp\":\"" + new Date() + "\",\"content\":\"Hello from Currency Converter Service\"}";
	}

	@RequestMapping("/rates")
	@Produces("application/json")
	public ResponseEntity<String> mGetRates(@RequestParam(value = "from") String from,
			@RequestParam(value = "to") String to, @RequestParam(value = "value") float value) {

		System.out.println(from + to + value);

		if ((from.equals("USD") || from.equals("EUR") || from.equals("JPY"))
				&& (to.equals("USD") || to.equals("EUR") || to.equals("JPY"))) {
			
			//TODO: Find better response mapping
			return util.createOkResponse("{ \"result\":"+String.valueOf(value * fetchRate(from, to).doubleValue())+"}");
		}

		return null;
	}

	public Number fetchRate(String from, String to) {

		try {
			//TODO: Cleanup
			InputStream in = new URL("http://api.fixer.io/latest?base=" + from + "&symbols=" + to).openStream();

			ObjectMapper mapper = new ObjectMapper();
			String input = IOUtils.toString(in);

			Map<String, Object> map = new HashMap<String, Object>();

			map = mapper.readValue(input, new TypeReference<Map<String, Object>>() {
			});
			
			LOG.info("Response from rates service: "+map);


			LOG.info(String.valueOf(((LinkedHashMap) map.get("rates")).get(to)));
			
			return (Number) ((LinkedHashMap) map.get("rates")).get(to);


		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
		return null;
	}

}
