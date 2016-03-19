package garage.api.service;

import java.util.Arrays;
import java.util.Date;

import org.ldauvergne.garage.shared.dto.clients.VehicleDto;
import org.ldauvergne.garage.shared.dto.structure.GarageLevelDto;
import org.ldauvergne.garage.shared.dto.wrappers.GarageLevelWrapperDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import garage.api.utils.Util;
/**
 * 
 * @author Leopold Dauvergne
 *
 */
@RestController
@RequestMapping("api")
public class GarageAPIFacadeService {

	private static final Logger LOG = LoggerFactory.getLogger(GarageAPIFacadeService.class);

	// TODO: Optimize for prod
	private static final String CORE_SERVICE_URL = "http://garage-core/core/";

	@Autowired
	Util aUtil;

	@Autowired
	@LoadBalanced
	private RestTemplate aRestTemplate;

	@RequestMapping("/")
	public String mHello() {
		String lCoreHello = aRestTemplate.getForObject(CORE_SERVICE_URL, String.class);
		return "{\"timestamp\":\"" + new Date() + "\",\"content\":\"Hello from Garage API Service\"}" + "<br>"
				+ lCoreHello;
	}

	/**
	 * Vehicle enters
	 * 
	 * @param registration_id
	 * @param pVehicle
	 * @return
	 */
	@RequestMapping(value = "/clients/gate", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> mEnter(@RequestBody VehicleDto pVehicle) {
		/*
		 * Should be checking Authentication here
		 */
		LOG.info("\n\nIncoming Vehicle");
		/*
		 * Contacting core service
		 */
		HttpHeaders lHeaders = new HttpHeaders();
		lHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		lHeaders.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<VehicleDto> lEntity = new HttpEntity<VehicleDto>(pVehicle, lHeaders);

		try {
			ResponseEntity<Object> lResult = aRestTemplate.exchange(CORE_SERVICE_URL + "/clients/gate", HttpMethod.POST,
					lEntity, Object.class);
			/*
			 * Last filtering possible here
			 */
			return lResult;
		} catch (HttpClientErrorException e) {
			return aUtil.createResponse(e.getResponseBodyAsString(), e.getStatusCode());
		}
	}

	/**
	 * Vehicle exits
	 * 
	 * @param pRegistration_id
	 * @return
	 */
	@RequestMapping(value = "/clients/gate/{pRegistration_id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Object> mExit(@PathVariable("pRegistration_id") String pRegistration_id) {
		/*
		 * Should be checking Authentication here
		 */

		/*
		 * Contacting core service
		 */
		try {
			ResponseEntity<Object> lResult = aRestTemplate.exchange(CORE_SERVICE_URL + "/clients/gate/" + pRegistration_id,
					HttpMethod.DELETE, new HttpEntity<Object>(null, null), Object.class);
			/*
			 * Last filtering possible here
			 */
			return lResult;
		} catch (HttpClientErrorException e) {
			return aUtil.createResponse(e.getResponseBodyAsString(), e.getStatusCode());
		}
	}

	/**
	 * Vehicle exits
	 * 
	 * @param pRegistration_id
	 * @return
	 */
	@RequestMapping(value = "/clients/find/{pRegistration_id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> mFind(@PathVariable("pRegistration_id") String pRegistration_id) {
		/*
		 * Should be checking Authentication here
		 */

		/*
		 * Contacting core service
		 */
		HttpHeaders lHeaders = new HttpHeaders();
		lHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		try {
			ResponseEntity<Object> result = aRestTemplate.exchange(CORE_SERVICE_URL + "/clients/find/" + pRegistration_id,
					HttpMethod.GET, new HttpEntity<Object>(null, lHeaders), Object.class);
			/*
			 * Last filtering possible here
			 */
			return result;
		} catch (HttpClientErrorException e) {
			return aUtil.createResponse(e.getResponseBodyAsString(), e.getStatusCode());
		}
	}

	/**
	 * Returns garage status
	 * 
	 * @param garage
	 * @return
	 */
	@RequestMapping(value = "/management/status", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> mGetStatus() {
		/*
		 * Should be checking Authentication here
		 */
		/*
		 * Contacting core service
		 */
		HttpHeaders lHeaders = new HttpHeaders();
		lHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		lHeaders.setContentType(MediaType.APPLICATION_JSON);
		try {
			ResponseEntity<Object> lResult = aRestTemplate.exchange(CORE_SERVICE_URL + "/management/status",
					HttpMethod.GET, new HttpEntity<Object>(null, lHeaders), Object.class);
			/*
			 * Last filtering possible here
			 */
			return lResult;
		} catch (HttpClientErrorException e) {
			return aUtil.createResponse(e.getResponseBodyAsString(), e.getStatusCode());
		}
	}

	/**
	 * Build garage
	 * 
	 * @param lGarageLevels
	 * @return
	 */
	@RequestMapping(value = "/admin/build/garage", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<String> mBuild(@RequestBody GarageLevelDto[] lGarageLevels) {
		/*
		 * Should be checking Authentication here
		 */
		/*
		 * Contacting core service
		 */
		HttpHeaders lHeaders = new HttpHeaders();
		lHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		lHeaders.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<GarageLevelDto[]> lEntity = new HttpEntity<GarageLevelDto[]>(lGarageLevels, lHeaders);
		try {
			ResponseEntity<String> lResult = aRestTemplate.exchange(CORE_SERVICE_URL + "/admin/build/garage",
					HttpMethod.POST, lEntity, String.class);
			/*
			 * Last filtering possible here
			 */
			return lResult;
		} catch (HttpClientErrorException e) {
			return aUtil.createResponse(e.getResponseBodyAsString(), e.getStatusCode());
		}
	}

	/**
	 * Delete garage
	 * 
	 * @param level_id
	 * @return
	 */
	@RequestMapping(value = "/admin/build/garage", method = RequestMethod.DELETE)
	public ResponseEntity<Object> mDeleteGarage() {
		/*
		 * Should be checking Authentication here
		 */
		ResponseEntity<Object> lResult = aRestTemplate.exchange(CORE_SERVICE_URL + "/admin/build/garage",
				HttpMethod.DELETE, new HttpEntity<Object>(null, null), Object.class);
		/*
		 * Last filtering possible here
		 */
		return lResult;
	}

	/**
	 * Add a level to the garage
	 * 
	 * @param level
	 * @return
	 */
	@RequestMapping(value = "/admin/build/level", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<Object> mAddLevel(@RequestBody GarageLevelDto level) {
		/*
		 * Should be checking Authentication here
		 */
		/*
		 * Contacting core service
		 */
		HttpHeaders lHeaders = new HttpHeaders();
		lHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		lHeaders.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<GarageLevelDto> lEntity = new HttpEntity<GarageLevelDto>(level, lHeaders);

		try {
			ResponseEntity<Object> lResult = aRestTemplate.exchange(CORE_SERVICE_URL + "/admin/build/level",
					HttpMethod.POST, lEntity, Object.class);
			/*
			 * Last filtering possible here
			 */
			return lResult;
		} catch (HttpClientErrorException e) {
			return aUtil.createResponse(e.getResponseBodyAsString(), e.getStatusCode());
		}
	}

	/**
	 * Modify a level in the garage, ie : remotes
	 * 
	 * @param level_id
	 * @param level
	 * @return
	 */
	@RequestMapping(value = "/admin/build/level/{level_id}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<Object> mModifyLevel(@PathVariable("level_id") Long level_id,
			@RequestBody GarageLevelDto level) {
		/*
		 * Should be checking Authentication here
		 */
		/*
		 * Contacting core service
		 */
		HttpHeaders lHeaders = new HttpHeaders();
		lHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		lHeaders.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<GarageLevelDto> lEntity = new HttpEntity<GarageLevelDto>(level, lHeaders);

		try {
			ResponseEntity<Object> lResult = aRestTemplate.exchange(CORE_SERVICE_URL + "/admin/build/level/" + level_id,
					HttpMethod.PUT, lEntity, Object.class);
			/*
			 * Last filtering possible here
			 */
			return lResult;
		} catch (HttpClientErrorException e) {
			return aUtil.createResponse(e.getResponseBodyAsString(), e.getStatusCode());
		}
	}

	/**
	 * Delete a level, ie : town hasn't approved to build so high/low !
	 * 
	 * @param level_id
	 * @return
	 */
	@RequestMapping(value = "/admin/build/level", method = RequestMethod.DELETE)
	public ResponseEntity<Object> mDeleteLevel() {
		/*
		 * Should be checking Authentication here
		 */
		/*
		 * Contacting core service
		 */
		HttpHeaders lHeaders = new HttpHeaders();
		HttpEntity<Object> lEntity = new HttpEntity<Object>(null, lHeaders);

		try {
			ResponseEntity<Object> lResult = aRestTemplate.exchange(CORE_SERVICE_URL + "/admin/build/level",
					HttpMethod.DELETE, lEntity, Object.class);
			/*
			 * Last filtering possible here
			 */
			return lResult;
		} catch (HttpClientErrorException e) {
			return aUtil.createResponse(e.getResponseBodyAsString(), e.getStatusCode());
		}
	}

}
