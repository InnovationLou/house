package xyz.nadev.house.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.models.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.nadev.house.entity.Banner;
import xyz.nadev.house.service.BannerService;
import xyz.nadev.house.vo.ResponseVO;

@RestController
@RequestMapping("/banner")
public class BannerController {
    public static final Logger logger= LoggerFactory.getLogger(BannerController.class);

    @Autowired
    BannerService bannerService;

    @ApiOperation("获取所有banner")
    @GetMapping("")
    public ResponseVO getAllBanner(){
        return bannerService.getAllBanner();
    }

    @ApiOperation("根据前端传的url返回banner")
    @GetMapping("/url/{url}")
    public ResponseVO getBannerByUrl(@PathVariable String url){
        return bannerService.getBannerByUrl(url);
    }

    @ApiOperation("上传banner")
    @PostMapping("")
    public ResponseVO uploadBanner(Banner banner){
        return bannerService.uploadBanner(banner);
    }
}
