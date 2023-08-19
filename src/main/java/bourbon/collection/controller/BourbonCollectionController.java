package bourbon.collection.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import bourbon.collection.controller.model.BottleData;
import bourbon.collection.service.BottleService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/bourbon_collection")
@Slf4j
public class BourbonCollectionController {
	
	@Autowired
	private BottleService bottleService;

	@PostMapping("/bottle")
	@ResponseStatus(code = HttpStatus.CREATED)
	public BottleData insertBottle(BottleData bottleData) {
		log.info("Adding bottle {} to collection", bottleData);
		return bottleService.saveBottle(bottleData);
	}
}
