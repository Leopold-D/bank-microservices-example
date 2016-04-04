package garage.core.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.NotFoundException;

import org.ldauvergne.garage.shared.models.LevelModel;
import org.ldauvergne.garage.shared.models.LotModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import garage.core.db.service.DatabaseConnector;
import lombok.Getter;

/**
 * 
 * @author Leopold Dauvergne
 *
 */
@Component
public class GarageCoreService {

	/**
	 * Wrapping the hard computation
	 */
	@Autowired
	@Getter
	private DatabaseConnector aDatabaseConnector;

	private static final Logger LOG = LoggerFactory.getLogger(GarageCoreService.class);

	public LotModel mGetLot(String pVehicleplate) {
		// Browse through levels
		LOG.info("Browsing through levels");
		for (LevelModel lLevel : aDatabaseConnector.getLevels()) {
			// Check if level is in use
			LOG.info("Checking if level is in use");
			if (lLevel.isInUse()) {
				// Return the first free lot
				if(!lLevel.getFreeLots().isEmpty()){
					LOG.info("Booking lot level " + lLevel.getId());
					LotModel newLot = mBookLot(lLevel,pVehicleplate);
					return newLot;
				}
			}
		}
		return null;
	}

	private LotModel mBookLot(LevelModel lLevel,String pVehicleplate) {
		LotModel lot = lLevel.getFreeLots().remove(0);
		lot.setVehicle_id(pVehicleplate);
		lLevel.getOccupiedLots().add(lot);
		aDatabaseConnector.modifyLevel(lLevel);
		return lot;
	}
	
	public void mFreeLot(LevelModel pLevel, LotModel pLot) throws NotFoundException {
		if(pLevel.getOccupiedLots().remove(pLot)){
			//Delete the vehicle from the lot
			pLot.setVehicle_id(null);
			pLevel.getFreeLots().add(pLot);
			aDatabaseConnector.modifyLevel(pLevel);
		}
		else{
			throw (new NotFoundException("Vehicle not found"));
		}
	}
	
	public void mCreateLevel(LevelModel pGarageLevel) {
		LOG.info("Creating new level with " + pGarageLevel.getNbLevelLots() + " lots");

		// Init lots
		List<LotModel> listLots = new ArrayList<LotModel>();
		for (int i = 0; i < pGarageLevel.getNbLevelLots(); i++) {
			listLots.add(new LotModel(i, aDatabaseConnector.getLevels().size(), null));
		}
		pGarageLevel.setId(aDatabaseConnector.getLevels().size());
		pGarageLevel.setFreeLots(listLots);
		pGarageLevel.setOccupiedLots(new ArrayList<LotModel>());
		
		aDatabaseConnector.add(pGarageLevel);
	}
	
}
