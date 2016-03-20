package garage.core.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ldauvergne.garage.shared.dto.clients.VehicleDto;
import org.ldauvergne.garage.shared.dto.structure.GarageLevelDto;
import org.ldauvergne.garage.shared.dto.wrappers.GarageLevelWrapperDto;
import org.ldauvergne.garage.shared.dto.wrappers.GarageLevelsWrapperDto;
import org.ldauvergne.garage.shared.dto.wrappers.VehicleWrapperDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import garage.core.db.model.LevelModel;
import garage.core.db.model.VehicleModel;
import garage.core.service.model.LotModel;
import garage.core.util.Util;

/**
 * 
 * @author Leopold Dauvergne
 *
 */
// Not be enough to be splitted in 2 classes
@RestController
@RequestMapping("core")
@Configuration
@ComponentScan("org.ldauvergne.garage.core.service")
public class GarageCoreServiceFacade {
	private static final Logger LOG = LoggerFactory.getLogger(GarageCoreServiceFacade.class);

	@Autowired
	Util aUtil;

	@Autowired
	GarageCoreServiceTools aGarageCoreService;

	@RequestMapping("/")
	public String mHello() {
		return "{\"timestamp\":\"" + new Date() + "\",\"content\":\"Hello from Garage Core Service\"}";
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

		LOG.info("Checking if Vehicle Plate is valid");
		if (!aGarageCoreService.mHasValidPlate(pVehicle.getRegistration_id())) {
			return aUtil.createResponse("Invalid licence plate, must be matching ^[-A-Z0-9]+$ regex",
					HttpStatus.FORBIDDEN);
		}

		LOG.info("Checking if garage is built");
		if (aGarageCoreService.getADatabaseConnector().getLevels().isEmpty()) {
			return aUtil.createResponse("Garage not builded yet", HttpStatus.FORBIDDEN);
		}

		LOG.info("Checking if vehicle is not inside garage");
		if (aGarageCoreService.getADatabaseConnector().getVehicle(pVehicle.getRegistration_id()) != null) {
			return aUtil.createResponse("Vehicle already inside", HttpStatus.FORBIDDEN);
		}

		LOG.info("Checking if not full and getting next free lot");
		LotModel lNextLot = aGarageCoreService.mFindLot();
		if (lNextLot == null) {
			return aUtil.createResponse("Garage full or not in service", HttpStatus.FORBIDDEN);
		}

		LOG.info("Parking...");
		if (!aGarageCoreService.getADatabaseConnector()
				.add(new VehicleModel(pVehicle.getRegistration_id(), lNextLot.getLevel_id(), lNextLot.getLot_id(),
						pVehicle.getType(), pVehicle.getNbWheels(), pVehicle.getBrand()))) {
			return aUtil.createResponse("Entering garage failed", HttpStatus.FORBIDDEN);
		}

		LOG.info("Reading where vehicle parked");
		VehicleModel lVehicleModel = aGarageCoreService.getADatabaseConnector().getVehicle(pVehicle.getRegistration_id());

		return aUtil.createResponse(
				new VehicleWrapperDto(lVehicleModel.getLevel_id(), lVehicleModel.getLot_id(), pVehicle), HttpStatus.CREATED);
	}

