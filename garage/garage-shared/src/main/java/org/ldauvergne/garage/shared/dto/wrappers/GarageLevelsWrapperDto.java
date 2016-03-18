package org.ldauvergne.garage.shared.dto.wrappers;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class GarageLevelsWrapperDto{
	Integer nbLevels=0;
	Integer nbTotalLots=0;
	Integer nbOccupiedLots=0;
	Integer nbFreeLots=0;
	
	List<GarageLevelWrapperDto> levels;
}
