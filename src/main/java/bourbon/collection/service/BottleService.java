package bourbon.collection.service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bourbon.collection.controller.model.BottleData;
import bourbon.collection.controller.model.BottleData.BourbonDistiller;
import bourbon.collection.controller.model.BottleData.BourbonStore;
import bourbon.collection.dao.BottleDao;
import bourbon.collection.dao.DistillerDao;
import bourbon.collection.dao.StoreDao;
import bourbon.collection.entity.Bottle;
import bourbon.collection.entity.Distiller;
import bourbon.collection.entity.Store;

@Service
public class BottleService {

	@Autowired
	private BottleDao bottleDao;

	@Autowired
	private DistillerDao distillerDao;

	@Autowired
	private StoreDao storeDao;

	@Transactional(readOnly = false)
	public BottleData saveBottle(BottleData bottleData, Long distillerId) {
		Distiller distiller = findDistillerById(distillerId);
		Bottle bottle = findOrCreateBottle(distillerId, bottleData.getBottleId());

		bottle.setDistiller(distiller);
		copyBottleFields(bottle, bottleData);
		distiller.getBottles().add(bottle);

		return new BottleData(bottleDao.save(bottle));
	}

	private void copyBottleFields(Bottle bottle, BottleData bottleData) {
		bottle.setBottleId(bottleData.getBottleId());
		bottle.setName(bottleData.getName());
		bottle.setProof(bottleData.getProof());
		bottle.setLabel(bottleData.getLabel());
		bottle.setPrice(bottleData.getPrice());
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

	@Transactional(readOnly = false)
	public void deleteBottleById(Long bottleId) {
		Bottle bottle = findBottleById(bottleId);

		bottleDao.delete(bottle);
	}

	@Transactional(readOnly = true)
	private Bottle findBottleById(Long bottleId) {
		return bottleDao.findById(bottleId)
				.orElseThrow(() -> new NoSuchElementException("Bottle with ID=" + bottleId + " does not exist."));
	}

	public List<BottleData> retrieveAllBottles() {
		List<BottleData> bottles = new LinkedList<>();

		for (Bottle bottle : bottleDao.findAll()) {
			BottleData b = new BottleData(bottle);

			bottles.add(b);
		}

		return bottles;
	}

	@Transactional(readOnly = false)
	public BourbonStore saveStore(BourbonStore bourbonStore, Long bottleId) {
		Bottle bottle = findBottleById(bottleId);
		Store store = findOrCreateStore(bourbonStore.getStoreId());

		copyStoreFields(store, bourbonStore);
		store.getBottles().add(bottle);
		bottle.getStores().add(store);

		return new BourbonStore(storeDao.save(store));
	}

	private void copyStoreFields(Store store, BourbonStore bourbonStore) {
		store.setStoreId(bourbonStore.getStoreId());
		store.setStoreName(bourbonStore.getStoreName());
		store.setStreetAddress(bourbonStore.getStreetAddress());
		store.setCity(bourbonStore.getCity());
		store.setState(bourbonStore.getState());
		store.setZip(bourbonStore.getZip());
	}

	private Store findOrCreateStore(Long storeId) {
		Store store;

		if (Objects.isNull(storeId)) {
			store = new Store();
		} else {
			store = findStoreById(storeId);
		}

		return store;
	}

	@Transactional(readOnly = true)
	private Store findStoreById(Long storeId) {
		return storeDao.findById(storeId)
				.orElseThrow(() -> new NoSuchElementException("Store with ID=" + storeId + " does not exist."));
	}

	@Transactional(readOnly = false)
	public BourbonStore saveStore(BourbonStore bourbonStore) {
		Long storeId = bourbonStore.getStoreId();
		Store store = findOrCreateStore(storeId);

		copyStoreFields(store, bourbonStore);

		Store dbStore = storeDao.save(store);
		return new BourbonStore(dbStore);
	}

	@Transactional(readOnly = true)
	public BottleData retrieveBottleById(Long bottleId) {
		Bottle bottle = findBottleById(bottleId);
		return new BottleData(bottle);
	}

	@Transactional(readOnly = true)
	public Set<BourbonStore> retrieveStoreByBottleId(Long bottleId) {
		Bottle bottle = findBottleById(bottleId);
		Set<BourbonStore> foundStores = new HashSet<>();

		for (Store store : bottle.getStores()) {
			foundStores.add(new BourbonStore(store));
		}

		return foundStores;
	}

	@Transactional(readOnly = true)
	public BourbonDistiller retrieveDistillerById(Long distillerId) {
		Distiller distiller = findDistillerById(distillerId);
		return new BourbonDistiller(distiller);
	}

	@Transactional(readOnly = false)
	public void deleteDistillerById(Long distillerId) {
		Distiller distiller = findDistillerById(distillerId);
		
		distillerDao.delete(distiller);
	}

	@Transactional(readOnly = false)
	public void deleteStoreById(Long storeId) {
		Store store = findStoreById(storeId);
		
		storeDao.delete(store);
		
	}

	public BottleData saveStoreToBottle(Long storeId, Long bottleId) {
		Store store = findStoreById(storeId);
		Bottle bottle = findBottleById(bottleId);
		
		store.getBottles().add(bottle);
		bottle.getStores().add(store);
		
		storeDao.save(store);
		bottleDao.save(bottle);
		
		return new BottleData(bottle);
	}

}
