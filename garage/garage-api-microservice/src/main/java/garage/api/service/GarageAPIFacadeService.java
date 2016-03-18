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

@RestController
@RequestMapping("api")
public class GarageAPIFacadeService {

	private static final Logger LOG = LoggerFactory.getLogger(GarageAPIFacadeService.class);

	// TODO: Optimisz for prod
	private static final String CORE_SERVICE_URL = "http://garage-core/core/";

	@Autowired
	Util util;

	@Autowired
	@LoadBalanced
	private RestTemplate restTemplate;

	@RequestMapping("/")
	public String mHello() {
		String lCoreHello = restTemplate.getForObject(CORE_SERVICE_URL, String.class);
		return "{\"timestamp\":\"" + new Date() + "\",\"content\":\"Hello from Garage API Service\"}" + "<br>"
				+ lCoreHello;
	}

	/**
	 * Vehicle enters
	 * 
	 * @param registration_id
	 * @param vehicle
	 * @return
	 */
	@RequestMapping(value = "/clients/gate", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> mEnter(@RequestBody VehicleDto vehicle) {
		/*
		 * Should be checking Authentication here
		 */
		LOG.info("\n\nIncoming Vehicle");
		/*
		 * Contacting core service
		 */
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<VehicleDto> entity = new HttpEntity<VehicleDto>(vehicle, headers);

		try {
			ResponseEntity<Object> result = restTemplate.exchange(CORE_SERVICE_URL + "/clients/gate", HttpMethod.POST,
					entity, Object.class);
			/*
			 * Last filtering possible here
			 */
			return result;
		} catch (HttpClientErrorException e) {
			return util.createResponse(e.getResponseBodyAsString(), e.getStatusCode());
		}
	}

	/**
	 * Vehicle exits
	 * 
	 * @param registration_id
	 * @return
	 */
	@RequestMapping(value = "/clients/gate/{registration_id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Object> mExit(@PathVariable("registration_id") String registration_id) {
		/*
		 * Should be checking Authentication here
		 */

		/*
		 * Contacting core service
		 */
		try {
			ResponseEntity<Object> result = restTemplate.exchange(CORE_SERVICE_URL + "/clients/gate/" + registration_id,
					HttpMethod.DELETE, new HttpEntity<Object>(null, null), Object.class);
			/*
			 * Last filtering possible here
			 */
			return result;
		} catch (HttpClientErrorException e) {
			return util.createResponse(e.getResponseBodyAsString(), e.getStatusCode());
		}
	}

	/**
	 * Vehicle exits
	 * 
	 * @param registration_id
	 * @return
	 */
	@RequestMapping(value = "/clients/find/{registration_id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> mFind(@PathVariable("registration_id") String registration_id) {
		/*
		 * Should be checking Authentication here
		 */

		/*
		 * Contacting core service
		 */
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		try {
			ResponseEntity<Object> result = restTemplate.exchange(CORE_SERVICE_URL + "/clients/find/" + registration_id,
					HttpMethod.GET, new HttpEntity<Object>(null, headers), Object.class);
			/*
			 * Last filtering possible here
			 */
			return result;
		} catch (HttpClientErrorException e) {
			return util.createResponse(e.getResponseBodyAsString(), e.getStatusCode());
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
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		try {
			ResponseEntity<Object> result = restTemplate.exchange(CORE_SERVICE_URL + "/management/status",
					HttpMethod.GET, new HttpEntity<Object>(null, headers), Object.class);
			/*
			 * Last filtering possible here
			 */
			return result;
		} catch (HttpClientErrorException e) {
			return util.createResponse(e.getResponseBodyAsString(), e.getStatusCode());
		}
	}

	/**
	 * Build garage
	 * 
	 * @param garage_levels
	 * @return
	 */
	@RequestMapping(value = "/admin/build/garage", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<String> mBuild(@RequestBody GarageLevelDto[] garage_levels) {
		/*
		 * Should be checking Authentication here
		 */
		/*
		 * Contacting core service
		 */
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<GarageLevelDto[]> entity = new HttpEntity<GarageLevelDto[]>(garage_levels, headers);
		try {
			ResponseEntity<String> result = restTemplate.exchange(CORE_SERVICE_URL + "/admin/build/garage",
					HttpMethod.POST, entity, String.class);
			/*
			 * Last filtering possible here
			 */
			return result;
		} catch (HttpClientErrorException e) {
			return util.createResponse(e.getResponseBodyAsString(), e.getStatusCode());
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
		ResponseEntity<Object> result = restTemplate.exchange(CORE_SERVICE_URL + "/admin/build/garage",
				HttpMethod.DELETE, new HttpEntity<Object>(null, null), Object.class);
		/*
		 * Last filtering possible here
		 */
		return result;
	}

	/**
	 * Add a level to the garage
	 * 
	 * @param level
	 * @return
	 */
	@RequestMapping(value = "/admin/build/level", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<Object> mAddLevel(@RequestBody GarageLevelWrapperDto level) {
		/*
		 * Should be checking Authentication here
		 */
		return util.createResponse(HttpStatus.NOT_IMPLEMENTED.toString(), HttpStatus.NOT_IMPLEMENTED);
	}

	/**
	 * Modify a level in the garage, ie : remotes
	 * 
	 * @param level_id
	 * @param level
	 * @return
	 */
	@RequestMapping(value = "/admin/build/level", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<Object> mModifyLevel(@RequestBody GarageLevelWrapperDto level) {
		/*
		 * Should be checking Authentication here
		 */
		return util.createResponse(HttpStatus.NOT_IMPLEMENTED.toString(), HttpStatus.NOT_IMPLEMENTED);
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
		return util.createResponse(HttpStatus.NOT_IMPLEMENTED.toString(), HttpStatus.NOT_IMPLEMENTED);
	}

}
