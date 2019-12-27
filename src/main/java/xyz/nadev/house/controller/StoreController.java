package xyz.nadev.house.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.nadev.house.entity.Store;
import xyz.nadev.house.service.StoreImgService;
import xyz.nadev.house.service.StoreService;
import xyz.nadev.house.vo.ResponseVO;


@Slf4j
@RequestMapping("/store")
@RestController
public class StoreController {

    @Autowired
    StoreService storeService;

    @Autowired
    StoreImgService storeImgService;

    @ApiOperation("根据类型返回店铺数据列表 如 '餐饮美食'")
    @GetMapping("/list/{type}")
    public ResponseVO getStoreList(@PathVariable String type) {
        return storeService.findByType(type);
    }

    @ApiOperation("根据store id 返回单个商店数据")
    @GetMapping("/{id}")
    public ResponseVO getStore(@PathVariable Integer id) {
        return storeService.findById(id);
    }

    @ApiOperation("根据store id 返回店铺所有图片")
    @GetMapping("/img/{id}")
    public ResponseVO getImgs(@PathVariable Integer id) {
        return storeImgService.findById(id);
    }

    @ApiOperation("根据地址关键字返回商铺信息列表")
    @GetMapping("/search/{type}/{keyword}")
    public ResponseVO search(@PathVariable String type, @PathVariable String keyword) {
        return storeService.search(type, keyword);
    }
}
