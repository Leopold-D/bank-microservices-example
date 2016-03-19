package garage.core.db.model;

import java.util.Map;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is a model
 * @author Leopold Dauvergne
 * Does not use coding rules
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LevelModel {
	@Id
	Integer id;	
	boolean inUse;
	Integer nbLevelLots;
	
	//Map<Integer, String> occupiedLots; //Lot id & Vehicle id

}
