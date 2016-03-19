package garage.core.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * This is a model to be mapped in DB using DAO
 * 
 * @author Leopold Dauvergne
 *
 */
@Data
@AllArgsConstructor
public class LotModel {
	Integer level_id;
	Integer lot_id;
}
