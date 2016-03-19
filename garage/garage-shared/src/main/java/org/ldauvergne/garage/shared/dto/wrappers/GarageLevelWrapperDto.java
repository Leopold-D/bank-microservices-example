package org.ldauvergne.garage.shared.dto.wrappers;

import java.util.List;

import org.ldauvergne.garage.shared.dto.structure.GarageLevelDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GarageLevelWrapperDto {
	Integer id;
	Integer nbOccupiedLots;
	Integer nbFreeLots;

	GarageLevelDto level;
	List<VehicleWrapperDto> vehicles;
}
