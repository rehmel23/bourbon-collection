package bourbon.collection.controller.model;

import java.util.HashSet;
import java.util.Set;

import bourbon.collection.entity.Bottle;
import bourbon.collection.entity.Distiller;
import bourbon.collection.entity.Store;
import bourbon.collection.entity.TastingNote;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BourbonCollectionData {

	private Long bottleId;
	private String name;
	private double proof;
	private String label;
	private double price;
	private TastingNote tastingNote;
	private Distiller distiller;
	private Set<Store> stores = new HashSet<>();

	public BourbonCollectionData(Bottle bottle) {
		this.bottleId = bottle.getBottleId();
		this.name = bottle.getName();
		this.proof = bottle.getProof();
		this.label = bottle.getLabel();
		this.price = bottle.getPrice();
		this.tastingNote = bottle.getTastingNote();
		this.distiller = bottle.getDistiller();

		for (Store store : bottle.getStores()) {
			this.stores.add(new BourbonStore(store)); //wut
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
}
