package xyz.nadev.house.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.nadev.house.entity.FavorStore;

import java.util.List;

@Repository
public interface FavorStoreRepository extends JpaRepository<FavorStore,Integer> {
    List<FavorStore> findAllByUserId(Integer userId);
    FavorStore findByUserIdAndStoreId(Integer userId, Integer storeId);
}
