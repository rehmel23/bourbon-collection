package bourbon.collection.controller.model;

import java.util.HashSet;
import java.util.Set;

import bourbon.collection.entity.Bottle;
import bourbon.collection.entity.Distiller;
import bourbon.collection.entity.Store;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BottleData {

	private Long bottleId;
	private String name;
	private double proof;
	private String label;
	private double price;
//	private TastingNote tastingNote;
	private Distiller distiller;
	private Set<BourbonStore> stores = new HashSet<>();

	public BottleData(Bottle bottle) {
		this.bottleId = bottle.getBottleId();
		this.name = bottle.getName();
		this.proof = bottle.getProof();
		this.label = bottle.getLabel();
		this.price = bottle.getPrice();
//		this.tastingNote = bottle.getTastingNote();
		this.distiller = bottle.getDistiller();

		for (Store store : bottle.getStores()) {
			this.stores.add(new BourbonStore(store));
		}
	}
	
	@Data
	@NoArgsConstructor
	public static class BourbonStore {
		private Long storeId;
		private String storeName;
		private String streetAddress;
		private String city;
		private String state;
		private Long zip;
		
		public BourbonStore(Store store) {
			this.storeId = store.getStoreId();
			this.storeName = store.getStoreName();
			this.streetAddress = store.getStreetAddress();
			this.city = store.getCity();
			this.state = store.getState();
			this.zip = store.getZip();
		}
		
	}
	/**
	 * Not sure if this inner class is needed
	 * @author clayr
	 *
	 */
	@Data
	@NoArgsConstructor
	public static class BourbonDistiller {
		private Long distillerId;
		private String distillerName;
		private String city;
		private String state;
		private String zip;
		
		public BourbonDistiller(Distiller distiller) {
			this.distillerId = distiller.getDistillerId();
			this.distillerName = distiller.getDistillerName();
			this.city = distiller.getCity();
			this.state = distiller.getState();
			this.zip = distiller.getZip();
		}
	}
}
