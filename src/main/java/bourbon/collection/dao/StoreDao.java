package bourbon.collection.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import bourbon.collection.entity.Store;

public interface StoreDao extends JpaRepository<Store, Long> {

}
