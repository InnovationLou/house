package xyz.nadev.house.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.flogger.Flogger;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import xyz.nadev.house.entity.User;
import xyz.nadev.house.service.UserService;
import xyz.nadev.house.repository.UserRepository;
import xyz.nadev.house.util.ControllerUtil;
import xyz.nadev.house.util.EntityUtil;
import xyz.nadev.house.util.UUIDUtil;
import xyz.nadev.house.util.constant.RespCode;
import xyz.nadev.house.vo.ResponseVO;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserServiceImpl implements UserService {


    // 设置key有效期,实现登录超时 单位分钟
    private static final int USER_LOGIN_TIMEOUT = 30;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Autowired
    UserRepository resp;

    @Value("${wx.app-id}")
    String appId;

    @Value("${wx.app-secret}")
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
}
