package bourbon.collection.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import bourbon.collection.entity.Bottle;

public interface BottleDao extends JpaRepository<Bottle, Long> {

}
