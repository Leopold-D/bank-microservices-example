package org.ldauvergne.garage.shared.dto.clients;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

//We do not have any special use case for motorbikes yet but that may be useful later
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
public class CarDto extends VehicleDto{
	

}
