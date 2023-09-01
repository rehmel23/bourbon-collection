package bourbon.collection.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Distiller entity
 * 
 * @author clayr
 *
 */
@Entity
@Data
public class Distiller {

	/**
	 * Distiller entity fields
	 */
	// distller_id is Primary Key for the distiller table
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long distillerId;

	private String distillerName;
	private String city;
	private String state;
	private String zip;

	/**
	 * Distiller relationship to Bottle is One-to-Many. Methods excluded to prevent
	 * recursion.
	 */
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(mappedBy = "distiller", cascade = CascadeType.ALL, orphanRemoval = false)
	private Set<Bottle> bottles = new HashSet<>();
}
