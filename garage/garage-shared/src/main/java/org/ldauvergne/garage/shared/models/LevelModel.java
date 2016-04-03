package org.ldauvergne.garage.shared.models;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LevelModel {
	//Can be used to update data
	public static Integer GARAGE_VERSION=GarageModel.GARAGE_VERSION;
	
	@Id
	private Integer id;
	
	boolean inUse;
	private Integer nbLevelLots;
	
	
	private List<LotModel> freeLots=new ArrayList<LotModel>();
	private List<LotModel> occupiedLots=new ArrayList<LotModel>();
}
