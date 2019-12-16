package xyz.nadev.house.repository;

import xyz.nadev.house.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByOpenId(String openId);
}
