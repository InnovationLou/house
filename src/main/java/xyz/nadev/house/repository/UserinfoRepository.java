package xyz.nadev.house.repository;

import xyz.nadev.house.entity.Userinfo;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserinfoRepository extends JpaRepository<Userinfo, Integer>{

}
