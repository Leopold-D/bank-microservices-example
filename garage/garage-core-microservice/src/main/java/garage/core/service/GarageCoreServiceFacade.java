package garage.core.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ldauvergne.garage.shared.dto.clients.VehicleDto;
import org.ldauvergne.garage.shared.dto.structure.GarageLevelDto;
import org.ldauvergne.garage.shared.dto.structure.GarageLotDto;
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
import garage.core.db.service.GarageCoreService;
import garage.core.service.classes.LotModel;
import garage.core.util.Util;

@RestController
@RequestMapping("core")
@Configuration
@ComponentScan("org.ldauvergne.garage.core.service")
public class GarageCoreServiceFacade {

	private static final Logger LOG = LoggerFactory.getLogger(GarageCoreServiceFacade.class);

	@Autowired
	Util util;

	@Autowired
	GarageCoreService garageCoreService;

	@RequestMapping("/")
	public String mHello() {
		return "{\"timestamp\":\"" + new Date() + "\",\"content\":\"Hello from Garage Core Service\"}";
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

		LOG.info("Checking if Vehicle Plate is valid");
		if (!garageCoreService.mHasValidPlate(vehicle.getRegistration_id())) {
			return util.createResponse("Invalid licence plate, must be matching ^[-A-Z0-9]+$ regex",
					HttpStatus.FORBIDDEN);
		}

		LOG.info("Checking if garage is builded");
		if (garageCoreService.getDatabaseConnector().getLevels().isEmpty()) {
			return util.createResponse("Garage not builded yet", HttpStatus.FORBIDDEN);
		}

		LOG.info("Checking if vehicle is not inside garage");
		if (garageCoreService.getDatabaseConnector().findVehicle(vehicle.getRegistration_id()) != null) {
			return util.createResponse("Vehicle already inside", HttpStatus.FORBIDDEN);
		}

		LOG.info("Checking if not full and getting next free lot");
		LotModel nextLot = garageCoreService.mFindLot();
		if (nextLot == null) {
			return util.createResponse("Garage full", HttpStatus.FORBIDDEN);
		}

		LOG.info("Parking...");
		if (!garageCoreService.getDatabaseConnector()
				.add(new VehicleModel(vehicle.getRegistration_id(), nextLot.getLevel_id(), nextLot.getLot_id(),
						vehicle.getType(), vehicle.getNbWheels(), vehicle.getBrand()))) {
			return util.createResponse("Entering garage failed", HttpStatus.FORBIDDEN);
		}

		LOG.info("Reading where vehicle parked");
		VehicleModel lVehicleModel = garageCoreService.getDatabaseConnector().findVehicle(vehicle.getRegistration_id());

		return util.createResponse(
				new VehicleWrapperDto(lVehicleModel.getLevel_id(), lVehicleModel.getLot_id(), vehicle), HttpStatus.OK);
	}

