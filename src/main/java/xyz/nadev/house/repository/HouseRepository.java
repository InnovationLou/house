package xyz.nadev.house.repository;

import xyz.nadev.house.entity.House;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


@Repository
public interface HouseRepository extends JpaRepository<House, Integer>{
    List<House> findByUserId(Integer userId);
    List<House> findHousesByTenantId(Integer tenantId);
}
