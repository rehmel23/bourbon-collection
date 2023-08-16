package bourbon.collection.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;

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
	
	@OneToOne(optional = true, mappedBy = "bottle", cascade = CascadeType.ALL)
	private TastingNote tastingNote;
	
	@ManyToOne
	private Distiller distiller;
	
	@ManyToMany(mappedBy = "bottles")
	private Set<Store> stores = new HashSet<>();
}
