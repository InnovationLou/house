package xyz.nadev.house.repository;

import xyz.nadev.house.entity.House;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface HouseRepository extends JpaRepository<House, Integer>{

}
