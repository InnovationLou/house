package xyz.nadev.house.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.nadev.house.entity.RefundUser;

@Repository
public interface RefundUserRepository extends JpaRepository<RefundUser, Integer>{

}
