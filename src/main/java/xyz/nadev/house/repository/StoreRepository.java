package xyz.nadev.house.repository;

import org.springframework.data.jpa.repository.Query;
import xyz.nadev.house.entity.Store;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {

    List<Store> findByType(String type);
}
