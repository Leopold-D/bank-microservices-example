package urlmapper.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import urlmapper.model.URLElement;

@RestController
@EnableNeo4jRepositories
public class UrlMapperCompositeService {

	private static final Logger LOG = LoggerFactory.getLogger(UrlMapperCompositeService.class);

	@Autowired
	Util util;
	
	BidiMap url_objects = new DualHashBidiMap();
	
	//BidiMap query = new DualHashBidiMap();

	/*
	 * Map<Long,URLElement> easy = new HashMap<Long,URLElement>();
	 * Map<Long,URLElement> query = new HashMap<Long,URLElement>();
	 */
	@RequestMapping("/")
	public String mHello() {
		return "{\"timestamp\":\"" + new Date() + "\",\"content\":\"Hello from URL Mapper Service\"}";
	}

	@RequestMapping(value = "/endpoint/**", method = RequestMethod.GET)
	public ResponseEntity<String> mGetMapping(HttpServletRequest request) {

		String restOfTheUrl = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)
				+ request.getQueryString();
		
		if (restOfTheUrl.isEmpty()) {
			return util.createOkResponse(mHello());
		} 
		else {
			mGetTypeAndProcess(request);
			
			return util.createOkResponse(restOfTheUrl);
		}
	}

	private void mGetTypeAndProcess(HttpServletRequest request) {
		Map<String, String[]> queryParams = request.getParameterMap(); 
	   
		Iterator it = queryParams.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        System.out.println(pair.getKey() + " = " + pair.getValue());
	        it.remove();
	    }
		
	}

	@RequestMapping(value = "/init", method = RequestMethod.GET)
	public ResponseEntity<Object> mLoadAll() {

		mLoad("products", "Fashion");
		mLoad("gender=female", "Women");
		mLoad("tag=1234", "Shoes");
		mLoad("tag=5678", "Boat­Shoes");
		mLoad("brand=123", "Adidas");

		return util.createOkResponse("");
	}

	private void mLoad(String pQueryObj, String pEasyObj) {
		url_objects.put(pQueryObj, pEasyObj);
	}

}
