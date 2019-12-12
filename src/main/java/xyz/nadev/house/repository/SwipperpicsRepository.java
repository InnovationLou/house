package xyz.nadev.house.repository;

import xyz.nadev.house.entity.Swipperpics;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface SwipperpicsRepository extends JpaRepository<Swipperpics, Integer>{

}
