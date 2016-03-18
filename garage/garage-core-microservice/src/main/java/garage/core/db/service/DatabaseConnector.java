package garage.core.db.service;

import java.util.List;

import garage.core.db.model.LevelModel;
import garage.core.db.model.VehicleModel;

public interface DatabaseConnector {

	boolean add(VehicleModel vehicleModel);
	
	List<VehicleModel> getVehicles();
	
	List<VehicleModel> findAllVehicleForLevel(Integer level_id);

	VehicleModel findVehicle(String registration_id);

	boolean removeVehicle(String registration_id);

	boolean add(LevelModel model);

	List<LevelModel> getLevels();
	
	LevelModel findLevel(String level_id);

	boolean removeLevel(String level_id);

	void clearGarage();

	void initGarage();

}
