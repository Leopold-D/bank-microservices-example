package garage.core.db.service;

import java.util.List;

import org.ldauvergne.garage.shared.models.LevelModel;
import org.ldauvergne.garage.shared.models.VehicleModel;
/**
 * 
 * @author Leopold Dauvergne
 * 
 */
public interface DatabaseConnector {

	void add(VehicleModel vehicleModel);
	
	List<VehicleModel> getVehicles();
	
	List<VehicleModel> getAllVehicleForLevel(Integer level_id);

	VehicleModel getVehicle(String registration_id);

	VehicleModel removeVehicle(String registration_id);

	void add(LevelModel model);

	List<LevelModel> getLevels();
	
	LevelModel getLevel(Integer level_id);

	void modifyLevel(LevelModel level);

	void removeLevel(Integer level_id);
	
	void clearGarage();

	void initGarage();

}
