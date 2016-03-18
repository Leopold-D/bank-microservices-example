package org.ldauvergne.garage.shared.dto.wrappers;

import org.ldauvergne.garage.shared.dto.clients.VehicleDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleWrapperDto {
	Integer level;
	Integer lot;
	VehicleDto vehicle;
}
