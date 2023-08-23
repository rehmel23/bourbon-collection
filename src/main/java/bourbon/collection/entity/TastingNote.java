//package bourbon.collection.entity;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.OneToOne;
//import lombok.Data;
//
//@Entity
//@Data
//public class TastingNote {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long tastingNoteId;
//	
//	private String taste;
//	private String nose;
//	private String color;
//	private String heat;
//	
//	@OneToOne(optional = true)
//	private Bottle bottle;
//}
