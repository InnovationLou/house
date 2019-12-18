package xyz.nadev.house.controller;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.nadev.house.entity.House;
import xyz.nadev.house.service.HouseService;
import xyz.nadev.house.util.ControllerUtil;
import xyz.nadev.house.vo.ResponseVO;

@RestController
@RequestMapping("/house")
public class HouseController {

    public static final Logger logger= LoggerFactory.getLogger(HouseController.class);

    @Autowired
    HouseService houseService;

    @ApiOperation("通过id查house")
    @GetMapping("/getHouseById/{id}")
    public ResponseVO getHouseById(@PathVariable Integer id){
        return houseService.getHouseById(id);
    }

    @ApiOperation("查看所有房源信息")
    @GetMapping("/getAllHouses")
    public ResponseVO getAllHouses(){
        return houseService.houseList();
    }

    @ApiOperation("新增房源信息")
    @PostMapping("/newHouse")
    public ResponseVO AddHouse(House house){
        return houseService.addHouse(house);
    }

    @ApiOperation("修改房源信息")
    @PostMapping("/modifyHouse")
    public ResponseVO modifyHouse(House house){
        return houseService.modifyHouse(house);
    }
}
