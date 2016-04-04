package org.ldauvergne.garage.shared.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class MotorbikeModel extends VehicleModel{
	// Motorbike properties should be held here
	MotorbikeModel(VehicleModel lMotorbike){
		super(lMotorbike);
		setVehicleType(VehicleType.MOTORBIKE);
	}
}
