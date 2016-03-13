package urlmapper.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

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

		LOG.info(restOfTheUrl);
		LOG.info(request.getQueryString());

		if (restOfTheUrl.isEmpty()) {
			return util.createOkResponse(mHello());
		} else {
			String out = mGetTypeAndProcess(request);
			if (out.contains("null")) {
				return util.createResponse("URL NOT VALID",HttpStatus.BAD_REQUEST);
			} else {
				return util.createOkResponse(out);
			}

		}
	}

	private String mGetTypeAndProcess(HttpServletRequest request) {
		Map<String, String[]> queryParams = request.getParameterMap();

		// Multimap<Long, String> lURLCode = ArrayListMultimap.create();

		StringBuilder tmp = new StringBuilder();

		if (queryParams.isEmpty()) {
			String[] lURLElems = ((String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE))
					.split("/");

			List<String> strings = new ArrayList<String>(Arrays.asList(lURLElems));

			// First element is "" and second is "endpoint" and shouldnt be
			// inside
			strings = strings.subList(2, strings.size());

			if (strings.size() == 1) {
				tmp.append(url_objects.get(BASE));
				return tmp.toString();
			}
			tmp.append("?");

			for (String lStr : strings) {
				tmp.append(url_objects.inverseBidiMap().get(lStr));
				tmp.append("&");
			}
			tmp.deleteCharAt(tmp.length() - 1);
			return tmp.toString();
		} else {
			Map<String, String[]> lQueryParamMap = request.getParameterMap();
			for (Entry<String, String[]> entry : lQueryParamMap.entrySet()) {
				for (String lParamTag : entry.getValue()) {
					tmp.append("/");
					LOG.info(entry.getKey() + "=" + lParamTag);
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

	@RequestMapping(value = "/size", method = RequestMethod.GET)
	public ResponseEntity<Object> mGetSize() {
		return util.createOkResponse(url_objects.size());
	}

	@RequestMapping(value = "/init200K", method = RequestMethod.GET)
	public ResponseEntity<Object> mLoad200K() {

		for (int i = 0; i < 200000; i++) {
			mLoad("products" + i, "Fashion" + i);
			mLoad("gender=female" + i, "Women" + i);
			mLoad("tag=1234" + i, "Shoes" + i);
			mLoad("tag=5678" + i, "Boat­Shoes" + i);
			mLoad("brand=123" + i, "Adidas" + i);
		}

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
