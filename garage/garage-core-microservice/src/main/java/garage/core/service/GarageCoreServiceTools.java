package garage.core.service;

import java.util.ArrayList;
import java.util.List;

import org.ldauvergne.garage.shared.dto.clients.VehicleDto;
import org.ldauvergne.garage.shared.dto.structure.GarageLevelDto;
import org.ldauvergne.garage.shared.dto.wrappers.GarageLevelWrapperDto;
import org.ldauvergne.garage.shared.dto.wrappers.VehicleWrapperDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import garage.core.db.model.LevelModel;
import garage.core.db.model.VehicleModel;
import garage.core.db.service.DatabaseConnector;
import garage.core.service.model.LotModel;
import lombok.Getter;
/**
 * 
 * @author Leopold Dauvergne
 *
 */
@Component
public class GarageCoreServiceTools {

	/**
	 * Wrapping the hard computation
	 */
	@Autowired
	@Getter
	private DatabaseConnector aDatabaseConnector;

	private static final Logger LOG = LoggerFactory.getLogger(GarageCoreServiceTools.class);

	// TODO: Expensive, should be reworked within model maybe, simulates well
	// the time a user needs to find a free lot
	public LotModel mFindLot() {
		// Browse through levels
		LOG.info("Browsing through levels");
		for (LevelModel lLevel : aDatabaseConnector.getLevels()) {
			// Check if level is in use
			LOG.info("Checking if level is in use");
			if (lLevel.isInUse()) {
				List<VehicleModel> lLevelVehicles = aDatabaseConnector.getAllVehicleForLevel(lLevel.getId());
				// Check if level has free lots
				LOG.info("Checking if level has free lots");
				if (lLevel.getNbLevelLots() > lLevelVehicles.size()) {

					// Get occupied lots
					LOG.info("Getting occupied lots");
					List<Integer> lOccupiedLots = new ArrayList<Integer>();
					for (VehicleModel lVehicle : lLevelVehicles) {
						lOccupiedLots.add(lVehicle.getLot_id());
					}
					// Browse through lots to get next free
					LOG.info("Browse through lots to get next free");
					for (int i = 0; i < lLevel.getNbLevelLots(); i++) {
						if (!lOccupiedLots.contains(i)) {
							return (new LotModel(lLevel.getId(), i));
						}
					}
				}
			}
		}
		return null;
	}

	public boolean mHasValidPlate(String pVehicleplate) {
		return pVehicleplate.matches("^[-A-Z0-9]+$");
	}

	public GarageLevelWrapperDto mWrapLevelModel(LevelModel pLevelModel) {
		GarageLevelWrapperDto lGarageLevelWrapperDto = new GarageLevelWrapperDto();

		lGarageLevelWrapperDto.setId(pLevelModel.getId());

		lGarageLevelWrapperDto.setLevel(new GarageLevelDto(pLevelModel.isInUse(), pLevelModel.getNbLevelLots()));
		List<VehicleModel> lVehicleModels = aDatabaseConnector.getAllVehicleForLevel(pLevelModel.getId());

		lGarageLevelWrapperDto.setNbOccupiedLots(lVehicleModels.size());
		lGarageLevelWrapperDto.setNbFreeLots(pLevelModel.getNbLevelLots() - lVehicleModels.size());

		List<VehicleWrapperDto> lVehiclesWrapperDto = new ArrayList<VehicleWrapperDto>();

		for (VehicleModel lVehicleModel : lVehicleModels) {
			lVehiclesWrapperDto.add(new VehicleWrapperDto(lVehicleModel.getLevel_id(), lVehicleModel.getLot_id(),
					new VehicleDto(lVehicleModel.getRegistration_id(), lVehicleModel.getType(),
							lVehicleModel.getNbWheels(), lVehicleModel.getBrand())));
		}

		lGarageLevelWrapperDto.setVehicles(lVehiclesWrapperDto);
		return lGarageLevelWrapperDto;
	}

}
