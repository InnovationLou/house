package xyz.nadev.house.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.nadev.house.entity.Recommend;

@Repository
public interface RecommedRepository extends JpaRepository<Recommend,Integer> {
}
