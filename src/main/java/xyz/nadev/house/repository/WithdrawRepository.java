package xyz.nadev.house.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.nadev.house.entity.Withdraw;

@Repository
public interface WithdrawRepository extends JpaRepository<Withdraw,Integer> {
    Withdraw findByOpenId(String openId);
    Withdraw findByWithdrawMent(String withdrawMent);
}
