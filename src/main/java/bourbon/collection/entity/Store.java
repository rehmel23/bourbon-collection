package bourbon.collection.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Entity
@Data
public class Store {

	private Long storeId;
	
	private String storeName;
	private String streetAddress;
	private String city;
	private String state;
	private Long zip;
	
	@ManyToMany(mappedBy = "bottles", cascade = CascadeType.ALL)
	private Set<Bottle> bottles = new HashSet<>();
}
