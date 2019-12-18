package xyz.nadev.house.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.nadev.house.entity.PayOwner;

@Repository
public interface PayOwnerRepository extends JpaRepository<PayOwner,Long> {
    PayOwner findByOpenId(String openId);
}
