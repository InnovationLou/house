package xyz.nadev.house.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.nadev.house.entity.Banner;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BannerServiceTest {

    @Autowired
    BannerService bannerService;

    @Test
    void getAllBanner() {
        System.out.println(bannerService.getAllBanner());
    }
    @Test
    void getBannerByUrl(){
        String url="exampleBucket.com/xxx111.jpg";
        System.out.println(bannerService.getBannerByUrl(url));
    }
    @Test
    void uploadBanner(){
        Banner banner=new Banner();
        banner.setImgInfo("123");
        banner.setImgUrl("qwer.jpg");
        System.out.println(bannerService.uploadBanner(banner));
    }
}