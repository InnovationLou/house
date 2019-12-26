package xyz.nadev.house.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xyz.nadev.house.entity.Browse;

import java.util.List;

@Repository
public interface BrowseRepository extends JpaRepository<Browse, Long>{
    List<Browse> findBrowsesByUserId(Integer userId);
//    List<Browse> findAllByUserId(Integer userId, Pageable pageable);
    @Query(value = "select id, user_id, house_id, gmt_create from browse b where b.user_id=?1 having id>=?3 order by id desc limit ?2 " ,nativeQuery = true)
    List<Browse> getBrowseByUserId(Integer userId,Integer limit,Integer start);

}
