package garage.api.service;
import java.util.Arrays;
import java.util.Date;

import org.ldauvergne.garage.shared.models.VehicleModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
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

import garage.api.utils.APIUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
/**
 * 
 * @author Leopold Dauvergne
 *
 */
@RestController
@ComponentScan("org.ldauvergne.garage.api.service")
public class GarageAPIFacadeService {

	private static final Logger LOG = LoggerFactory.getLogger(GarageAPIFacadeService.class);

	private static final String CORE_SERVICE_URL = "http://garage-core:8085/core/";

	@Autowired
	APIUtils aAPIUtils;

	@Autowired
	GarageAPIService aGarageAPIService;
	
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
	@ApiOperation(value = "mEnter", nickname = "mEnter")
    @ApiResponses(value = { 
            @ApiResponse(code = 200, message = "Success", response = VehicleModel.class)}) 
	@RequestMapping(value = "/clients/gate", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> mEnter(@RequestBody VehicleModel pVehicle) {

		LOG.info("\n\nIncoming Vehicle");
		try {
			LOG.info("Check Vehicle");
			pVehicle = aGarageAPIService.mCheckVehicle(pVehicle);
			LOG.info("Vehicle Type: " + pVehicle.getVehicleType().toString());
		} catch (Exception e) {
			return aAPIUtils.createResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		HttpHeaders lHeaders = new HttpHeaders();
		lHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		lHeaders.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<VehicleModel> lEntity = new HttpEntity<VehicleModel>(pVehicle, lHeaders);

		try {
			ResponseEntity<Object> lResult = aRestTemplate.exchange(CORE_SERVICE_URL + "/clients/gate", HttpMethod.POST,
					lEntity, Object.class);

			return lResult;
		} catch (HttpClientErrorException e) {
			return aAPIUtils.createResponse(e.getResponseBodyAsString(), e.getStatusCode());
		}
	}

	/**
	 * Vehicle exits
	 * 
	 * @param pRegistrationId
	 * @return
	 */
	@ApiOperation(value = "mExit", nickname = "mExit")
	@RequestMapping(value = "/clients/gate/{registration_id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> mExit(@PathVariable("registration_id") String pRegistrationId) {

		LOG.info("Checking if Vehicle Plate is valid");
		if (!aGarageAPIService.mIsValidPlate(pRegistrationId)) {
			return aAPIUtils.createResponse(GarageAPIService.INVALID_LICENCE_PLATE_MESSAGE,
					HttpStatus.FORBIDDEN);
		}
		
		try {
			ResponseEntity<Object> lResult = aRestTemplate.exchange(CORE_SERVICE_URL + "/clients/gate/" + pRegistrationId,
					HttpMethod.DELETE, new HttpEntity<Object>(null, null), Object.class);

			return lResult;
		} catch (HttpClientErrorException e) {
			return aAPIUtils.createResponse(e.getResponseBodyAsString(), e.getStatusCode());
		}
	}

	/**
	 * Find Vehicle
	 * 
	 * @param pRegistrationId
	 * @return
	 */
	@ApiOperation(value = "mFind", nickname = "mFind")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Success", response = VehicleModel.class)}) 
	@RequestMapping(value = "/clients/find/{registration_id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> mFind(@PathVariable("registration_id") String pRegistrationId) {

		LOG.info("Checking if Vehicle Plate is valid");
		if (!aGarageAPIService.mIsValidPlate(pRegistrationId)) {
			return aAPIUtils.createResponse(GarageAPIService.INVALID_LICENCE_PLATE_MESSAGE,
					HttpStatus.FORBIDDEN);
		}
		
		HttpHeaders lHeaders = new HttpHeaders();
		lHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		try {
			ResponseEntity<Object> result = aRestTemplate.exchange(CORE_SERVICE_URL + "/clients/find/" + pRegistrationId,
					HttpMethod.GET, new HttpEntity<Object>(null, lHeaders), Object.class);

			return result;
		} catch (HttpClientErrorException e) {
			return aAPIUtils.createResponse(e.getResponseBodyAsString(), e.getStatusCode());
		}
	}
}
