package xyz.nadev.house.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.nadev.house.vo.ResponseVO;
import xyz.nadev.house.entity.House;
import xyz.nadev.house.service.HouseService;

@Slf4j
@RestController
@RequestMapping("/house")
public class HouseController {

    @Autowired
    HouseService houseService;

    @ApiOperation("条件筛选房屋")
    @GetMapping("")
    public ResponseVO getHouse(House house, Integer distance, Integer latest, Integer price, Integer page) {
        return houseService.findByCondition(house, distance, latest, price, page);
    }

    @ApiOperation("通过id查house")
    @GetMapping("/{id}")
    public ResponseVO getHouseById(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        return houseService.getHouseById(token, id);
    }

    @ApiOperation("新增房源信息")
    @PostMapping("")
    public ResponseVO AddHouse(House house) {
        return houseService.addHouse(house);
    }

    @ApiOperation("修改房源信息")
    @PutMapping("")
    public ResponseVO modifyHouse(House house) {
        return houseService.modifyHouse(house);
    }


    @ApiOperation("获取用户浏览过的房源")
    @GetMapping("/browse")
    public ResponseVO browsedHouses(Integer userId) {
        return houseService.getBrowsedHouses(userId);
    }

    @ApiOperation("当前租了哪些房")
    @GetMapping("/user")
    public ResponseVO userRelatedHouse(@RequestHeader("Authorization")String token){return houseService.getRelatedHouse(token);}
}
