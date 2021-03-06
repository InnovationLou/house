package xyz.nadev.house.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.models.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.nadev.house.entity.HouseRepairImg;
import xyz.nadev.house.entity.User;
import xyz.nadev.house.service.*;
import xyz.nadev.house.util.ControllerUtil;
import xyz.nadev.house.util.DateUtil;
import xyz.nadev.house.vo.ResponseVO;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

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
    MessageService messageService;

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
    public ResponseVO register(@RequestHeader("Authorization") String token, String sign, Double money, String wxId,HttpServletRequest request) throws Exception {
        //人工打款，由用户自己输入自己微信号
        return userService.launchWithdraw(token, request);
    }

    @ApiOperation("用户发起退款请求")
    @PostMapping("/refund/{outTradeNo}")
    public ResponseVO refundToUser(@RequestHeader("Authorization") String token, String sign, @PathVariable String outTradeNo, HttpServletRequest request) throws Exception {
        return wxPayService.doRefund(token, request,outTradeNo);
    }

    @ApiOperation("用户添加收藏房源信息")
    @PostMapping("/star/house/{houseId}")
    public ResponseVO collectHouse(@RequestHeader("Authorization") String token,Integer houseId) {
        return userService.addUserColleection(token, houseId);
    }

    @ApiOperation("获取用户收藏房源")
    @GetMapping("/star/house")
    public ResponseVO collectedHouses(@RequestHeader("Authorization") String token) {
        return houseService.getCollectedHouses(token);
    }

    @ApiOperation("用户查看自己的浏览历史信息")
    @GetMapping("/browse")
    public ResponseVO collections(@RequestHeader("Authorization") String token, Integer page) {
        return userService.findUserBrowse(token, page);
    }

    @ApiOperation("用户查看自己账单信息")
    @GetMapping("/bill")
    public ResponseVO getUserBill(@RequestHeader("Authorization") String token) {
        return userService.getUserBill(token);
    }

    @ApiOperation("用户查看自己签约信息")
    @GetMapping("/sign")
    public ResponseVO getSignInfo(@RequestHeader("Authorization") String token) {
        return userService.getSignInfo(token);
    }


    @ApiOperation("房东房源列表")
    @GetMapping("/landload/house")
    public ResponseVO rentHouseList(@RequestHeader("Authorization") String token) {
        return houseService.rentHouseList(token);
    }

    @ApiOperation("认证房东")
    @PostMapping("/landlord/certify")
    public ResponseVO certifyLandlord(@RequestHeader("Authorization") String token,String authImgUrl){
        return userService.certifyLandlord(token,authImgUrl);
    }

    @ApiOperation("用户认证")
    @PostMapping("/certify")
    public ResponseVO certifyUser(@RequestHeader("Authorization") String token
            ,@RequestParam  String idcardFront
            ,@RequestParam String idcardBack){
        return userService.certifyUser(token,idcardFront, idcardBack);
    }

    @ApiOperation("获取所有报修")
    @GetMapping("/repair")
    public ResponseVO getRepairList(@RequestHeader("Authorization") String token) {
        return repairService.getRepairListByUserToken(token);
    }

    @ApiOperation("上传报修")
    @PutMapping("/repair")
    public ResponseVO uploadRepair(@RequestHeader("Authorization") String token, Integer houseId, String phone, String repairTime, String content,
                                   HouseRepairImg img) {
        Date date = DateUtil.getDateTimeByStr(repairTime);
        if (null == date){
            return ControllerUtil.getFalseResultMsgBySelf("时间格式错误yyyy-MM-dd HH:mm:ss-->" + repairTime);
        }
        return repairService.uploadRepair(token, houseId, phone, date, content, img);
    }

    @ApiOperation("获得一段时间内的系统通知")
    @GetMapping("/notifier/{days}")
    public ResponseVO getNotifiesInMonth(@PathVariable Integer days) {
        return messageService.getNotifies(days);
    }

    @ApiOperation("获取用户收藏的商店及信息")
    @GetMapping("/star/store")
    public ResponseVO getUserStarStore(@RequestHeader("Authorization") String token) {
        return userService.getUserStarStore(token);
    }

    @ApiOperation("用户添加商店收藏")
    @PostMapping("/star/store/{storeId}")
    public ResponseVO addUserStarStore(@RequestHeader("Authorization") String token, @PathVariable Integer storeId) {
        return userService.addUserStarStore(token, storeId);
    }

    @ApiOperation("取消收藏房屋")
    @DeleteMapping("/star/house/{houseId}")
    public ResponseVO cancelUserStarHouse(@RequestHeader("Authorization") String token, @PathVariable Integer houseId) {
        return userService.cancelUserStarHouse(token, houseId);
    }

    @ApiOperation("取消收藏商店")
    @DeleteMapping("/star/store/{storeId}")
    public ResponseVO cancelUserStarStore(@RequestHeader("Authorization") String token, @PathVariable Integer storeId) {
        return userService.cancelUserStarStore(token, storeId);
    }

    @ApiOperation("获取特色推荐")
    @GetMapping("/rcmd")
    public ResponseVO getRecommendList(){
        return userService.recommendList();
    }

    @ApiOperation("获得用户已提现金额")
    @GetMapping("/allGetMoney")
    public ResponseVO getUserAllGetMoney(@RequestHeader("Authorization") String token){
        return userService.getUserAllGetMoney(token);
    }
    @ApiOperation("用户和房东签约")
    @PostMapping("/{houseId}/sign")
    public ResponseVO postSignInfo(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer houseId,
            String handWriteImgUrl,
            String contractImgUrl,
            String userName,
            String idCardNum,
            Date startDate,
            Date endDate){
        return userService.postSignInfo(token,houseId,handWriteImgUrl,contractImgUrl, userName,idCardNum,startDate, endDate);
    }
    @ApiOperation("获取用户组了的房的列表")
    @PostMapping("/myrented")
    public ResponseVO getMyRentedHouse( @RequestHeader("Authorization") String token){
        return userService.getMyRentedHouse(token);
    }
}
