package garage.core.db.service;

import java.util.List;

import garage.core.db.model.LevelModel;
import garage.core.db.model.VehicleModel;
/**
 * 
 * @author Leopold Dauvergne
 * This Interface does not comply to coding rules
 */
public interface DatabaseConnector {

	boolean add(VehicleModel vehicleModel);
	
	List<VehicleModel> getVehicles();
	
	List<VehicleModel> getAllVehicleForLevel(Integer level_id);

	VehicleModel getVehicle(String registration_id);

	boolean removeVehicle(String registration_id);

	boolean add(LevelModel model);

	List<LevelModel> getLevels();
	
	LevelModel getLevel(Integer level_id);

	boolean modifyLevel(LevelModel level);

	boolean removeLevel(Integer level_id);

	void clearGarage();

	void initGarage();

}
