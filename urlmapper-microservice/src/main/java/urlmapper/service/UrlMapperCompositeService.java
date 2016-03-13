package urlmapper.service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import urlmapper.model.URLElement;

@RestController
@EnableNeo4jRepositories
public class UrlMapperCompositeService {

	private static final Logger LOG = LoggerFactory.getLogger(UrlMapperCompositeService.class);

	@Autowired
	Util util;

	static String BASE = "products";

	BidiMap url_objects = new DualHashBidiMap();

	// BidiMap query = new DualHashBidiMap();

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

		String restOfTheUrl = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);

		System.out.println(restOfTheUrl);
		System.out.println(request.getQueryString());

		if (restOfTheUrl.isEmpty()) {
			return util.createOkResponse(mHello());
		} else {

			return util.createOkResponse(mGetTypeAndProcess(request));
		}
	}

	private String mGetTypeAndProcess(HttpServletRequest request) {
		Map<String, String[]> queryParams = request.getParameterMap();

		// Multimap<Long, String> lURLCode = ArrayListMultimap.create();

		StringBuilder tmp = new StringBuilder();

		if (queryParams.isEmpty()) {
			String[] lURLElems = ((String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE))
					.split("/");

			List<String> strings = Arrays.asList(lURLElems);

			// First element is "endpoint" and shouldnt be inside
			strings.remove(0);

			if (strings.size() == 1) {
				tmp.append(url_objects.get(BASE));
				return tmp.toString();
			} else {
				// Ignoring "products" as it's not counted when other url params exist.
				strings.remove(0);
			}
			tmp.append("?");
			
			for (String lStr : strings) {
				tmp.append(url_objects.inverseBidiMap().get(lStr));
				tmp.append("&");
			}
			tmp.deleteCharAt(tmp.length() - 1);
			return tmp.toString();
		} 
		else {
			Map<String, String[]> lQueryParamMap = request.getParameterMap();
			for (Entry<String, String[]> entry : lQueryParamMap.entrySet()) {
				for (String lParamTag : entry.getValue()) {
					tmp.append("/");
					System.out.println(entry.getKey() + "=" + lParamTag);
					tmp.append(url_objects.get(entry.getKey() + "=" + lParamTag));
				}
			}
			return tmp.toString();
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
