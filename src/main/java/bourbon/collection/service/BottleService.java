package bourbon.collection.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bourbon.collection.controller.model.BottleData;
import bourbon.collection.controller.model.BottleData.BourbonDistiller;
import bourbon.collection.dao.BottleDao;
import bourbon.collection.dao.DistillerDao;
import bourbon.collection.entity.Bottle;
import bourbon.collection.entity.Distiller;

@Service
public class BottleService {

	@Autowired
	private BottleDao bottleDao;

	@Autowired
	private DistillerDao distillerDao;

	@Transactional(readOnly = false)
	public BottleData saveBottle(BottleData bottleData, Long distillerId) {
		Distiller distiller = findDistillerById(distillerId);
		Bottle bottle = findOrCreateBottle(distillerId, bottleData.getBottleId());
		
		copyBottleFields(bottle, bottleData);
		bottle.setDistiller(distiller);
		distiller.getBottles().add(bottle);
		
		return new BottleData(bottleDao.save(bottle));
	}

	private void copyBottleFields(Bottle bottle, BottleData bottleData) {
		bottle.setBottleId(bottleData.getBottleId());
		bottle.setName(bottleData.getName());
		bottle.setProof(bottleData.getProof());
		bottle.setLabel(bottleData.getLabel());
		bottle.setPrice(bottleData.getPrice());
		bottle.setDistiller(bottleData.getDistiller());
	}

	private Bottle findOrCreateBottle(Long distillerId, Long bottleId) {
		Bottle bottle;

		if (Objects.isNull(bottleId)) {
			bottle = new Bottle();
		} else {
			bottle = findBottleById(distillerId, bottleId);
		}

		return bottle;
	}

	private Bottle findBottleById(Long distillerId, Long bottleId) {
		Bottle bottle = bottleDao.findById(bottleId)
				.orElseThrow(() -> new NoSuchElementException("Bottle with ID=" + bottleId + " does not exist."));

		if (bottle.getDistiller().getDistillerId() != distillerId) {
			throw new IllegalArgumentException(
					"Bottle with ID=" + bottleId + " is not made by distiller with ID=" + distillerId);
		}

		return bottle;
	}
	
	@Transactional(readOnly = false)
	public BourbonDistiller saveDistiller(BourbonDistiller bourbonDistiller) {
		Long distillerId = bourbonDistiller.getDistillerId();
		Distiller distiller = findOrCreateDistiller(distillerId);

		copyDistillerFields(distiller, bourbonDistiller);

		Distiller dbDistiller = distillerDao.save(distiller);
		return new BourbonDistiller(dbDistiller);
	}

	private void copyDistillerFields(Distiller distiller, BourbonDistiller bourbonDistiller) {
		distiller.setDistillerId(bourbonDistiller.getDistillerId());
		distiller.setDistillerName(bourbonDistiller.getDistillerName());
		distiller.setCity(bourbonDistiller.getCity());
		distiller.setState(bourbonDistiller.getState());
		distiller.setZip(bourbonDistiller.getZip());
	}

	private Distiller findOrCreateDistiller(Long distillerId) {
		Distiller distiller;

		if (Objects.isNull(distillerId)) {
			distiller = new Distiller();
		} else {
			distiller = findDistillerById(distillerId);
		}

		return distiller;
	}

	private Distiller findDistillerById(Long distillerId) {
		return distillerDao.findById(distillerId)
				.orElseThrow(() -> new NoSuchElementException("Distiller with ID=" + distillerId + " does not exist."));
	}
	
	@Transactional(readOnly = true)
	public List<BourbonDistiller> retrieveAllDistillers() {
		List<BourbonDistiller> bourbonDistiller = new LinkedList<>();
		
		for (Distiller distiller : distillerDao.findAll()) {
			BourbonDistiller bd = new BourbonDistiller(distiller);
			
			bourbonDistiller.add(bd);
		}
			
		return bourbonDistiller;
	}

}
