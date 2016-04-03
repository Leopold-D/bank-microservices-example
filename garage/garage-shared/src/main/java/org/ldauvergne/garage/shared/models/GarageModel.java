package org.ldauvergne.garage.shared.models;

import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GarageModel {
	// Can be used to update data
	public static Integer GARAGE_VERSION = 1;
	@Id
	private Integer nbOccupiedLots;
	private Integer nbFreeLots;

	private List<LevelModel> levels;
}
