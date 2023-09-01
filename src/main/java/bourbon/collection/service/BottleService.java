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

/**
 * Service class for controller methods
 * 
 * @author clayr
 *
 */
@Service
public class BottleService {

	// Dao instance variables for each entity
	@Autowired
	private BottleDao bottleDao;

	@Autowired
	private DistillerDao distillerDao;

	@Autowired
	private StoreDao storeDao;

	/**
	 * Save BottleData information from request to Bottle
	 * 
	 * @param bottleData
	 * @param distillerId
	 * @return new BottleData that is saved in the BottleDao
	 */
	@Transactional(readOnly = false)
	public BottleData saveBottle(BottleData bottleData, Long distillerId) {
		Distiller distiller = findDistillerById(distillerId);
		Bottle bottle = findOrCreateBottle(distillerId, bottleData.getBottleId());
		// Set distiller in bottle
		bottle.setDistiller(distiller);
		copyBottleFields(bottle, bottleData);
		// Add bottle to distiller bottle set
		distiller.getBottles().add(bottle);

		return new BottleData(bottleDao.save(bottle));
	}

	/**
	 * Copy bottle fields from bottleData to bottle
	 * 
	 * @param bottle
	 * @param bottleData
	 */
	private void copyBottleFields(Bottle bottle, BottleData bottleData) {
		bottle.setBottleId(bottleData.getBottleId());
		bottle.setName(bottleData.getName());
		bottle.setProof(bottleData.getProof());
		bottle.setLabel(bottleData.getLabel());
		bottle.setPrice(bottleData.getPrice());
	}

	/**
	 * Find bottle if it already exists, or create a new one.
	 * 
	 * @param distillerId
	 * @param bottleId
	 * @return Bottle (either new or existing)
	 */
	private Bottle findOrCreateBottle(Long distillerId, Long bottleId) {
		Bottle bottle;
		// if bottleId is empty, create new bottle, otherwise go find it.
		if (Objects.isNull(bottleId)) {
			bottle = new Bottle();
		} else {
			bottle = findBottleById(distillerId, bottleId);
		}

		return bottle;
	}

	/**
	 * Find by id in Dao, else throw NoSuchElementException with error message. If
	 * distillerId doesn't match distillerId of found bottle, throw
	 * IllegalArgumentException.
	 * 
	 * @param distillerId
	 * @param bottleId
	 * @return found bottle
	 */
	private Bottle findBottleById(Long distillerId, Long bottleId) {
		Bottle bottle = bottleDao.findById(bottleId)
				.orElseThrow(() -> new NoSuchElementException("Bottle with ID=" + bottleId + " does not exist."));

		if (bottle.getDistiller().getDistillerId() != distillerId) {
			throw new IllegalArgumentException(
					"Bottle with ID=" + bottleId + " is not made by distiller with ID=" + distillerId);
		}

		return bottle;
	}

	/**
	 * Save BourbonDistiller information to Distiller entity
	 * 
	 * @param bourbonDistiller
	 * @return new BourbonDistiller object
	 */
	@Transactional(readOnly = false)
	public BourbonDistiller saveDistiller(BourbonDistiller bourbonDistiller) {
		Long distillerId = bourbonDistiller.getDistillerId();
		Distiller distiller = findOrCreateDistiller(distillerId);

		copyDistillerFields(distiller, bourbonDistiller);

		Distiller dbDistiller = distillerDao.save(distiller);
		return new BourbonDistiller(dbDistiller);
	}

	/**
	 * Copy distiller fields from DTO to entity
	 * 
	 * @param distiller
	 * @param bourbonDistiller
	 */
	private void copyDistillerFields(Distiller distiller, BourbonDistiller bourbonDistiller) {
		distiller.setDistillerId(bourbonDistiller.getDistillerId());
		distiller.setDistillerName(bourbonDistiller.getDistillerName());
		distiller.setCity(bourbonDistiller.getCity());
		distiller.setState(bourbonDistiller.getState());
		distiller.setZip(bourbonDistiller.getZip());
	}

	/**
	 * Find distiller by ID or create a new Distiller
	 * 
	 * @param distillerId
	 * @return distiller (either found or new)
	 */
	private Distiller findOrCreateDistiller(Long distillerId) {
		Distiller distiller;

		if (Objects.isNull(distillerId)) {
			distiller = new Distiller();
		} else {
			distiller = findDistillerById(distillerId);
		}

		return distiller;
	}

	/**
	 * Find by ID in DistillerDao
	 * 
	 * @param distillerId
	 * @return distiller with matching ID or NoSuchElementException
	 */
	private Distiller findDistillerById(Long distillerId) {
		return distillerDao.findById(distillerId)
				.orElseThrow(() -> new NoSuchElementException("Distiller with ID=" + distillerId + " does not exist."));
	}

	/**
	 * Find all distillers by the Dao, add them to new List of BourbonDistiller
	 * 
	 * @return List of all BourbonDistiller
	 */
	@Transactional(readOnly = true)
	public List<BourbonDistiller> retrieveAllDistillers() {
		List<BourbonDistiller> bourbonDistiller = new LinkedList<>();

		for (Distiller distiller : distillerDao.findAll()) {
			BourbonDistiller bd = new BourbonDistiller(distiller);

			bourbonDistiller.add(bd);
		}

		return bourbonDistiller;
	}

