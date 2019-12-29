package xyz.nadev.house.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.nadev.house.entity.Withdraw;

import java.util.List;

@Repository
public interface WithdrawRepository extends JpaRepository<Withdraw,Integer> {
    List<Withdraw> findByOpenId(String openId);
    Withdraw findByWithdrawMent(String withdrawMent);
}
