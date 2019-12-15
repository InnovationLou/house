package xyz.nadev.house.repository;

import xyz.nadev.house.entity.Browse;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface BrowseRepository extends JpaRepository<Browse, Long>{

}
