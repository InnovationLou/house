package xyz.nadev.house.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.nadev.house.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByOpenId(String openId);
    User findById(int userId);
}
