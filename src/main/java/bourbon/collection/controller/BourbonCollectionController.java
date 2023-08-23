package bourbon.collection.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

}
