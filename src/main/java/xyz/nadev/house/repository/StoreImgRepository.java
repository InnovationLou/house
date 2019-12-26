package xyz.nadev.house.repository;

import xyz.nadev.house.entity.StoreImg;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface StoreImgRepository extends JpaRepository<StoreImg, Long>{

    List<StoreImg> findByStoreId(Integer storeId);
}
