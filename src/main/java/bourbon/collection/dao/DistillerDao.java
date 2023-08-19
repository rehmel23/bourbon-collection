package bourbon.collection.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import bourbon.collection.entity.Distiller;

public interface DistillerDao extends JpaRepository<Distiller, Long> {

}
