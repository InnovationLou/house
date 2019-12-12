package xyz.nadev.house.repository;

import xyz.nadev.house.entity.Managerinfo;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ManagerinfoRepository extends JpaRepository<Managerinfo, Integer>{

}
