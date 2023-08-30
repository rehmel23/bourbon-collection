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

@Entity
@Data
public class Bottle {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bottleId;
	
	private String name;
	private double proof;
	private String label;
	private double price;
	
//	@OneToOne(optional = true, mappedBy = "bottle", cascade = CascadeType.ALL)
//	private TastingNote tastingNote;
	
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToOne
	@JoinColumn(name = "distiller_id")
	private Distiller distiller;
	
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name = "bottle_store", joinColumns = @JoinColumn(name = "bottle_id"), inverseJoinColumns = @JoinColumn(name = "store_id"))
	private Set<Store> stores = new HashSet<>();
}
