package urlmapper.service;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

@RestController
public class UrlMapperCompositeService {

	private static final Logger LOG = LoggerFactory.getLogger(UrlMapperCompositeService.class);

	@Autowired
	Util util;

//	Map<RateCouple, RateValue> rates = new HashMap<RateCouple, RateValue>();
	
	@RequestMapping("/")
	public String mHello() {
		return "{\"timestamp\":\"" + new Date() + "\",\"content\":\"Hello from URL Mapper Service\"}";
	}
	
	@GET
	@RequestMapping("/**")
	@Produces("application/json")
	public ResponseEntity<String> mGetMapping(HttpServletRequest request) {
		
	    String restOfTheUrl = (String) request.getAttribute(
	            HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)+request.getQueryString();
	    if(restOfTheUrl.isEmpty()){
	    	return util.createOkResponse(mHello());
	    }
	    else{
	    	return  util.createOkResponse(restOfTheUrl);
	    }
	}
	    

}
