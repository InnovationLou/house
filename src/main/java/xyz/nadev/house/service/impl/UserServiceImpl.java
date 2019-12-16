package xyz.nadev.house.service.impl;

import com.alibaba.fastjson.JSONObject;
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
import xyz.nadev.house.vo.ResponseVO;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
public class UserServiceImpl implements UserService {

    public static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

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
    public String getOpenId(String code) {
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
            logger.error(e.toString());
            return null;
        } finally {
            httpGet.abort();
        }

        // 取出openId
        if (responseTxt != null) {
            JSONObject jsonObject = JSONObject.parseObject(responseTxt);
            openId = jsonObject.getString("openid");
            if (openId == null) return null;
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
     * 保存/更新user
     *
     * @param user :
     * @return: boolean
     */
    @Override
    public ResponseVO save(User user) {
        try {
            if (user.getId() != null) {
                User old = resp.findByOpenId(user.getOpenId());
                EntityUtil.update(user, old);
            }
            resp.save(user);
        } catch (Exception e) {
            return ControllerUtil.getFalseResultMsgBySelf("保存出错");
        }
        return ControllerUtil.getSuccessResultBySelf("保存成功");
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
        String openId = getOpenId(code);
        if (openId == null)
            return null;
        user.setOpenId(openId);
        save(user);
        return login(code);
    }

    /**
     * 用户登录 当前未判断已登录用户重复登录
     *
     * @param code :
     * @return: xyz.nadev.house.vo.ResponseVO
     */
    @Override
    public ResponseVO login(String code) {
        String openId = getOpenId(code);
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
        if (redisTemplate.hasKey(token)) return ControllerUtil.getSuccessResultBySelf("当前token在线");
        return ControllerUtil.getFalseResultMsgBySelf("token已过期");
    }
}
