package xyz.nadev.house.repository;

import xyz.nadev.house.entity.Collection;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long>{
    List<Collection> findCollectionsByUserId(Integer userId);
}
