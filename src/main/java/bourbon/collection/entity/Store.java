package bourbon.collection.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Store entity
 * 
 * @author clayr
 *
 */
@Entity
@Data
public class Store {
	/**
	 * Fields for Store entity
	 */
	// store_id is Primary Key for store table
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long storeId;

	private String storeName;
	private String streetAddress;
	private String city;
	private String state;
	private String zip;

	/**
	 * Store relationship to Bottle is a Many-to-Many relationship. Excluded methods
	 * to prevent recursion.
	 */
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToMany(mappedBy = "stores")
	private Set<Bottle> bottles = new HashSet<>();
}
