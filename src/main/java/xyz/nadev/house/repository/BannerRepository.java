package xyz.nadev.house.repository;

import xyz.nadev.house.entity.Banner;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Integer>{
    Banner findBannerByImgUrl(String url);
}
