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

@Entity
@Data
public class Distiller {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long distillerId;
	
	private String distillerName;
	private String city;
	private String state;
	private String zip;
	
	@OneToMany(mappedBy = "distiller", cascade = CascadeType.ALL)
	private Set<Bottle> bottles = new HashSet<>();
}