	/**
	 * Find bottle by ID and delete it from the table using delete in the Dao.
	 * 
	 * @param bottleId
	 */
	@Transactional(readOnly = false)
	public void deleteBottleById(Long bottleId) {
		Bottle bottle = findBottleById(bottleId);

		bottleDao.delete(bottle);
	}

	/**
	 * Find bottle by ID, with only bottleId as a parameter.
	 * 
	 * @param bottleId
	 * @return Bottle with matching ID or NoSuchElementException
	 */
	@Transactional(readOnly = true)
	private Bottle findBottleById(Long bottleId) {
		return bottleDao.findById(bottleId)
				.orElseThrow(() -> new NoSuchElementException("Bottle with ID=" + bottleId + " does not exist."));
	}

	/**
	 * Find all bottles and add them to a new list
	 * 
	 * @return List of BottleData
	 */
	@Transactional(readOnly = true)
	public List<BottleData> retrieveAllBottles() {
		List<BottleData> bottles = new LinkedList<>();

		for (Bottle bottle : bottleDao.findAll()) {
			BottleData b = new BottleData(bottle);

			bottles.add(b);
		}

		return bottles;
	}

	/**
	 * Find bottle and store by ID and add them to each other's list
	 * 
	 * @param bourbonStore
	 * @param bottleId
	 * @return BourbonStore saved using Dao
	 */
	@Transactional(readOnly = false)
	public BourbonStore saveStore(BourbonStore bourbonStore, Long bottleId) {
		Bottle bottle = findBottleById(bottleId);
		Store store = findOrCreateStore(bourbonStore.getStoreId());

		copyStoreFields(store, bourbonStore);
		store.getBottles().add(bottle);
		bottle.getStores().add(store);

		return new BourbonStore(storeDao.save(store));
	}

	/**
	 * copy store fields from DTO to entity
	 * 
	 * @param store
	 * @param bourbonStore
	 */
	private void copyStoreFields(Store store, BourbonStore bourbonStore) {
		store.setStoreId(bourbonStore.getStoreId());
		store.setStoreName(bourbonStore.getStoreName());
		store.setStreetAddress(bourbonStore.getStreetAddress());
		store.setCity(bourbonStore.getCity());
		store.setState(bourbonStore.getState());
		store.setZip(bourbonStore.getZip());
	}

	/**
	 * Find store by ID or create new store
	 * 
	 * @param storeId
	 * @return store
	 */
	private Store findOrCreateStore(Long storeId) {
		Store store;

		if (Objects.isNull(storeId)) {
			store = new Store();
		} else {
			store = findStoreById(storeId);
		}

		return store;
	}

	/**
	 * Find Store by ID using Dao or throw NoSuchElementException
	 * 
	 * @param storeId
	 * @return
	 */
	@Transactional(readOnly = true)
	private Store findStoreById(Long storeId) {
		return storeDao.findById(storeId)
				.orElseThrow(() -> new NoSuchElementException("Store with ID=" + storeId + " does not exist."));
	}

	/**
	 * Save store using provided BourbonStore data
	 * 
	 * @param bourbonStore
	 * @return new Bourbon Store
	 */
	@Transactional(readOnly = false)
	public BourbonStore saveStore(BourbonStore bourbonStore) {
		Long storeId = bourbonStore.getStoreId();
		Store store = findOrCreateStore(storeId);

		copyStoreFields(store, bourbonStore);

		Store dbStore = storeDao.save(store);
		return new BourbonStore(dbStore);
	}

	/**
	 * Find Bottle by ID
	 * 
	 * @param bottleId
	 * @return new BottleData
	 */
	@Transactional(readOnly = true)
	public BottleData retrieveBottleById(Long bottleId) {
		Bottle bottle = findBottleById(bottleId);
		return new BottleData(bottle);
	}

	/**
	 * Find Store by Bottle ID and add to Set of BourbonStore
	 * 
	 * @param bottleId
	 * @return foundStores Set
	 */
	@Transactional(readOnly = true)
	public Set<BourbonStore> retrieveStoreByBottleId(Long bottleId) {
		Bottle bottle = findBottleById(bottleId);
		Set<BourbonStore> foundStores = new HashSet<>();

		for (Store store : bottle.getStores()) {
			foundStores.add(new BourbonStore(store));
		}

		return foundStores;
	}

	/**
	 * Retrieve Distiller by ID
	 * 
	 * @param distillerId
	 * @return BourbonDistiller that matches ID
	 */
	@Transactional(readOnly = true)
	public BourbonDistiller retrieveDistillerById(Long distillerId) {
		Distiller distiller = findDistillerById(distillerId);
		return new BourbonDistiller(distiller);
	}

	/**
	 * Delete distiller with matching ID using Dao delete method
	 * 
	 * @param distillerId
	 */
	@Transactional(readOnly = false)
	public void deleteDistillerById(Long distillerId) {
		Distiller distiller = findDistillerById(distillerId);

		distillerDao.delete(distiller);
	}

	/**
	 * Delete store with matching ID using Dao delete method
	 * 
	 * @param storeId
	 */
	@Transactional(readOnly = false)
	public void deleteStoreById(Long storeId) {
		Store store = findStoreById(storeId);

		storeDao.delete(store);

	}

	/**
	 * Save existing store to existing bottle
	 * 
	 * @param storeId
	 * @param bottleId
	 * @return BottleData with bottle info
	 */
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
