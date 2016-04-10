package garage.core.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ldauvergne.garage.shared.models.GarageModel;
import org.ldauvergne.garage.shared.models.LevelModel;
import org.ldauvergne.garage.shared.models.LotModel;
import org.ldauvergne.garage.shared.models.VehicleModel;
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

import garage.core.util.Util;


/**
 * 
 * @author Leopold Dauvergne
 *
 */
@RestController
@RequestMapping("core")
@Configuration
@ComponentScan("org.ldauvergne.garage.core.service")
public class GarageCoreFacadeService {
	private static final Logger LOG = LoggerFactory.getLogger(GarageCoreFacadeService.class);

	@Autowired
	Util aUtil;

	@Autowired
	GarageCoreService aGarageCoreService;

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
	public ResponseEntity<Object> mEnter(@RequestBody VehicleModel pVehicle) {
		try {
			LOG.info("Vehicle : " +pVehicle.getRegistration_id());
			LOG.info("Checking if garage is built");
			if (aGarageCoreService.getADatabaseConnector().getLevels().isEmpty()) {
				return aUtil.createResponse("Garage not builded yet", HttpStatus.FORBIDDEN);
			}

			LOG.info("Checking if vehicle is not inside garage");
			if (aGarageCoreService.getADatabaseConnector().getVehicle(pVehicle.getRegistration_id().toUpperCase()) != null) {
				return aUtil.createResponse("Vehicle already inside", HttpStatus.FORBIDDEN);
			}

			LOG.info("Checking if not full and getting next free lot");
			LotModel lNextLot = aGarageCoreService.mGetLot(pVehicle.getRegistration_id().toUpperCase());
			if (lNextLot == null) {
				return aUtil.createResponse("Garage full or not in service", HttpStatus.FORBIDDEN);
			}

			LOG.info("Parking...");
			pVehicle.setLevel_id(lNextLot.getLevel_id());
			pVehicle.setLot_id(lNextLot.getId());
			
			aGarageCoreService.getADatabaseConnector().add(pVehicle);


			LOG.info("Reading where vehicle parked");
			VehicleModel lVehicleModel = aGarageCoreService.getADatabaseConnector()
					.getVehicle(pVehicle.getRegistration_id());

			return aUtil.createResponse(lVehicleModel, HttpStatus.CREATED);
		} catch (Exception e) {
			return aUtil.createResponse("Entering garage failed : "+e.getMessage(), HttpStatus.FORBIDDEN);
		}
	}

	/**
	 * Vehicle exits
	 * 
	 * @param pRegistrationId
	 * @return
	 */
	@RequestMapping(value = "/clients/gate/{pRegistrationId}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Object> mExit(@PathVariable("pRegistrationId") String pRegistrationId) {

		try {
			VehicleModel lVehicle = aGarageCoreService.getADatabaseConnector().removeVehicle(pRegistrationId.toUpperCase());
			//Check if vehicle has well been removed
			if (lVehicle!=null) {
				aGarageCoreService.mFreeLot(aGarageCoreService.getADatabaseConnector().getLevel(lVehicle.getLevel_id()), new LotModel(lVehicle.getLot_id(), lVehicle.getLevel_id(), lVehicle.getRegistration_id().toUpperCase()));
				return aUtil.createResponse(null, HttpStatus.NO_CONTENT);
			} else {
				return aUtil.createResponse("Vehicle not found", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return aUtil.createResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * Find Vehicle
	 * 
	 * @param pRegistrationId
	 * @return
	 */
	@RequestMapping(value = "/clients/find/{pRegistrationId}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> mFind(@PathVariable("pRegistrationId") String pRegistrationId) {

		VehicleModel lVehicle = aGarageCoreService.getADatabaseConnector().getVehicle(pRegistrationId.toUpperCase());
		if (lVehicle != null) {
			return aUtil.createResponse(lVehicle, HttpStatus.OK);
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
		try {

			GarageModel lGarage = new GarageModel(0,0,new ArrayList<LevelModel>());
			for (LevelModel lLevelModel : aGarageCoreService.getADatabaseConnector().getLevels()) {
				lGarage.getLevels().add(lLevelModel);
				lGarage.setNbFreeLots(lGarage.getNbFreeLots() + lLevelModel.getFreeLots().size());
				lGarage.setNbOccupiedLots(lGarage.getNbOccupiedLots() + lLevelModel.getOccupiedLots().size());
			}
			return aUtil.createResponse(lGarage, HttpStatus.OK);
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
	public ResponseEntity<Object> mBuild(@RequestBody LevelModel[] pGarageLevels) {
		
		try {
			aGarageCoreService.getADatabaseConnector().clearGarage();
			aGarageCoreService.getADatabaseConnector().initGarage();
			for (LevelModel lGarageLevel : pGarageLevels) {
				aGarageCoreService.mCreateLevel(lGarageLevel);
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
	public ResponseEntity<Object> mAddLevel(@RequestBody LevelModel pGarageLevel) {
		try {
			aGarageCoreService.mCreateLevel(pGarageLevel);
			return aUtil.createResponse(null, HttpStatus.CREATED);
		} catch (Exception e) {
			return aUtil.createResponse(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	/**
	 * Modify a level in the garage, ie : remotes
	 * 
	 * @param pLevelId
	 * @param pLevel
	 * @return
	 */
	@RequestMapping(value = "/admin/build/level/{pLevelId}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<Object> mModifyLevel(@PathVariable("pLevelId") Integer pLevelId,
			@RequestBody LevelModel pLevel) {
		try {
			List<VehicleModel> lLevelVehicles = aGarageCoreService.getADatabaseConnector()
					.getAllVehicleForLevel(pLevelId);

			if (pLevel.getNbLevelLots() < lLevelVehicles.size()) {
				return aUtil.createResponse("Too many vehicles inside currently the level", HttpStatus.BAD_REQUEST);
			}

			for (VehicleModel lVehicleModel : lLevelVehicles) {
				if (lVehicleModel.getLot_id() > pLevel.getNbLevelLots()) {
					return aUtil.createResponse("Lots still in use, cannot be reworked", HttpStatus.BAD_REQUEST);
				}
			}

			LevelModel lLevelModel = aGarageCoreService.getADatabaseConnector().getLevel(pLevelId);
			lLevelModel.setInUse(pLevel.isInUse());
			lLevelModel.setNbLevelLots(pLevel.getNbLevelLots());

			// Scale up
			for (int i = lLevelModel.getOccupiedLots().size()+lLevelModel.getFreeLots().size(); i < lLevelModel.getNbLevelLots(); i++) {
				lLevelModel.getFreeLots().add(new LotModel(i, lLevelModel.getId(), null));
			}
			
			// Scale down
			for (int i = lLevelModel.getNbLevelLots(); i > lLevelModel.getOccupiedLots().size()+lLevelModel.getFreeLots().size(); i--) {
				lLevelModel.getFreeLots().remove(i);
			}
			
			aGarageCoreService.getADatabaseConnector().modifyLevel(lLevelModel);
			return aUtil.createResponse(null, HttpStatus.NO_CONTENT);
		} catch (Exception e) {
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
