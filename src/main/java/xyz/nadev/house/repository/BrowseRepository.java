package xyz.nadev.house.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xyz.nadev.house.entity.Browse;

import java.util.List;

@Repository
public interface BrowseRepository extends JpaRepository<Browse, Long>{
    List<Browse> findBrowseByUserId(Integer userId);

    @Query(value = "select * from browse where browse.user_id=?1  order by browse.gmt_create desc" ,
            countQuery = "select count(*) from browse where browse.user_id=?1",
            nativeQuery = true)
    List<Browse> findBrowseByUserId(Integer userId, Pageable pageable);

    Browse findByUserIdAndHouseId(Integer userId,Integer houseId);


}
