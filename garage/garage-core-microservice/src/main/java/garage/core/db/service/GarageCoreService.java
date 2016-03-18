package garage.core.db.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import garage.core.db.model.LevelModel;
import garage.core.db.model.VehicleModel;
import garage.core.service.classes.LotModel;
import lombok.Getter;

//TODO: Store in DB
@Component
public class GarageCoreService {

	/**
	 * The two maps represent the DB tables
	 */

	@Autowired
	@Getter
	private DatabaseConnector databaseConnector;
	
	private static final Logger LOG = LoggerFactory.getLogger(GarageCoreService.class);
	
	//TODO: Expensive, should be reworked within model maybe, simulates well the time a user needs to find a free lot
	public LotModel mFindLot() {
		//Browse through levels
		LOG.info("Browsing through levels");
		for(LevelModel level : databaseConnector.getLevels()){
			List<VehicleModel> vehicles = databaseConnector.findAllVehicleForLevel(level.getId());
			//Check if level has free lots
			LOG.info("Checking if level has free lots");
			if(level.getNbLevelLots() > vehicles.size()){
				
				//Get occupied lots
				LOG.info("Getting occupied lots");
				List<Integer> occupied_lots = new ArrayList<Integer>();
				for(VehicleModel vehicle : vehicles){
					occupied_lots.add(vehicle.getLot_id());
				}
				//Browse through lots to get next free
				LOG.info("Browse through lots to get next free");
				for(int i=0;i<level.getNbLevelLots();i++){
					if(!occupied_lots.contains(i)){
						return (new LotModel(level.getId(),i)); 
					}
				}
			}
		}
		return null;
	}

	public boolean mHasValidPlate(String vehicleplate) {
		return vehicleplate.matches("^[-A-Z0-9]+$");
	}



}
