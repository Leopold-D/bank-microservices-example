package garage.core.db.model;

import org.ldauvergne.garage.shared.dto.clients.VehicleType;
import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
/**
 * This is a model
 * @author Leopold Dauvergne
 * Does not use coding rules
 */
@Data
@AllArgsConstructor
public class VehicleModel {
	@Id
	String registration_id;
	Integer level_id;
	Integer lot_id;
	
	VehicleType type;
	Integer nbWheels;
	String brand;
}
