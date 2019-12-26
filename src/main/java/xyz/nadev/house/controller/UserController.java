package xyz.nadev.house.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.nadev.house.entity.House;
import xyz.nadev.house.entity.HouseRepairImg;
import xyz.nadev.house.entity.User;
import xyz.nadev.house.service.HouseRepairService;
import xyz.nadev.house.service.HouseService;
import xyz.nadev.house.service.UserService;
import xyz.nadev.house.service.WxPayService;
import xyz.nadev.house.vo.ResponseVO;

import javax.servlet.http.HttpServletRequest;

@Slf4j

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    HouseService houseService;

    @Autowired
    HouseRepairService repairService;

    @Autowired
    WxPayService wxPayService;


    @ApiOperation(value = "检查token是否过期")
    @GetMapping("/token/{token}")
    public ResponseVO checkToken(@PathVariable String token) {
        return userService.checkToken(token);
    }

    @ApiOperation("登录")
    @PostMapping("/login")
    public ResponseVO wxLogin(@RequestParam String code) {
        return userService.login(code);
    }

    @ApiOperation("修改用户信息")
    @PostMapping("/info")
    public ResponseVO updateUserInfo(@RequestHeader("Authorization") String token, User user) {
        return userService.updateUser(token, user);
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/info")
    public ResponseVO selectInfo(@RequestHeader("Authorization") String token) {
        return userService.getUserInfo(token);
    }

    @ApiOperation("注册用户")
    @PostMapping("/register")
    public ResponseVO register(User user, String code) {
        return userService.register(user, code);
    }

    @ApiOperation("用户发起提现请求")
    @PostMapping("/launchWithdraw")
    public ResponseVO register(@RequestHeader("Authorization")String token,String sign, Double money, HttpServletRequest request) throws Exception {
        //人工打款，由用户自己输入自己微信号
        return userService.launchWithdraw(token,request);
    }

    @ApiOperation("用户发起退款请求")
    @PostMapping("/refund/{outTradeNo}")
    public ResponseVO refundToUser(@RequestHeader("Authorization")String token,String sign,String outTradeNo,HttpServletRequest request) throws Exception {
        return wxPayService.doRefund(token,request);
    }

    @ApiOperation("用户添加收藏房源信息")
    @PostMapping("/star/house/{houseId}")
    public ResponseVO collectHouse(@RequestHeader("Authorization")String token,Integer houseId){
        return userService.addUserColleection(token,houseId);
    }
    @ApiOperation("获取用户收藏房源")
    @GetMapping("/star/house")
    public ResponseVO collectedHouses(@RequestHeader("Authorization")String token) {
        return houseService.getCollectedHouses(token);
    }


    @ApiOperation("用户查看自己的浏览历史信息")
    @GetMapping("/browse")
    public ResponseVO collections(@RequestHeader("Authorization")String token,Integer limit, Integer start){
        return userService.getUserBrowse(token,limit,start);
    }

    @ApiOperation("用户查看自己账单信息")
    @GetMapping("/bill")
    public ResponseVO getUserBill(@RequestHeader("Authorization")String token){
        return userService.getUserBill(token) ;
    }

    @ApiOperation("")
    @GetMapping("/house")
    public ResponseVO getUserHouse(){
        return null;
    }


    @ApiOperation("房东房源列表")
    @GetMapping("/house")
    public ResponseVO rentHouseList(@RequestHeader("Authorization")String token,Boolean isLandlord){
        return houseService.rentHouseList(isLandlord,token);
    }

    @ApiOperation("获取所有报修")
    @GetMapping("/repair")
    public ResponseVO getRepairList(@RequestHeader("Authorization")String token){
        return repairService.getRepairListByUserToken(token);
    }

    @ApiOperation("上传报修")
    @PutMapping("/repair")
    public ResponseVO uploadRepair(@RequestHeader("Authorization")String token, House house, String phone, String content,
                                   HouseRepairImg img){
        return repairService.uploadRepair(token, house, phone, content,img);
    }
}