	/**
	 * Vehicle exits
	 * 
	 * @param registration_id
	 * @return
	 */
	@RequestMapping(value = "/clients/gate/{registration_id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Object> mExit(@PathVariable("registration_id") String registration_id) {
		// Returns the vehicle from the given lot, deleting the lot attribution

		if (!garageCoreService.mHasValidPlate(registration_id.toUpperCase())) {
			return util.createResponse("Invalid licence plate, must be matching ^[-A-Z0-9]+$ regex",
					HttpStatus.FORBIDDEN);
		}

		try {
			if (garageCoreService.getDatabaseConnector().removeVehicle(registration_id.toUpperCase())) {
				return util.createResponse(null, HttpStatus.OK);
			} else {
				return util.createResponse("Vehicle not found", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return util.createResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);
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

		if (!garageCoreService.mHasValidPlate(registration_id.toUpperCase())) {
			return util.createResponse("Invalid licence plate, must be matching ^[-A-Z0-9]+$ regex",
					HttpStatus.FORBIDDEN);
		}
		VehicleModel lVehicle = garageCoreService.getDatabaseConnector().findVehicle(registration_id.toUpperCase());
		if (lVehicle != null) {
			return util.createResponse(new GarageLotDto(lVehicle.getLevel_id(), lVehicle.getLot_id()), HttpStatus.OK);
		} else {
			return util.createResponse("Vehicle not found", HttpStatus.NOT_FOUND);
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

		try {

			GarageLevelsWrapperDto garageLevelsWrapperDto = new GarageLevelsWrapperDto();

			garageLevelsWrapperDto.setNbLevels(garageCoreService.getDatabaseConnector().getLevels().size());

			for (LevelModel levelModel : garageCoreService.getDatabaseConnector().getLevels()) {
				GarageLevelWrapperDto garageLevelWrapperDto = new GarageLevelWrapperDto();

				garageLevelWrapperDto.setId(levelModel.getId());
				garageLevelWrapperDto.setLevel(new GarageLevelDto(levelModel.getNbLevelLots()));
				List<VehicleModel> lVehicleModels = garageCoreService.getDatabaseConnector()
						.findAllVehicleForLevel(levelModel.getId());

				garageLevelWrapperDto.setNbOccupiedLots(lVehicleModels.size());
				garageLevelWrapperDto.setNbFreeLots(levelModel.getNbLevelLots() - lVehicleModels.size());

				List<VehicleDto> lVehiclesDto = new ArrayList<VehicleDto>();
				for (VehicleModel lVehicleModel : lVehicleModels) {
					lVehiclesDto.add(new VehicleDto(lVehicleModel.getRegistration_id(), lVehicleModel.getType(),
							lVehicleModel.getNbWheels(), lVehicleModel.getBrand()));
				}
				garageLevelWrapperDto.setVehicles(lVehiclesDto);

				// Bug?
				garageLevelsWrapperDto.getLevels().add(garageLevelWrapperDto);

				garageLevelsWrapperDto
						.setNbFreeLots(garageLevelsWrapperDto.getNbFreeLots() + garageLevelWrapperDto.getNbFreeLots());
				garageLevelsWrapperDto.setNbOccupiedLots(
						garageLevelsWrapperDto.getNbOccupiedLots() + garageLevelWrapperDto.getNbOccupiedLots());

			}
			garageLevelsWrapperDto.setNbTotalLots(
					garageLevelsWrapperDto.getNbFreeLots() + garageLevelsWrapperDto.getNbOccupiedLots());
			return util.createResponse(garageLevelsWrapperDto, HttpStatus.OK);

		} catch (Exception e) {
			return util.createResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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
		try {
			garageCoreService.getDatabaseConnector().clearGarage();
			garageCoreService.getDatabaseConnector().initGarage();

			for (GarageLevelDto lGarageLevel : garage_levels) {
				LOG.info("Creating new level with " + lGarageLevel.getNbLevelLots() + " lots");
				garageCoreService.getDatabaseConnector()
						.add(new LevelModel(garageCoreService.getDatabaseConnector().getLevels().size(), true,
								lGarageLevel.getNbLevelLots()));
			}

			return util.createOkResponse(null);
		} catch (Exception e) {
			return util.createResponse(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
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
			garageCoreService.getDatabaseConnector().clearGarage();
			return util.createOkResponse(null);
		} catch (Exception e) {
			return util.createResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Add a level to the garage
	 * 
	 * @param level
	 * @return
	 */
	@RequestMapping(value = "/admin/build/level", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<Object> mAddLevel(@RequestBody GarageLevelWrapperDto level) {
		return util.createResponse(HttpStatus.NOT_IMPLEMENTED.toString(), HttpStatus.NOT_IMPLEMENTED);
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
			@RequestBody GarageLevelWrapperDto level) {
		return util.createResponse(HttpStatus.NOT_IMPLEMENTED.toString(), HttpStatus.NOT_IMPLEMENTED);
	}

	/**
	 * Delete a level, ie : town hasn't approved to build so high/low !
	 * 
	 * @param level_id
	 * @return
	 */
	@RequestMapping(value = "/admin/build/level/{level_id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> mDeleteLevel(@PathVariable("level_id") Long level_id) {
		return util.createResponse(HttpStatus.NOT_IMPLEMENTED.toString(), HttpStatus.NOT_IMPLEMENTED);
	}
}