	/**
	 * Vehicle exits
	 * 
	 * @param pRegistration_id
	 * @return
	 */
	@RequestMapping(value = "/clients/gate/{pRegistration_id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Object> mExit(@PathVariable("pRegistration_id") String pRegistration_id) {
		// Returns the vehicle from the given lot, deleting the lot attribution

		if (!aGarageCoreService.mHasValidPlate(pRegistration_id.toUpperCase())) {
			return aUtil.createResponse("Invalid licence plate, must be matching ^[-A-Z0-9]+$ regex",
					HttpStatus.FORBIDDEN);
		}

		try {
			if (aGarageCoreService.getADatabaseConnector().removeVehicle(pRegistration_id.toUpperCase())) {
				// DELETE (rfc2616)
				return aUtil.createResponse(null, HttpStatus.NO_CONTENT);
			} else {
				return aUtil.createResponse("Vehicle not found", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return aUtil.createResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);
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
		if (!aGarageCoreService.mHasValidPlate(pRegistration_id.toUpperCase())) {
			return aUtil.createResponse("Invalid licence plate, must be matching ^[-A-Z0-9]+$ regex",
					HttpStatus.FORBIDDEN);
		}
		VehicleModel lVehicle = aGarageCoreService.getADatabaseConnector().getVehicle(pRegistration_id.toUpperCase());
		if (lVehicle != null) {
			return aUtil
					.createResponse(
							new VehicleWrapperDto(lVehicle.getLevel_id(),
									lVehicle.getLot_id(), new VehicleDto(lVehicle.getRegistration_id(),
											lVehicle.getType(), lVehicle.getNbWheels(), lVehicle.getBrand())),
							HttpStatus.OK);
		} else {
			return aUtil.createResponse("Vehicle not found", HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Returns garage status
	 * 
	 * @return
	 */
	@RequestMapping(value = "/admin/status", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> mGetStatus() {
		// TODO: Rework, too big
		try {

			GarageLevelsWrapperDto pGarageLevelsWrapperDto = new GarageLevelsWrapperDto();
			pGarageLevelsWrapperDto.setNbLevels(aGarageCoreService.getADatabaseConnector().getLevels().size());

			List<GarageLevelWrapperDto> lListGarageLevelWrapperDto = new ArrayList<GarageLevelWrapperDto>();
			for (LevelModel lLevelModel : aGarageCoreService.getADatabaseConnector().getLevels()) {
				GarageLevelWrapperDto garageLevelWrapperDto = aGarageCoreService.mWrapLevelModel(lLevelModel);

				lListGarageLevelWrapperDto.add(garageLevelWrapperDto);

				pGarageLevelsWrapperDto
						.setNbFreeLots(pGarageLevelsWrapperDto.getNbFreeLots() + garageLevelWrapperDto.getNbFreeLots());
				
				pGarageLevelsWrapperDto.setNbOccupiedLots(
						pGarageLevelsWrapperDto.getNbOccupiedLots() + garageLevelWrapperDto.getNbOccupiedLots());
			}

			pGarageLevelsWrapperDto.setLevels(lListGarageLevelWrapperDto);

			pGarageLevelsWrapperDto.setNbTotalLots(
					pGarageLevelsWrapperDto.getNbFreeLots() + pGarageLevelsWrapperDto.getNbOccupiedLots());
			return aUtil.createResponse(pGarageLevelsWrapperDto, HttpStatus.OK);

		} catch (Exception e) {
			return aUtil.createResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Build garage
	 * 
	 * @param pGarageLevels
	 * @return
	 */
	@RequestMapping(value = "/admin/build/garage", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<String> mBuild(@RequestBody GarageLevelDto[] pGarageLevels) {
		try {
			aGarageCoreService.getADatabaseConnector().clearGarage();
			aGarageCoreService.getADatabaseConnector().initGarage();

			for (GarageLevelDto lGarageLevel : pGarageLevels) {
				LOG.info("Creating new level with " + lGarageLevel.getNbLevelLots() + " lots");
				aGarageCoreService.getADatabaseConnector()
						.add(new LevelModel(aGarageCoreService.getADatabaseConnector().getLevels().size(),
								lGarageLevel.isInUse(), lGarageLevel.getNbLevelLots()));
			}
			return aUtil.createResponse(null, HttpStatus.CREATED);
		} catch (Exception e) {
			return aUtil.createResponse(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
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
		try {
			aGarageCoreService.getADatabaseConnector().clearGarage();
			return aUtil.createResponse(null, HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return aUtil.createResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Add a level to the garage
	 * 
	 * @param pLLevel
	 * @return
	 */
	@RequestMapping(value = "/admin/build/level", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<Object> mAddLevel(@RequestBody GarageLevelDto pLLevel) {

		try {
			aGarageCoreService.getADatabaseConnector().add(new LevelModel(
					aGarageCoreService.getADatabaseConnector().getLevels().size(), true, pLLevel.getNbLevelLots()));
			return aUtil.createResponse(null, HttpStatus.CREATED);
		} catch (Exception e) {
			return aUtil.createResponse(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	/**
	 * Modify a level in the garage, ie : remotes
	 * 
	 * @param pLevelId
	 * @param pLevelWrapper
	 * @return
	 */
	@RequestMapping(value = "/admin/build/level/{pLevelId}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<Object> mModifyLevel(@PathVariable("pLevelId") Integer pLevelId,
			@RequestBody GarageLevelDto pLevelWrapper) {

		List<VehicleModel> lLevelVehicles = aGarageCoreService.getADatabaseConnector().getAllVehicleForLevel(pLevelId);

		
		if (pLevelWrapper.getNbLevelLots() < lLevelVehicles.size()) {
			return aUtil.createResponse("Too many vehicles inside currently the level", HttpStatus.BAD_REQUEST);
		}

		for (VehicleModel lVehicleModel : lLevelVehicles) {
			if (lVehicleModel.getLot_id() > pLevelWrapper.getNbLevelLots()) {
				return aUtil.createResponse("Lots still in use, cannot be reworked", HttpStatus.BAD_REQUEST);
			}
		}

		LevelModel lLevelModel = aGarageCoreService.getADatabaseConnector().getLevel(pLevelId);
		lLevelModel.setInUse(pLevelWrapper.isInUse());
		lLevelModel.setNbLevelLots(pLevelWrapper.getNbLevelLots());

		if (aGarageCoreService.getADatabaseConnector().modifyLevel(lLevelModel)) {
			return aUtil.createResponse(null, HttpStatus.NO_CONTENT);
		} else {
			return aUtil.createResponse("Impossible to update level", HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	/**
	 * Delete a level, ie : town hasn't approved to build so high !
	 * 
	 * @param level_id
	 * @return
	 */
	@RequestMapping(value = "/admin/build/level", method = RequestMethod.DELETE)
	public ResponseEntity<Object> mDeleteLevel() {

		Integer lLastLevelId = aGarageCoreService.getADatabaseConnector().getLevels().size() - 1;

		List<VehicleModel> lLevelVehicles = aGarageCoreService.getADatabaseConnector()
				.getAllVehicleForLevel(lLastLevelId);

		// Level must be empty of vehicles
		if (lLevelVehicles.isEmpty()) {
			aGarageCoreService.getADatabaseConnector().removeLevel(lLastLevelId);
			// DELETE (rfc2616)
			return aUtil.createResponse(null, HttpStatus.NO_CONTENT);
		} else {
			return aUtil.createResponse("Impossible to delete level,vehicles still in there",
					HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
}
