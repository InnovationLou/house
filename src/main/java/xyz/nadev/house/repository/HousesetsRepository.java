package xyz.nadev.house.repository;

import xyz.nadev.house.entity.Housesets;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface HousesetsRepository extends JpaRepository<Housesets, Integer>{

}
