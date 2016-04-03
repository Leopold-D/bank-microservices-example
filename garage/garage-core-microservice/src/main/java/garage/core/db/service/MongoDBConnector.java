package garage.core.db.service;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.ldauvergne.garage.shared.models.LevelModel;
import org.ldauvergne.garage.shared.models.VehicleModel;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import lombok.Getter;

/**
 * 
 * @author Leopold Dauvergne
 *
 */
@Component
public class MongoDBConnector implements DatabaseConnector {

	private static final String MONGODB_BASE_ADDR = "mongodb://garage_user:garage_pw@ds013579.mlab.com:13579/garage";
	private static final String MONGODB_BASE_NAME = "garage";

	@Getter
	private MongoOperations mongoOps;

	public MongoDBConnector() {
		try {
			MongoClientURI uri = new MongoClientURI(MONGODB_BASE_ADDR);
			MongoClient client = new MongoClient(uri);

			mongoOps = new MongoTemplate(client, MONGODB_BASE_NAME);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		clearGarage();
		initGarage();
	}

	@Override
	public void initGarage() {
		mongoOps.createCollection(LevelModel.class);
		mongoOps.createCollection(VehicleModel.class);
	}

	@Override
	public void clearGarage() {
		mongoOps.dropCollection(LevelModel.class);
		mongoOps.dropCollection(VehicleModel.class);
	}

	/**
	 * Vehicle
	 */

	@Override
	public void add(VehicleModel model) {
		mongoOps.insert(model);
	}

	@Override
	public List<VehicleModel> getVehicles() {
		return mongoOps.findAll(VehicleModel.class);
	}

	@Override
	public List<VehicleModel> getAllVehicleForLevel(Integer level_id) {
		return mongoOps.find(new Query(where("level_id").is(level_id)), VehicleModel.class);
	}

	@Override
	public VehicleModel getVehicle(String registration_id) {
		return mongoOps.findOne(new Query(Criteria.where("_id").is(registration_id)), VehicleModel.class);
	}

	@Override
	public VehicleModel removeVehicle(String registration_id) {
		return mongoOps.findAndRemove(new Query(Criteria.where("_id").is(registration_id)), VehicleModel.class);
	}

	/**
	 * Level
	 */
	@Override
	public void add(LevelModel model) {
		mongoOps.insert(model);
	}

	@Override
	public List<LevelModel> getLevels() {
		List<LevelModel> lLevels = mongoOps.findAll(LevelModel.class);
		// Java8 way of sorting result from MongoDB on level id and not last updated
		Collections.sort(lLevels, (a, b) -> a.getId() < b.getId() ? -1 : a.getId() == b.getId() ? 0 : 1);
		return lLevels;
	}

	@Override
	public LevelModel getLevel(Integer level_id) {
		return mongoOps.findOne(new Query(Criteria.where("_id").is(level_id)), LevelModel.class);
	}

	@Override
	public void modifyLevel(LevelModel level) {
		mongoOps.save(level);
	}

	@Override
	public void removeLevel(Integer level_id) {
		mongoOps.findAndRemove(new Query(Criteria.where("_id").is(level_id)), LevelModel.class);
	}

	class LevelDtoComparator implements Comparator<LevelModel> {
		@Override
		public int compare(LevelModel a, LevelModel b) {
			return a.getId() < b.getId() ? -1 : a.getId() == b.getId() ? 0 : 1;
		}
	}

}