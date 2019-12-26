package xyz.nadev.house.repository;

import xyz.nadev.house.entity.HouseRepair;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface HouseRepairRepository extends JpaRepository<HouseRepair, Long>{
    List<HouseRepair> findHouseRepairsByUserId(Integer userId);
}
