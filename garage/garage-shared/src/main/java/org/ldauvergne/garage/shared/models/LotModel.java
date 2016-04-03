package org.ldauvergne.garage.shared.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LotModel {
	//Can be used to update data
	public static Integer GARAGE_VERSION=GarageModel.GARAGE_VERSION;
	private Integer id;
	private Integer level_id;
	private String vehicle_id;
}
