package garage.core.db.service;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.net.UnknownHostException;
import java.util.List;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import garage.core.db.model.LevelModel;
import garage.core.db.model.VehicleModel;
import lombok.Getter;

@Component
public class MongoDBConnector implements DatabaseConnector {

	@Getter
	private MongoOperations mongoOps;

	// TODO: DB Connection assets in variables
	public MongoDBConnector() {
		try {
			MongoClientURI uri = new MongoClientURI("mongodb://garage_user:garage_pw@ds013579.mlab.com:13579/garage");
			MongoClient client = new MongoClient(uri);

			mongoOps = new MongoTemplate(client, "garage");
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

	// TODO: Generic methods
	/**
	 * Vehicle
	 */

	@Override
	public boolean add(VehicleModel model) {
		try {
			mongoOps.insert(model);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public List<VehicleModel> getVehicles() {
		return mongoOps.findAll(VehicleModel.class);
	}

	@Override
	public List<VehicleModel> findAllVehicleForLevel(Integer level_id) {
		return mongoOps.find(new Query(where("level_id").is(level_id)), VehicleModel.class);
	}

	@Override
	public VehicleModel findVehicle(String registration_id) {
		return mongoOps.findOne(new Query(Criteria.where("_id").is(registration_id)), VehicleModel.class);
	}

	@Override
	public boolean removeVehicle(String registration_id) {
		if (mongoOps.findAndRemove(new Query(Criteria.where("_id").is(registration_id)), VehicleModel.class) == null) {
			return false;
		}
		return true;
	}

	/**
	 * Level
	 */

	@Override
	public boolean add(LevelModel model) {
		try {
			mongoOps.insert(model);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public List<LevelModel> getLevels() {
		return mongoOps.findAll(LevelModel.class);
	}

	@Override
	public LevelModel findLevel(String level_id) {
		return mongoOps.findOne(new Query(Criteria.where("id").is(level_id)), LevelModel.class);
	}

	@Override
	public boolean removeLevel(String level_id) {
		try {
			mongoOps.findAndRemove(new Query(Criteria.where("id").is(level_id)), LevelModel.class);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}