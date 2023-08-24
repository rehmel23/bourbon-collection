package bourbon.collection.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import bourbon.collection.controller.model.BottleData;
import bourbon.collection.controller.model.BottleData.BourbonDistiller;
import bourbon.collection.service.BottleService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/bourbon_collection")
@Slf4j
public class BourbonCollectionController {

	@Autowired
	private BottleService bottleService;

	// Infinite Loop on return, PUTs into DB correctly.
	@PostMapping("/{distillerId}/bottle")
	@ResponseStatus(code = HttpStatus.CREATED)
	public BottleData insertBottle(@PathVariable Long distillerId, @RequestBody BottleData bottleData) {
		log.info("Adding bottle {} to collection made by distiller {}.", bottleData, distillerId);
		return bottleService.saveBottle(bottleData, distillerId);
	}

	/**
	 * PUT new distillery in database
	 * 
	 * @param bourbonDistiller
	 * @return Distiller information
	 */
	@PostMapping("/distiller")
	@ResponseStatus(code = HttpStatus.CREATED)
	public BourbonDistiller addDistiller(@RequestBody BourbonDistiller bourbonDistiller) {
		log.info("Adding distiller {}.", bourbonDistiller.getDistillerName());
		return bottleService.saveDistiller(bourbonDistiller);
	}

	/**
	 * GET all distilleries to make bottle POST easier for user to find distillerId.
	 */
	@GetMapping("/distillers")
	public List<BourbonDistiller> listAllDistillers(){
		log.info("Listing all distilleries.");
		return bottleService.retrieveAllDistillers();
	}
	
	@GetMapping("/bottles")
	public List<BottleData> listAllBottlesInCollection(){
		log.info("Listing all bottles in the collection.");
		return bottleService.retrieveAllBottles();
	}
	
	/**
	 * DELETE single bottle by ID
	 * @param bottleId
	 * @return
	 */
	@DeleteMapping("/bottle/{bottleId}")
	public Map<String, String> deleteBottleById(@PathVariable Long bottleId){
		log.info("Deleting bottle with ID={}", bottleId);
		bottleService.deleteBottleById(bottleId);
		
		return Map.of("message", "Deletion of bottle with ID=" + bottleId + " was successful.");
	}

}
