package xyz.nadev.house.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.nadev.house.entity.Bill;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill,Integer> {
    List<Bill> findByUserId(Integer userId);
    List<Bill> findByHouseId(Integer houseId);
}
