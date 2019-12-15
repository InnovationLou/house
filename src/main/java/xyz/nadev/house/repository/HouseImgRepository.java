package xyz.nadev.house.repository;

import xyz.nadev.house.entity.HouseImg;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface HouseImgRepository extends JpaRepository<HouseImg, Long>{

}
