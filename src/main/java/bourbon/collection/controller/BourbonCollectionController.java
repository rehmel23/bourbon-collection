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

/**
 * Bourbon Collection Application Controllers - define what each CRUD HTTP
 * request does
 * 
 * @author clayr
 *
 */
@RestController
@RequestMapping("/bourbon_collection")
@Slf4j
public class BourbonCollectionController {

	// Instance variable of BottleService class to be called by Controller methods
	@Autowired
	private BottleService bottleService;

	/**
	 * POST new bottle to collection
	 * 
	 * @param distillerId - PathVariable
	 * @param bottleData  - RequestBody
	 * @return BottleData
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
	 * @param bourbonDistiller - RequestBody
	 * @return Distiller information
	 */
	@PostMapping("/distiller")
	@ResponseStatus(code = HttpStatus.CREATED)
	public BourbonDistiller addDistiller(@RequestBody BourbonDistiller bourbonDistiller) {
		log.info("Adding distiller {}", bourbonDistiller.getDistillerName());
		return bottleService.saveDistiller(bourbonDistiller);
	}

	/**
	 * POST new store to bottle
	 * 
	 * @param bourbonStore - RequestBody
	 * @param bottleId     - PathVariable
	 * @return Store information
	 */
	@PostMapping("/store/{bottleId}")
	@ResponseStatus(code = HttpStatus.CREATED)
	public BourbonStore addNewStoreToBottle(@RequestBody BourbonStore bourbonStore, @PathVariable Long bottleId) {
		log.info("Adding new store {} to bottle {}", bourbonStore.getStoreName(), bottleId);
		return bottleService.saveStore(bourbonStore, bottleId);
	}

	/**
	 * POST to connect existing bottle to existing store
	 * 
	 * @param storeId  - PathVariable
	 * @param bottleId - PathVariable
	 * @return Store information
	 */
	@PostMapping("/store/{storeId}/bottle/{bottleId}")
	public BottleData addExistingStoreToBottle(@PathVariable Long storeId, @PathVariable Long bottleId) {
		log.info("Adding store {} to bottle {}", storeId, bottleId);
		return bottleService.saveStoreToBottle(storeId, bottleId);
	}

	/**
	 * GET all distilleries to make bottle POST easier for user to find distillerId.
	 * 
	 * @return list of Distiller information
	 */
	@GetMapping("/distillers")
	public List<BourbonDistiller> listAllDistillers() {
		log.info("Listing all distilleries");
		return bottleService.retrieveAllDistillers();
	}

	/**
	 * GET distillery by ID
	 * 
	 * @param distillerId - PathVariable
	 * @return single Distiller information
	 */
	@GetMapping("/distiller/{distillerId}")
	public BourbonDistiller findDistillerById(@PathVariable Long distillerId) {
		log.info("Retrieving distiller with ID={}", distillerId);
		return bottleService.retrieveDistillerById(distillerId);
	}

	/**
	 * GET all bottles
	 * 
	 * @return list of Bottle information
	 */
	@GetMapping("/bottles")
	public List<BottleData> listAllBottlesInCollection() {
		log.info("Listing all bottles in the collection");
		return bottleService.retrieveAllBottles();
	}

	/**
	 * GET Bottle by ID
	 * 
	 * @param bottleId - PathVariable
	 * @return Bottle information
	 */
	@GetMapping("/bottle/{bottleId}")
	public BottleData findBottleById(@PathVariable Long bottleId) {
		log.info("Retrieving bottle with ID={}", bottleId);
		return bottleService.retrieveBottleById(bottleId);
	}

	/**
	 * GET Store by Bottle ID (Where did I buy that?)
	 * 
	 * @param bottleId - PathVariable
	 * @return Store information for a single Bottle
	 */
	@GetMapping("/store/bottle/{bottleId}")
	public Set<BourbonStore> findStoreByBottleId(@PathVariable Long bottleId) {
		log.info("Retrieving store for bottle with ID={}", bottleId);
		return bottleService.retrieveStoreByBottleId(bottleId);
	}

	/**
	 * PUT for bottle
	 * 
	 * @param distillerId - PathVariable
	 * @param bottleId    - PathVariable
	 * @param bottleData  - RequestBody
	 * @return updated Bottle information
	 */
	@PutMapping("{distillerId}/bottle/{bottleId}")
	public BottleData updateBottle(@PathVariable Long distillerId, @PathVariable Long bottleId,
			@RequestBody BottleData bottleData) {
		bottleData.setBottleId(bottleId);
		log.info("Updating bottle {} from distiller {}", bottleId, distillerId);
		return bottleService.saveBottle(bottleData, distillerId);
	}

	/**
	 * PUT for distillery
	 * 
	 * @param distillerId      - PathVariable
	 * @param bourbonDistiller - RequestBody
	 * @return updated Distiller information
	 */
	@PutMapping("/distiller/{distillerId}")
	public BourbonDistiller updateDistillery(@PathVariable Long distillerId,
			@RequestBody BourbonDistiller bourbonDistiller) {
		bourbonDistiller.setDistillerId(distillerId);
		log.info("Updating distiller {}", distillerId);
		return bottleService.saveDistiller(bourbonDistiller);
	}

	/**
	 * PUT for store
	 * 
	 * @param storeId      - PathVariable
	 * @param bourbonStore - RequestBody
	 * @return updated Store information
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
	 * @param bottleId - PathVariable
	 * @return message confirming deletion of bottle with provided ID
	 */
	@DeleteMapping("/bottle/{bottleId}")
	public Map<String, String> deleteBottleById(@PathVariable Long bottleId) {
		log.info("Deleting bottle with ID={}", bottleId);
		bottleService.deleteBottleById(bottleId);

		return Map.of("message", "Deletion of bottle with ID=" + bottleId + " was successful.");
	}

	/**
	 * DELETE distiller by ID
	 * 
	 * @param distillerId - PathVariable
	 * @return message confirming deletion of distiller with provided ID
	 */
	@DeleteMapping("/distiller/{distillerId}")
	public Map<String, String> deleteDistillerById(@PathVariable Long distillerId) {
		log.info("Deleting distiller with ID={}", distillerId);
		bottleService.deleteDistillerById(distillerId);

		return Map.of("message", "Deletion of distiller with ID=" + distillerId + " was successful.");
	}

	/**
	 * DELETE store by ID
	 * 
	 * @param storeId - PathVariable
	 * @return message confirming deletion of store with provided ID
	 */
	@DeleteMapping("/store/{storeId}")
	public Map<String, String> deleteStoreById(@PathVariable Long storeId) {
		log.info("Deleting store with ID={}", storeId);
		bottleService.deleteStoreById(storeId);

		return Map.of("message", "Deletion of store with ID=" + storeId + " was successful.");
	}
}
