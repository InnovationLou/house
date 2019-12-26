package xyz.nadev.house.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.nadev.house.entity.HouseSign;

import java.util.List;

@Repository
public interface HouseSignRepository extends JpaRepository<HouseSign,Integer> {
    List<HouseSign> findByUserId(Integer userId);
}
