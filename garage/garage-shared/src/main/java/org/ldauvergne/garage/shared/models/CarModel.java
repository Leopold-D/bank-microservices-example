package org.ldauvergne.garage.shared.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class CarModel extends VehicleModel{
	// Car properties should be held here
	CarModel(VehicleModel lCar){
		super(lCar);
		setVehicleType(VehicleType.CAR);
	}
}
