package xyz.nadev.house.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import xyz.nadev.house.config.WxPayConfig;
import xyz.nadev.house.entity.Collection;
import xyz.nadev.house.entity.*;
import xyz.nadev.house.repository.*;
import xyz.nadev.house.service.UserService;
import xyz.nadev.house.util.ControllerUtil;
import xyz.nadev.house.util.EntityUtil;
import xyz.nadev.house.util.UUIDUtil;
import xyz.nadev.house.util.WePayUtil;
import xyz.nadev.house.util.constant.RespCode;
import xyz.nadev.house.vo.ResponseVO;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserServiceImpl implements UserService {


    // 设置key有效期,实现登录超时 单位分钟
    private static final int USER_LOGIN_TIMEOUT = 30;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Autowired
    BrowseRepository browseRepository;

    @Autowired
    WithdrawRepository withdrawRepository;

    @Autowired
    HouseOrderRepository houseOrderRepository;

    @Autowired
    RecommedRepository recommedRepository;

    @Autowired
    WxPayServiceImpl wxPayServiceImpl;

    @Autowired
    CollectionRepository collectionRepository;

    @Autowired
    HouseRepository houseRepository;

    @Autowired
    BillRepository billRepository;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    HouseSignRepository houseSignRepository;


    @Autowired
    FavorStoreRepository favorStoreRepository;

    @Autowired
    UserRepository resp;

    @Value("${wx.app.id}")
    String appId;

    @Value("${wx.app.secret}")
    String appSecret;

    @Override
    public List<User> findAll() {
        return resp.findAll();
    }

    /**
     * 根据Id查找用户
     *
     * @param id :
     * @return: xyz.nadev.house.entity.User
     */
    @Override
    public User findById(int id) {
        return resp.findById(id).get();
    }

    /**
     * 请求API获取用户openId
     *
     * @param code : 小程序js code
     * @return: java.lang.String
     */
    @Override
    public String getWxOpenId(String code) {
        String openId = null;
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="
                + appId + "&secret=" + appSecret
                + "&grant_type=authorization_code&js_code=" + code;
        HttpGet httpGet = new HttpGet(url);
        String responseTxt = null;
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpResponse response = httpClient.execute(httpGet);
            responseTxt = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            log.error(e.toString());
            return null;
        } finally {
            httpGet.abort();
        }

        // 取出openId
        if (responseTxt != null) {
            JSONObject jsonObject = JSONObject.parseObject(responseTxt);
            openId = jsonObject.getString("openid");
            if (openId == null) {
                log.error(responseTxt);
                return null;
            }
        }
        return openId;
    }

    /**
     * 根据openId查找用户
     *
     * @param openId :
     * @return: xyz.nadev.house.entity.User
     */
    @Override
    public User findByOpenId(String openId) {
        return resp.findByOpenId(openId);
    }

    /**
     * 拿token查当前登录用户
     *
     * @param token :
     * @return: xyz.nadev.house.entity.User
     */
    @Override
    public User findByToken(String token) {
        String openId = redisTemplate.opsForValue().get(token);
        if (openId == null) return null;
        return resp.findByOpenId(openId);
    }

    /**
     * 保存user
     *
     * @param user :
     * @return: boolean
     */
    @Override
    public ResponseVO saveUser(User user) {
        try {
            resp.save(user);
        } catch (Exception e) {
            return ControllerUtil.getFalseResultMsgBySelf("保存出错");
        }
        return ControllerUtil.getSuccessResultBySelf("保存成功");
    }

    /**
     * 更新用户信息
     *
     * @param token :
     * @param user  :
     * @return: xyz.nadev.house.vo.ResponseVO
     */
    @Override
    public ResponseVO updateUser(String token, User user) {
        String openId = redisTemplate.opsForValue().get(token);
        if (openId == null) return ControllerUtil.getFalseResultMsgBySelf(RespCode.MSG_WITHOUT_AUTH);
        User old = findByOpenId(openId);
        user.setOpenId(null);
        user.setId(null);
        user.setIsAuth(null);

        if (old.getIsAuth() != 0) {
            if (StringUtils.isNotBlank(user.getIdCardBackImg())
                    || StringUtils.isNotBlank(user.getIdCardFrontImg())) {
                return ControllerUtil.getFalseResultMsgBySelf("不能重复提交认证信息");
            }
        }


        EntityUtil.update(user, old);
        resp.save(user);
        return ControllerUtil.getSuccessResultBySelf("修改成功");
    }

    /**
     * 用户注册
     *
     * @param user :
     * @param code :
     * @return: xyz.nadev.house.vo.ResponseVO
     */
    @Override
    public ResponseVO register(User user, String code) {
        String openId = getWxOpenId(code);
        if (openId == null)
            return ControllerUtil.getFalseResultMsgBySelf("未获取到openId");
        if (findByOpenId(openId) != null) return ControllerUtil.getFalseResultMsgBySelf("用户已存在");
        user.setOpenId(openId);
        return saveUser(user);
    }

    /**
     * 用户登录 当前未判断已登录用户重复登录
     *
     * @param code :
     * @return: xyz.nadev.house.vo.ResponseVO
     */
    @Override
    public ResponseVO login(String code) {
        String openId = getWxOpenId(code);
        if (openId == null) return ControllerUtil.getFalseResultMsgBySelf("请求openId失败");
        User user = findByOpenId(openId);
        if (user == null) return ControllerUtil.getFalseResultMsgBySelf("用户未注册");
        String token = UUIDUtil.getUUID(); // 使用UUID作为token
        redisTemplate.opsForValue().set(token, openId, USER_LOGIN_TIMEOUT, TimeUnit.MINUTES);
        return ControllerUtil.getSuccessResultBySelf(token);
    }

    /**
     * 检查token是否过期
     *
     * @param token :
     * @return: xyz.nadev.house.vo.ResponseVO
     */
    @Override
    public ResponseVO checkToken(String token) {
        if (redisTemplate.hasKey(token)) return ControllerUtil.getSuccessResultBySelf("token可用");
        return ControllerUtil.getFalseResultMsgBySelf(RespCode.MSG_WITHOUT_AUTH);
    }

    /**
     * 返回用户信息
     *
     * @return: xyz.nadev.house.vo.ResponseVO
     */
    @Override
    public ResponseVO getUserInfo(String token) {
        return ControllerUtil.getSuccessResultBySelf(findByToken(token));
    }

    /**
     * 用于用户发起提现
     *
     * @param token
     * @param request
     * @return
     */
    @Override
    public ResponseVO launchWithdraw(String token, HttpServletRequest request) throws Exception {
        //用token拿到当前用户信息
        User user = findByToken(token);
        if (user == null) {
            log.info(RespCode.MSG_WITHOUT_AUTH);
            return null;
        }
        String openId = user.getOpenId();
        // 检查sign
        if (WePayUtil.checkSign(request, openId) == false) {
            log.info("sign不正确");
            return ControllerUtil.getFalseResultMsgBySelf("sign不正确");
        }
        //取出需要的参数

        Withdraw withdraw = new Withdraw();
        try {
            BigDecimal money = BigDecimal.valueOf(Double.valueOf(request.getParameter("money")));
            String wxId = request.getParameter("wxId");
            //price1.compareTo()price2
            //price1 大于price2返回1，price1 等于price2返回0，price1 小于price2返回-1
            if (money.compareTo(user.getMoney()) == 1) {
                return ControllerUtil.getFalseResultMsgBySelf("余额不足");
            }
            String withdrawMent = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
            System.out.println("生成的16位提现订单号：" + withdrawMent);
            BigDecimal afterWithdrawMoney = user.getMoney().subtract(money);
            System.out.println("余额-提现额=新金额" + user.getMoney() + "-" + money + "=" + afterWithdrawMoney);
            //更新用户余额
            user.setMoney(afterWithdrawMoney);
            //保存提现表信息
            withdraw.setGmtCreate(new Date());
            withdraw.setGmtModify(new Date());
            withdraw.setWxId(wxId);
            withdraw.setOpenId(openId);
            withdraw.setMoney(money);
            withdraw.setWithdrawMent(withdrawMent);
            withdraw.setWithdrawStatus(WxPayConfig.WITHDRAW_NO_ERROR);
            withdraw.setIsFinish(WxPayConfig.NOT_FINISHED);
            withdrawRepository.save(withdraw);
            resp.save(user);
        } catch (Exception e) {
            log.error("保存提现记录失败");
            return ControllerUtil.getFalseResultMsgBySelf("保存错误");
        }
        return ControllerUtil.getDataResult(withdraw);
    }

    @Override
    public ResponseVO addUserColleection(String token, Integer houseId) {
        try {
            //用token拿到当前用户信息（UserId）
            User user = findByToken(token);
            if (user == null) {
                log.info(RespCode.MSG_WITHOUT_AUTH);
                return ControllerUtil.getFalseResultMsgBySelf(RespCode.MSG_WITHOUT_AUTH);
            }
            Collection collection = collectionRepository.findByUserIdAndHouseId(user.getId(),houseId);
            if (collection != null) {
                return ControllerUtil.getFalseResultMsgBySelf("请勿重复收藏");
            }
            Integer userId = user.getId();
            collection = new Collection();
            collection.setUserId(userId);
            collection.setHouseId(houseId);
            collection.setGmtCreate(new Date());
            collectionRepository.save(collection);
            return ControllerUtil.getDataResult("收藏成功");
        } catch (Exception e) {
            log.info("添加收藏记录失败");
            return ControllerUtil.getFalseResultMsgBySelf("添加收藏记录失败");
        }

    }

    @Override
    public ResponseVO findUserBrowse(String token, Integer page) {
        User user = findByToken(token);
        if (user == null) {
            return ControllerUtil.getFalseResultMsgBySelf(RespCode.MSG_WITHOUT_AUTH);
        }
        List list = new ArrayList();
        Pageable pageable = PageRequest.of(--page, 10);
        List<Browse> browses = browseRepository.findBrowseByUserId(user.getId(), pageable);
        if (browses.isEmpty()) {
            return ControllerUtil.getFalseResultMsgBySelf("无游览信息");
        }
        for (Browse browse : browses) {
            Optional<House> house = houseRepository.findById(browse.getHouseId());
            if (!house.isPresent()) {
                continue;
            }
            list.add(house);
        }
        return ControllerUtil.getDataResult(list);

    }

    @Override
    public ResponseVO getUserBill(String token) {
        try {
            User user = findByToken(token);
            if (user == null) {
                log.info(RespCode.MSG_WITHOUT_AUTH);
                return ControllerUtil.getFalseResultMsgBySelf(RespCode.MSG_WITHOUT_AUTH);
            }
            List result = new ArrayList();
            List<Bill> bills = billRepository.findByUserId(user.getId());
            if (bills.isEmpty()) {
                return ControllerUtil.getFalseResultMsgBySelf("没有找到账单信息");
            }
            for (Bill bill : bills) {
                Map map = new HashMap();
                System.out.println("bill: " + bill.toString());
                Optional<House> house = houseRepository.findById(bill.getHouseId());
                if (!house.isPresent()) {
                    continue;
                }
                System.out.println(house.toString());
                map.put("userId", bill.getUserId());
                map.put("houseId", bill.getHouseId());
                map.put("houseInfo", house.get().getHouseInfo());
                map.put("cashType", house.get().getCashType());
                map.put("houseType", house.get().getHouseType());
                map.put("payItem", bill.getPayItem());
                map.put("money", bill.getMoney());
                map.put("gmtCreate", bill.getGmtCreate());
                map.put("isPaid", bill.getIsPaid());
                map.put("remark", bill.getRemark());
                map.put("dead_date", bill.getDeadDate());
                map.put("payDate", bill.getPayDate());
                map.put("payDetail1", bill.getPayDetail1());
                map.put("payDetail2", bill.getPayDetail2());
                map.put("payDetailFee1", bill.getPayDetailFee1());
                map.put("payDetailFee2", bill.getPayDetailFee2());
                map.put("waterUse", bill.getWaterUse());
                map.put("waterUntPrice", bill.getWaterUntPrice());
                map.put("eleUse", bill.getEleUse());
                map.put("eleUntPrice", bill.getEleUntPrice());

                result.add(map);
            }
            return ControllerUtil.getDataResult(result);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ResponseVO getSignInfo(String token) {
        User user = findByToken(token);
        if (user == null) {
            return ControllerUtil.getFalseResultMsgBySelf(RespCode.MSG_WITHOUT_AUTH);
        }
        //不为房东
        if (user.getLandlord() == 0) {
            List result = new ArrayList<>();
            List<HouseSign> houseSigns = houseSignRepository.findByUserId(user.getId());
            System.out.println("houseSign:" + houseSigns.toString());
            if (houseSigns.isEmpty() || houseSigns == null) {
                return ControllerUtil.getFalseResultMsgBySelf("没有该用户的签约信息");
            }
            for (HouseSign houseSign : houseSigns) {
                Optional<House> house = houseRepository.findById(houseSign.getHouseId());
                if (!house.isPresent()) {
                    continue;
                }
                Map<String, Object> map = new HashMap<>();
                map.put("userId",houseSign.getUserId());
                map.put("houseId",houseSign.getHouseId());
                map.put("cashType", house.get().getCash());
                map.put("houseInfo", house.get().getHouseInfo());
                map.put("houseType", house.get().getHouseType());
                map.put("startCreate", houseSign.getStartDate());
                map.put("endCreate", houseSign.getEndDate());
                map.put("expDate", houseSign.getExpDate());
                //履行状态：当前日期.compareTo(截止日期)  = 0, < -1, > 1
                if (new Date().compareTo(houseSign.getEndDate()) > 0 && houseSign.getIsFulfill() != 0) {
                    houseSign.setIsFulfill(WxPayConfig.HOUSE_NOT_FULFILL);
                    houseSignRepository.save(houseSign);
                }
                map.put("isFulFill", houseSign.getIsFulfill());
                result.add(map);
            }
            return ControllerUtil.getDataResult(result);
        }
        return ControllerUtil.getFalseResultMsgBySelf("房东无法获得信息");
    }

    @Override
    public ResponseVO getUserStarStore(String token) {
        User user = findByToken(token);
        if (user == null) {
            return ControllerUtil.getFalseResultMsgBySelf(RespCode.MSG_WITHOUT_AUTH);
        }
        List result = new ArrayList();
        List<FavorStore> favorStores = favorStoreRepository.findAllByUserId(user.getId());
        if (favorStores.isEmpty()) return ControllerUtil.getFalseResultMsgBySelf("用户收藏商店信息为空");
        for (FavorStore favorStore : favorStores) {
            Optional<Store> store = storeRepository.findById(favorStore.getStoreId());
            if (!store.isPresent()) continue;
            result.add(store);
        }
        return ControllerUtil.getDataResult(result);
    }

    @Override
    public ResponseVO addUserStarStore(String token, Integer storeId) {
        User user = findByToken(token);
        if (user == null) {
            log.info(RespCode.MSG_WITHOUT_AUTH);
            return ControllerUtil.getFalseResultMsgBySelf(RespCode.MSG_WITHOUT_AUTH);
        }
        FavorStore favorStore = favorStoreRepository.findByUserIdAndStoreId(user.getId(), storeId);
        if (favorStore != null) {
            return ControllerUtil.getFalseResultMsgBySelf("请勿重复收藏");
        }
        favorStore = new FavorStore();
        favorStore.setUserId(user.getId());
        favorStore.setStoreId(storeId);
        favorStoreRepository.save(favorStore);
        return ControllerUtil.getDataResult("收藏成功");
    }

    @Override
    public ResponseVO cancelUserStarHouse(String token, Integer houseId) {
        User user = findByToken(token);
        if (user == null) {
            log.info(RespCode.MSG_WITHOUT_AUTH);
            return ControllerUtil.getFalseResultMsgBySelf(RespCode.MSG_WITHOUT_AUTH);
        }
        Collection collection = collectionRepository.findByUserIdAndHouseId(user.getId(), houseId);
        if (collection == null) {
            return ControllerUtil.getFalseResultMsgBySelf("你没有收藏该房屋");
        }
        try {
            collectionRepository.delete(collection);
        } catch (Exception e) {
            return ControllerUtil.getFalseResultMsgBySelf("取消收藏房屋失败");
        }
        return ControllerUtil.getSuccessResultBySelf("取消收藏房屋成功");
    }

    @Override
    public ResponseVO cancelUserStarStore(String token, Integer storeId) {
        User user = findByToken(token);
        if (user == null) {
            log.info(RespCode.MSG_WITHOUT_AUTH);
            return ControllerUtil.getFalseResultMsgBySelf(RespCode.MSG_WITHOUT_AUTH);
        }
        FavorStore favorStore = favorStoreRepository.findByUserIdAndStoreId(user.getId(), storeId);
        if (favorStore == null) {
            return ControllerUtil.getFalseResultMsgBySelf("你没有收藏该商店");
        }
        try {
            favorStoreRepository.delete(favorStore);
        } catch (Exception e) {
            return ControllerUtil.getFalseResultMsgBySelf("取消收藏商店失败");
        }
        return ControllerUtil.getSuccessResultBySelf("取消收藏商店成功");
    }

    @Override
    public ResponseVO certifyLandlord(String token,String authImgUrl) {
        User user = findByToken(token);
        if (user == null) {
            log.info("token不存在");
            return ControllerUtil.getFalseResultMsgBySelf("token不存在");
        }
        if(user.getLandlord()==0 && user.getAuthImgUrl()==null){
            user.setAuthImgUrl(authImgUrl);
            resp.save(user);
            return ControllerUtil.getSuccessResultBySelf("上传完成，等待管理员认证");
        }
        if(user.getLandlord()==0 && user.getAuthImgUrl()!=null){
            return ControllerUtil.getFalseResultMsgBySelf("已上传，请勿重复上传");
        }
        return ControllerUtil.getSuccessResultBySelf("恭喜您，已认证成为房东");
    }

    @Override
    public ResponseVO recommendList() {
        return ControllerUtil.getDataResult(recommedRepository.findAll());
    }

    @Override
    public ResponseVO getUserAllGetMoney(String token) {
        User user = findByToken(token);
        if (user == null) {
            log.info("token不存在");
            return ControllerUtil.getFalseResultMsgBySelf("token不存在");
        }
        List<Withdraw> withdraw = withdrawRepository.findByOpenId(user.getOpenId());
        BigDecimal allMoney = BigDecimal.valueOf(0.00);
        for (Withdraw withdraw1:withdraw){
            if (withdraw1!=null&&withdraw1.getIsFinish()){
                allMoney.add(withdraw1.getMoney());
            }
        }
        return ControllerUtil.getDataResult(allMoney);
    }

    @Override
    public ResponseVO certifyUser(String token, String idcardFront, String idcardBack) {
        User user = findByToken(token);
        if (user == null) {
            log.info("未登录");
            return ControllerUtil.getFalseResultMsgBySelf("token不存在");
        }
        if(user.getIsAuth() == 0 && null == user.getIdCardFrontImg()){
            // 未认证
            user.setIdCardFrontImg(idcardFront);
            user.setIdCardBackImg(idcardBack);
            resp.save(user);
            return ControllerUtil.getSuccessResultBySelf("成功上传认证信息,请等待管理员认证通过!");
        } else {
            if (user.getIsAuth() > 0){
                return ControllerUtil.getSuccessResultBySelf("你已认证通过,请勿重复认证");
            }
            if (user.getIdCardFrontImg() != null){
                return ControllerUtil.getSuccessResultBySelf("正在认证中,请稍后再试");
            }
        }
        return ControllerUtil.getFalseResultMsgBySelf("未知错误");
    }
    public ResponseVO postSignInfo(String token, Integer houseId,String handWriteImgUrl, String contractImgUrl,String userName,String idCardNum,            Date startDate,
                                   Date endDate) {
        User user = findByToken(token);
        if (user == null) {
            log.info("token不存在");
            return ControllerUtil.getFalseResultMsgBySelf("token不存在");
        }
        HouseSign houseSign = new HouseSign();
        houseSign.setUserId(user.getId());
        houseSign.setHouseId(houseId);
        houseSign.setHandWriteImg(handWriteImgUrl);
        houseSign.setContractImg(contractImgUrl);
        houseSign.setUserName(userName);
        houseSign.setIdcardNum(idCardNum);
        houseSign.setStartDate(startDate);
        houseSign.setEndDate(endDate);
        houseSignRepository.save(houseSign);
        return ControllerUtil.getSuccessResultBySelf("签约成功");
    }

    @Override
    public ResponseVO getMyRentedHouse(String token) {
        User user = findByToken(token);
        if (user == null) {
            log.info("token不存在");
            return ControllerUtil.getFalseResultMsgBySelf("token不存在");
        }
        List<HouseSign> houseSigns = houseSignRepository.findByUserId(user.getId());
        if (null != houseSigns && houseSigns.size() > 0){
            for(int i=0; i<houseSigns.size(); i++){
                int houseId = houseSigns.get(i).getHouseId();
                houseSigns.get(i).setHouse(houseRepository.findById(houseId).get());
            }
        }
        return ControllerUtil.getDataResult(houseSigns);
    }
}
