package bourbon.collection.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Bottle entity
 * 
 * @author clayr
 *
 */
@Entity
@Data
public class Bottle {

	/**
	 * Bottle entity fields
	 */
	// bottle_id is the Primary Key for the bottle table
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bottleId;

	private String name;
	private double proof;
	private String label;
	private double price;

	/**
	 * Bottle to Distiller is a Many-to-One relationship. Many bottles to one
	 * distiller. Connected via distiller_id. Excluded methods to prevent recursion.
	 */
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToOne
	@JoinColumn(name = "distiller_id")
	private Distiller distiller;

	/**
	 * Bottle to Store is a Many-to_many relationship. Many bottles to many stores.
	 * Connected via bottle_id and store_id in a JOIN table Excluded methods to
	 * prevent recursion.
	 */
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name = "bottle_store", joinColumns = @JoinColumn(name = "bottle_id"), inverseJoinColumns = @JoinColumn(name = "store_id"))
	private Set<Store> stores = new HashSet<>();
}
