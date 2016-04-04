package garage.api.service;

import org.ldauvergne.garage.shared.models.VehicleModel;
import org.springframework.stereotype.Component;

@Component
public class GarageAPIService {
	
	public static final String INVALID_LICENCE_PLATE_MESSAGE = "Invalid licence plate, must be matching ^[-A-Z0-9]+$ regex";
	
	public boolean mIsValidPlate(String pVehicleplate) {
		return pVehicleplate.toUpperCase().matches("^[-A-Z0-9]+$");
	}
	
	public VehicleModel mCheckVehicle(VehicleModel pVehicle) {
		// Check if the license plate is valid
		if (!mIsValidPlate(pVehicle.getRegistration_id())){
			throw new IllegalArgumentException(INVALID_LICENCE_PLATE_MESSAGE);
		}

		// Add proper vehicle type depending of wheels number
		pVehicle = pVehicle.mCheckVehicleType();
		return pVehicle;
	}
}
