package bourbon.collection.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import bourbon.collection.controller.model.BottleData;
import bourbon.collection.controller.model.BottleData.BourbonDistiller;
import bourbon.collection.controller.model.BottleData.BourbonStore;
import bourbon.collection.service.BottleService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/bourbon_collection")
@Slf4j
public class BourbonCollectionController {

	@Autowired
	private BottleService bottleService;

	/**
	 * POST new bottle to collection
	 * 
	 * @param distillerId - PathVariable
	 * @param bottleData  - RequestBody
	 * @return BottleData
	 * 
	 *         Infinite loop is fixed. Returns 500 status. IllegalArgumentException:
	 *         The given id must not be null. Distiller id?
	 */
	@PostMapping("/{distillerId}/bottle")
	@ResponseStatus(code = HttpStatus.CREATED)
	public BottleData insertBottle(@PathVariable Long distillerId, @RequestBody BottleData bottleData) {
		log.info("Adding bottle {} to collection made by distiller {}.", bottleData, distillerId);
		return bottleService.saveBottle(bottleData, distillerId);
	}

	/**
	 * POST new distillery to database
	 * 
	 * @param bourbonDistiller
	 * @return Distiller information
	 */
	@PostMapping("/distiller")
	@ResponseStatus(code = HttpStatus.CREATED)
	public BourbonDistiller addDistiller(@RequestBody BourbonDistiller bourbonDistiller) {
		log.info("Adding distiller, {}.", bourbonDistiller.getDistillerName());
		return bottleService.saveDistiller(bourbonDistiller);
	}

	// POST new store to bottle
	@PostMapping("/store/{bottleId}")
	public BourbonStore addStoreToBottle(@RequestBody BourbonStore bourbonStore, @PathVariable Long bottleId) {
		log.info("Adding store, {}, to bottle {}.", bourbonStore.getStoreName(), bottleId);
		return bottleService.saveStore(bourbonStore, bottleId);
	}

	/**
	 * GET all distilleries to make bottle POST easier for user to find distillerId.
	 */
	@GetMapping("/distillers")
	public List<BourbonDistiller> listAllDistillers() {
		log.info("Listing all distilleries.");
		return bottleService.retrieveAllDistillers();
	}

	// GET distillery by ID
	@GetMapping("/distiller/{distillerId}")
	public BourbonDistiller findDistillerById(Long distillerId) {
		log.info("Retrieving distiller with ID={}", distillerId);
		return bottleService.retrieveDistillerById(distillerId);
	}

	@GetMapping("/bottles")
	public List<BottleData> listAllBottlesInCollection() {
		log.info("Listing all bottles in the collection.");
		return bottleService.retrieveAllBottles();
	}

	// GET Bottle by ID
	@GetMapping("/bottle/{bottleId}")
	public BottleData findBottleById(@PathVariable Long bottleId) {
		log.info("Retrieving bottle with ID={}", bottleId);
		return bottleService.retrieveBottleById(bottleId);
	}

	// GET Store by Bottle ID (Where did I buy that?)
	@GetMapping("/store/bottle/{bottleId}")
	public Set<BourbonStore> findStoreByBottleId(@PathVariable Long bottleId) {
		log.info("Retrieving store for bottle with ID={}", bottleId);
		return bottleService.retrieveStoreByBottleId(bottleId);
	}

	/*
	 *  PUT for bottle *TEST*
	 *  
	 *  500 Error due to null distillierId
	 */
	@PutMapping("/bottle/{bottleId}")
	public BottleData updateBottle(@PathVariable Long bottleId, @RequestBody BottleData bottleData) {
		bottleData.setBottleId(bottleId);
		log.info("Updating bottle {}", bottleId);
		return bottleService.saveBottle(bottleData);
	}
	/*
	 *  PUT for distillery
	 *  
	 *  
	 */
	@PutMapping("/distiller/{distillerId}")
	public BourbonDistiller updateDistillery(@PathVariable Long distillerId, @RequestBody BourbonDistiller bourbonDistiller) {
		bourbonDistiller.setDistillerId(distillerId);
		log.info("Updating distiller {}", distillerId);
		return bottleService.saveDistiller(bourbonDistiller);
	}
	
	/*
	 *  PUT for store
	 *  
	 *  Creates new store with each request.
	 */
	@PutMapping("/store/{storeId}")
	public BourbonStore updateStore(@PathVariable Long storeId, @RequestBody BourbonStore bourbonStore) {
		bourbonStore.setStoreId(storeId);
		log.info("Updating store {}", storeId);
		return bottleService.saveStore(bourbonStore);
	}
	
	/**
	 * DELETE single bottle by ID
	 * 
	 * @param bottleId
	 * @return
	 */
	@DeleteMapping("/bottle/{bottleId}")
	public Map<String, String> deleteBottleById(@PathVariable Long bottleId) {
		log.info("Deleting bottle with ID={}", bottleId);
		bottleService.deleteBottleById(bottleId);

		return Map.of("message", "Deletion of bottle with ID=" + bottleId + " was successful.");
	}

	// DELETE distiller by ID
	@DeleteMapping("/distiller/{distillerId}")
	public Map<String, String> deleteDistillerById(@PathVariable Long distillerId){
		log.info("Deleting distiller with ID={}", distillerId);
		bottleService.deleteDistillerById(distillerId);
		
		return Map.of("message", "Deletion of distiller with ID=" + distillerId + " was successful.");
	}
	
	// DELETE store by ID
	@DeleteMapping("/store/{storeId}")
	public Map<String, String> deleteStoreById(@PathVariable Long storeId){
		log.info("Deleting store with ID={}", storeId);
		bottleService.deleteStoreById(storeId);
		
		return Map.of("message", "Deletion of store with ID=" + storeId + " was successful.");
	}
}
