package garage.api.service;

import java.util.Arrays;
import java.util.Date;

import org.ldauvergne.garage.shared.models.GarageModel;
import org.ldauvergne.garage.shared.models.LevelModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import garage.api.utils.APIUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@EnableResourceServer
@ComponentScan("org.ldauvergne.garage.api.service")
public class GarageAPIAdminFacadeService {
	
	private static final String CORE_SERVICE_URL = "http://garage-core:8085/core/";

	@Autowired
	APIUtils aAPIUtils;

	private static RestTemplate aRestTemplate;
	
	static {
		aRestTemplate = new RestTemplate();
    }
	
	@ApiOperation(value = "mHello", nickname = "mHello")
    @ApiResponses(value = { 
            @ApiResponse(code = 200, message = "Success", response = String.class)}) 
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String mHello() {
		String lCoreHello = aRestTemplate.getForObject(CORE_SERVICE_URL, String.class);
		return "{\"timestamp\":\"" + new Date() + "\",\"content\":\"Hello from Garage API Admin Service\"}" + "<br>"
				+ lCoreHello;
	}
	
	/**
	 * Returns garage status
	 * 
	 * @param garage
	 * @return
	 */
	@ApiOperation(value = "mGetStatus", nickname = "mGetStatus")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Success", response = GarageModel.class)}) 
	@RequestMapping(value = "/status", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> mGetStatus() {

		HttpHeaders lHeaders = new HttpHeaders();
		lHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		lHeaders.setContentType(MediaType.APPLICATION_JSON);
		try {
			ResponseEntity<Object> lResult = aRestTemplate.exchange(CORE_SERVICE_URL + "/admin/status",
					HttpMethod.GET, new HttpEntity<Object>(null, lHeaders), Object.class);

			return lResult;
		} catch (HttpClientErrorException e) {
			return aAPIUtils.createResponse(e.getResponseBodyAsString(), e.getStatusCode());
		}
	}

	/**
	 * Build garage
	 * 
	 * @param lGarageLevels
	 * @return
	 */
	@ApiOperation(value = "mBuild", nickname = "mBuild")
	@RequestMapping(value = "/build/garage", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<Object> mBuild(@RequestBody LevelModel[] lGarageLevels) {
		
		HttpHeaders lHeaders = new HttpHeaders();
		lHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		lHeaders.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<LevelModel[]> lEntity = new HttpEntity<LevelModel[]>(lGarageLevels, lHeaders);
		
		try {
			ResponseEntity<Object> lResult = aRestTemplate.exchange(CORE_SERVICE_URL + "/admin/build/garage",
					HttpMethod.POST, lEntity, Object.class);
			return lResult;
		} catch (HttpClientErrorException e) {
			return aAPIUtils.createResponse(e.getResponseBodyAsString(), e.getStatusCode());
		}
	}

	/**
	 * Delete garage
	 * 
	 * @param level_id
	 * @return
	 */
	@ApiOperation(value = "mDeleteGarage", nickname = "mDeleteGarage")
	@RequestMapping(value = "/build/garage", method = RequestMethod.DELETE)
	public ResponseEntity<Object> mDeleteGarage() {

		ResponseEntity<Object> lResult = aRestTemplate.exchange(CORE_SERVICE_URL + "/admin/build/garage",
				HttpMethod.DELETE, new HttpEntity<Object>(null, null), Object.class);

		return lResult;
	}

	/**
	 * Add a level to the garage
	 * 
	 * @param level
	 * @return
	 */
	@ApiOperation(value = "mAddLevel", nickname = "mAddLevel")
	@RequestMapping(value = "/build/level", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<Object> mAddLevel(@RequestBody LevelModel level) {

		HttpHeaders lHeaders = new HttpHeaders();
		lHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		lHeaders.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<LevelModel> lEntity = new HttpEntity<LevelModel>(level, lHeaders);

		try {
			ResponseEntity<Object> lResult = aRestTemplate.exchange(CORE_SERVICE_URL + "/admin/build/level",
					HttpMethod.POST, lEntity, Object.class);

			return lResult;
		} catch (HttpClientErrorException e) {
			return aAPIUtils.createResponse(e.getResponseBodyAsString(), e.getStatusCode());
		}
	}

	/**
	 * Modify a level in the garage, ie : remotes
	 * 
	 * @param level_id
	 * @param level
	 * @return
	 */
	@ApiOperation(value = "mModifyLevel", nickname = "mModifyLevel")
	@ApiResponses(value = { 
			@ApiResponse(code = 204, message = "No Content")}) 
	@RequestMapping(value = "/build/level/{level_id}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<Object> mModifyLevel(@PathVariable("level_id") Long level_id,
			@RequestBody LevelModel level) {

		HttpHeaders lHeaders = new HttpHeaders();
		lHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		lHeaders.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<LevelModel> lEntity = new HttpEntity<LevelModel>(level, lHeaders);

		try {
			ResponseEntity<Object> lResult = aRestTemplate.exchange(CORE_SERVICE_URL + "/admin/build/level/" + level_id,
					HttpMethod.PUT, lEntity, Object.class);

			return lResult;
		} catch (HttpClientErrorException e) {
			return aAPIUtils.createResponse(e.getResponseBodyAsString(), e.getStatusCode());
		}
	}

	/**
	 * Delete a level, ie : town hasn't approved to build so high/low !
	 * 
	 * @param level_id
	 * @return
	 */
	@ApiOperation(value = "mDeleteLevel", nickname = "mDeleteLevel")
	@RequestMapping(value = "/build/level", method = RequestMethod.DELETE)
	public ResponseEntity<Object> mDeleteLevel() {

		HttpHeaders lHeaders = new HttpHeaders();
		HttpEntity<Object> lEntity = new HttpEntity<Object>(null, lHeaders);

		try {
			ResponseEntity<Object> lResult = aRestTemplate.exchange(CORE_SERVICE_URL + "/admin/build/level",
					HttpMethod.DELETE, lEntity, Object.class);

			return lResult;
		} catch (HttpClientErrorException e) {
			return aAPIUtils.createResponse(e.getResponseBodyAsString(), e.getStatusCode());
		}
	}
}
