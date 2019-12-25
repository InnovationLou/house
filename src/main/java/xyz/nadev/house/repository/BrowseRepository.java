package xyz.nadev.house.repository;

import xyz.nadev.house.entity.Browse;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface BrowseRepository extends JpaRepository<Browse, Long>{
    List<Browse> findBrowsesByUserId(Integer userId);
}
