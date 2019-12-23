package xyz.nadev.house.repository;

import xyz.nadev.house.entity.HouseRepairImg;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface HouseRepairImgRepository extends JpaRepository<HouseRepairImg, Long>{

}
