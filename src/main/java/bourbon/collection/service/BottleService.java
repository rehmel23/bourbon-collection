package bourbon.collection.service;

import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bourbon.collection.controller.model.BottleData;
import bourbon.collection.dao.BottleDao;
import bourbon.collection.entity.Bottle;

@Service
public class BottleService {

	@Autowired
	private BottleDao bottleDao;

	@Transactional(readOnly = false)
	public BottleData saveBottle(BottleData bottleData) {
		Long bottleId = bottleData.getBottleId();
		Bottle bottle = findOrCreateBottle(bottleId);

		copyBottleFields(bottle, bottleData);

		Bottle dbBottle = bottleDao.save(bottle);
		return new BottleData(dbBottle);
	}

	private void copyBottleFields(Bottle bottle, BottleData bottleData) {
		bottle.setBottleId(bottleData.getBottleId());
		bottle.setName(bottleData.getName());
		bottle.setProof(bottleData.getProof());
		bottle.setLabel(bottleData.getLabel());
		bottle.setPrice(bottleData.getPrice());
		bottle.setDistiller(bottleData.getDistiller());
	}

	private Bottle findOrCreateBottle(Long bottleId) {
		Bottle bottle;

		if (Objects.isNull(bottleId)) {
			bottle = new Bottle();
		} else {
			bottle = findBottleById(bottleId);
		}

		return bottle;
	}

	private Bottle findBottleById(Long bottleId) {
		return bottleDao.findById(bottleId)
				.orElseThrow(() -> new NoSuchElementException("Bottle with ID=" + bottleId + " does not exist."));
	}

}
