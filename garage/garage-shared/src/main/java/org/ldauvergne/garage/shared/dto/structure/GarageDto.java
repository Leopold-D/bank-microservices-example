package org.ldauvergne.garage.shared.dto.structure;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GarageDto {
	List<GarageLevelDto> levels;
}
