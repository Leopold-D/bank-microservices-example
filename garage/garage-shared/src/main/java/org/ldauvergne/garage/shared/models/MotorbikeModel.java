package org.ldauvergne.garage.shared.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class MotorbikeModel extends VehicleModel{
	// Specific Motorbike properties should be held here
	MotorbikeModel(VehicleModel lMotorbike){
		super(lMotorbike);
		setType(VehicleType.MOTORBIKE);
	}
}
