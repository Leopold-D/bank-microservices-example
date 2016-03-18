package org.ldauvergne.garage.shared.dto.clients;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Ensure having abstract class to allow several Vehicles types
 * @author Leopold Dauvergne
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDto 
{
	String registration_id;
	VehicleType type;
	Integer nbWheels;
	String brand;
}
