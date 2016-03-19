package org.ldauvergne.garage.shared.dto.structure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GarageLevelDto {
	boolean inUse;
	Integer nbLevelLots;
}
