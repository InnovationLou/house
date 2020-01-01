package xyz.nadev.house.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import xyz.nadev.house.entity.User;
import xyz.nadev.house.service.UserService;
import xyz.nadev.house.util.ControllerUtil;
import xyz.nadev.house.vo.ResponseVO;
import xyz.nadev.house.entity.House;
import xyz.nadev.house.service.HouseService;

import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/house")
public class HouseController {

    @Value("${contract}")
    private String CONTRACT = "https://image.ruankun.xyz/2.jpg";

    @Autowired
    UserService userService;

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
    public ResponseVO AddHouse(@RequestHeader("Authorization") String token, House house) {
        User user = userService.findByToken(token);
        if (null == user){
            return ControllerUtil.getFalseResultMsgBySelf("没有找到用户信息");
        }
        house = setNoneBusinessFields(user.getId(), house);
        return houseService.addHouse(house);
    }

    @ApiOperation("修改房源信息")
    @PutMapping("")
    public ResponseVO modifyHouse(House house) {
        return houseService.modifyHouse(house);
    }


    @ApiOperation("当前租了哪些房")
    @GetMapping("/user")
    public ResponseVO userRelatedHouse(@RequestHeader("Authorization")String token){return houseService.getRelatedHouse(token);}

    @ApiOperation("判断用户是否收藏了该房源信息")
    @GetMapping("/{houseId}/isfavor")
    public ResponseVO houseIsFavor(@RequestHeader("Authorization") String token, @PathVariable Integer houseId) {
        return houseService.houseIsFavor(token, houseId);
    }

    @ApiOperation("获取合同图片")
    @GetMapping("/contract")
    public ResponseVO contract() {
        return ControllerUtil.getSuccessResultBySelf(getContractImg());
    }



    private String getContractImg(){
        return CONTRACT;
    }

    // ---------- private methods ------------- //
    private House setNoneBusinessFields(Integer userId, House house){
        house.setUserId(userId);
        house.setGmtCreate(new Date());
        house.setReleased(1);
        house.setId(null);
        house.setGmtModify(new Date());
        return house;
    }

    @ApiOperation(value = "房东查看自己租客账单信息")
    @GetMapping("/roomer")
    public ResponseVO getRoomerBill(@RequestHeader("Authorization") String token){
        return houseService.getRoomerBill(token);
    }
}
