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
    WxPayServiceImpl wxPayServiceImpl;

    @Autowired
    CollectionRepository collectionRepository;

    @Autowired
    HouseRepository houseRepository;

    @Autowired
    BillRepository billRepository;
    @Autowired
    HouseSignRepository houseSignRepository;

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
            log.info("token不存在");
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
                log.info("token不存在");
                return ControllerUtil.getFalseResultMsgBySelf("token不存在");
            }
            Integer userId = user.getId();
            Collection collection = new Collection();
            collection.setUserId(userId);
            collection.setHouseId(houseId);
            collectionRepository.save(collection);
            return ControllerUtil.getDataResult("收藏成功");
        } catch (Exception e) {
            log.info("添加收藏记录失败");
            return ControllerUtil.getFalseResultMsgBySelf("添加收藏记录失败");
        }

    }

    @Override
    public ResponseVO getUserBrowse(String token, Integer limit, Integer start) {
        try {
            User user = findByToken(token);
            if (user == null) {
                log.info("token不存在");
                return ControllerUtil.getFalseResultMsgBySelf("token不存在");
            }
            Integer userId = user.getId();
            List<Browse> browses = browseRepository.getBrowseByUserId(userId, limit, start);
            List result = new ArrayList<>();
            for (Browse browse : browses) {
                Optional house = houseRepository.findById(browse.getHouseId());
                result.add(house);
            }
            return ControllerUtil.getDataResult(result);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ResponseVO getUserBill(String token) {
        try {
            User user = findByToken(token);
            if (user == null) {
                log.info("token不存在");
                return ControllerUtil.getFalseResultMsgBySelf("token不存在");
            }
            List result = new ArrayList();
            List<Bill> bills = billRepository.findByUserId(user.getId());
            for (Bill bill : bills) {
                List list = new ArrayList();
                Map map = new HashMap();
                System.out.println("bill: " + bill.toString());
                Optional<House> house = houseRepository.findById(bill.getHouseId());
                System.out.println(house.toString());
                map.put("houseInfo", house.get().getHouseInfo());
                map.put("cashType", house.get().getCashType());
                map.put("houseType", house.get().getHouseType());
                map.put("payItem", bill.getPayItem());
                map.put("money", bill.getMoney());
                map.put("gmtCreate", bill.getGmtCreate());
                map.put("isPaid", bill.getIsPaid());
                map.put("remark", bill.getRemark());
                map.put("dead_date",bill.getDeadDate());
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
            log.info("token不存在");
            return ControllerUtil.getFalseResultMsgBySelf("token不存在");
        }
        if (user.getLandlord() == 0) {
            List result = new ArrayList<>();
            List<HouseSign> houseSigns = houseSignRepository.findByUserId(user.getId());
            System.out.println("houseSign:" + houseSigns.toString());
            for (HouseSign houseSign : houseSigns) {
                Optional<House> house = houseRepository.findById(houseSign.getHouseId());
                Map<String, Object> map = new HashMap<>();
                map.put("cashType", house.get().getCash());
                map.put("houseInfo", house.get().getHouseInfo());
                map.put("houseType", house.get().getHouseType());
                map.put("gmtCreate", houseSign.getGmtCreate());
                map.put("endCreate", houseSign.getEndCreate());
                map.put("expDate", houseSign.getExpDate());
                //履行状态：当前日期.compareTo(截止日期)  = 0, < -1, > 1
                if (new Date().compareTo(houseSign.getEndCreate()) > 0 && houseSign.getIsFulfill() != 0) {
                    houseSign.setIsFulfill(WxPayConfig.HOUSE_NOT_FULFILL);
                    houseSignRepository.save(houseSign);
                }
                map.put("isFulFill", houseSign.getIsFulfill());
                result.add(map);
            }
            return ControllerUtil.getDataResult(result);
        }
        return null;
    }

}
