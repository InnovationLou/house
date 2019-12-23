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
    public ResponseVO getHouse(House house, Integer distance, Integer latest, Integer page) {
        return houseService.findByCondition(house, distance, latest, page);
    }

    @ApiOperation("通过id查house")
    @GetMapping("/{id}")
    public ResponseVO getHouseById(@PathVariable Integer id) {
        return houseService.getHouseById(id);
    }

    @ApiOperation("新增房源信息")
    @PutMapping("")
    public ResponseVO AddHouse(House house) {
        return houseService.addHouse(house);
    }

    @ApiOperation("修改房源信息")
    @PostMapping("")
    public ResponseVO modifyHouse(House house) {
        return houseService.modifyHouse(house);
    }
}
