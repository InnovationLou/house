package xyz.nadev.house.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.nadev.house.entity.HouseOrder;

@Repository
public interface HouseOrderRepository extends JpaRepository<HouseOrder,Long> {
    HouseOrder findByOutTradeNoAndPrepayId(String outTradeNo, String prepayId);
}
