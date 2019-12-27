package xyz.nadev.house.service;

import xyz.nadev.house.entity.User;
import xyz.nadev.house.vo.ResponseVO;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public interface UserService {
    /**
     * 获取所有用户信息
     *
     * @return: java.util.List<xyz.nadev.house.entity.User>
     */
    List<User> findAll();

    /**
     * 根据Id查找用户
     *
     * @param id :
     * @return: xyz.nadev.house.entity.User
     */
    User findById(int id);

    /**
     * 请求API获取用户openId
     *
     * @param code : 小程序js code
     * @return: java.lang.String
     */
    String getWxOpenId(String code) throws IOException;

    /**
     * 根据openId查找用户
     *
     * @param openId :
     * @return: xyz.nadev.house.entity.User
     */
    User findByOpenId(String openId);

    /**
     * 拿token查当前登录用户
     * @param token :
     * @return: xyz.nadev.house.entity.User
     */
    User findByToken(String token);

    /**
     * 保存user
     *
     * @param user :
     * @return: boolean
     */
    ResponseVO saveUser(User user);

    /**
     * 更新用户信息
     *
     * @param token :
     * @param user  :
     * @return: xyz.nadev.house.vo.ResponseVO
     */
    ResponseVO updateUser(String token, User user);

    /**
     * 用户注册
     *
     * @param user :
     * @param code :
     * @return: xyz.nadev.house.vo.ResponseVO
     */
    ResponseVO register(User user, String code);

    /**
     * 用户登录
     *
     * @param code :
     * @return: xyz.nadev.house.vo.ResponseVO
     */
    ResponseVO login(String code);

    /**
     * 检查token是否过期
     *
     * @param token :
     * @return: xyz.nadev.house.vo.ResponseVO
     */
    ResponseVO checkToken(String token);

    /**
     * 返回用户信息
     *
     * @return: xyz.nadev.house.vo.ResponseVO
     */
    ResponseVO getUserInfo(String token);

    /**
     * 用于用户发起提现
     * @param token
     * @param  request
     * @return
     */
    ResponseVO launchWithdraw(String token, HttpServletRequest request) throws Exception;

    /**
     * 用户添加收藏信息
     * @param token
     * @param houseId
     * @return
     */
    ResponseVO addUserColleection(String token,Integer houseId);

    /**
     * 查看自己游览历史
     * @param token
     * @param page
     * @return
     */
    ResponseVO findUserBrowse(String token, Integer page,Integer size);

    /**
     * 获得用户账单信息
     * @param token
     * @return
     */
    ResponseVO getUserBill(String token);

    /**
     *  获得用户签约信息
     * @param token
     * @return
     */
    ResponseVO getSignInfo(String token);



    /**
     * 获得用户收藏商店信息
     * @param token
     * @return
     */
    ResponseVO getUserStarStore(String token);

    /**
     * 增加用户收藏信息
     * @param token
     * @param storeId
     * @return
     */
    ResponseVO addUserStarStore(String token,Integer storeId);

    /**
     * 用户取消收藏房源
     * @param token
     * @param houseId
     * @return
     */
    ResponseVO cancelUserStarHouse(String token,Integer houseId);

    /**
     * 用户取消收藏商店
     * @param token
     * @param storeId
     * @return
     */
    ResponseVO cancelUserStarStore(String token,Integer storeId);



}
