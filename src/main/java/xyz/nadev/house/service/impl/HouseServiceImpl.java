package xyz.nadev.house.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.nadev.house.entity.*;
import xyz.nadev.house.entity.Collection;
import xyz.nadev.house.repository.*;
import xyz.nadev.house.service.HouseService;
import xyz.nadev.house.service.UserService;
import xyz.nadev.house.util.ControllerUtil;
import xyz.nadev.house.util.EntityUtil;
import xyz.nadev.house.vo.ResponseVO;


import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Slf4j
@Service
public class HouseServiceImpl implements HouseService {

    @Autowired
    private HouseRepository resp;

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private BrowseRepository browseRepository;

    @Autowired
    UserService userService;

    @Autowired
    EntityManager entityManager;

    @Autowired
    FavorStoreRepository favorStoreRepository;

    @Autowired
    HouseSignRepository houseSignRepository;

    @Autowired
    HouseRepository houseRepository;

    private static final Integer SINGLE_PAGE_NUM = 10;

    @Override
    public ResponseVO findByCondition(House house, Integer distance, Integer latest, Integer price, Integer pageNum) {
        //
        // 不传默认第一页
        if (pageNum == null) pageNum = 1;

        StringBuilder sqlStr = new StringBuilder("SELECT *");


        Double lat = house.getLat();
        Double lng = house.getLng();

        if (lat == null || lng == null) {
            // 天安门睡大街
            lat = 39.903740;
            lng = 116.397827;
        }

        // 存储参数
        List<Object> parmList = new ArrayList<>();

        // 距离计算
        sqlStr.append(", ROUND(6378.138 * 2 * ASIN(" +
                "SQRT(POW(SIN((" + lat.toString() +
                " * PI() / 180 - house.lat * PI() / 180 ) / 2 ), 2 ) + COS(" +
                lat.toString() + " * PI() / 180 ) * COS( house.lat * PI() / 180 ) * POW( SIN( (" +
                lng.toString() + "* PI() / 180 - house.lng * PI() / 180 ) / 2 ), 2 ) )" +
                ") * 1000) AS distance ");

        sqlStr.append(" FROM house WHERE");

        // 市区筛选
        if (StringUtils.isNotBlank(house.getCity())) {
            if (!sqlStr.toString().endsWith("WHERE")) sqlStr.append(" AND");
            sqlStr.append(
                    " house.city = ?"
            );
            parmList.add(house.getCity());
        }

        // title和 houseinfo筛选
        if (StringUtils.isNotBlank(house.getHouseInfo())) {
            String[] words = house.getHouseInfo().trim().split(" ");
            //  拆分搜索关键词
            for (String w : words
            ) {
                // 排除空格中的空格
                if (StringUtils.isNotBlank(w.trim())) {
                    if (!sqlStr.toString().endsWith("WHERE")) sqlStr.append(" AND");
                    sqlStr.append(
                            " (house.house_info LIKE CONCAT('%',?,'%') OR house.title LIKE CONCAT('%',?,'%'))"
                    );
                    parmList.add(w);
                    parmList.add(w);
                }
            }

        }

        // 朝向筛选
        if (StringUtils.isNotBlank(house.getOrientation())) {
            if (!sqlStr.toString().endsWith("WHERE")) sqlStr.append(" AND");
            sqlStr.append(
                    " house.orientation = ?"
            );
            parmList.add(house.getOrientation());
        }

        // 地区筛选
        if (StringUtils.isNotBlank(house.getDistrict())) {
            if (!sqlStr.toString().endsWith("WHERE")) sqlStr.append(" AND");

            sqlStr.append(
                    " house.district = ?"

            );
            parmList.add(house.getDistrict());
        }

        // 户型筛选
        if (StringUtils.isNotBlank(house.getHouseType())) {
            if (!sqlStr.toString().endsWith("WHERE")) sqlStr.append(" AND");
            sqlStr.append(
                    " house.house_type LIKE CONCAT(?, '%')"
            );
            parmList.add(house.getHouseType());
        }

        // 付款类型
        if (StringUtils.isNotBlank(house.getCashType())) {
            if (!sqlStr.toString().endsWith("WHERE")) sqlStr.append(" AND");

            sqlStr.append(
                    " house.cash_type = ?");
            parmList.add(house.getCashType());
        }

        // 是否配套齐全
        if (null != house.getHasComplete() && house.getHasComplete() != 0) {
            if (!sqlStr.toString().endsWith("WHERE")) sqlStr.append(" AND");
            sqlStr.append(
                    " house.has_complete = 1");
        }

        // 可否短租
        if (null != house.getShortRent() && 0 != house.getShortRent()) {
            if (!sqlStr.toString().endsWith("WHERE")) sqlStr.append(" AND");
            sqlStr.append(
                    " house.short_rent = 1");
        }

        // 限女生租
        if (null != house.getGirlShared() && 0 != house.getGirlShared()) {
            if (!sqlStr.toString().endsWith("WHERE")) sqlStr.append(" AND");
            sqlStr.append(
                    " house.girl_shared = 1");
        }
        // 限男生租
        if (null != house.getBoyShared() && 0 != house.getBoyShared()) {
            if (!sqlStr.toString().endsWith("WHERE")) sqlStr.append(" AND");
            sqlStr.append(
                    " house.boy_shared = 1");
        }

        // 是否有阳台
        if (null != house.getHasBalcony() && 0 != house.getHasBalcony()) {
            if (!sqlStr.toString().endsWith("WHERE")) sqlStr.append(" AND");
            sqlStr.append(
                    " house.has_balcony = 1");
        }

        // 出租类型 整租还是 合租
        if (null != house.getRentType() && 0 != house.getRentType()) {
            if (!sqlStr.toString().endsWith("WHERE")) sqlStr.append(" AND");
            sqlStr.append(
                    " house.rent_type = 1");
        }

        // 限制价格区间
        if (price != null && price != 0) {
            if (!sqlStr.toString().endsWith("WHERE")) sqlStr.append(" AND");
            switch (price) {
                case 1:
                    sqlStr.append(
                            " house.cash <= 1000");
                    break;
                case 2:
                    sqlStr.append(
                            " house.cash > 1000 AND house.cash <= 1500");
                    break;
                case 3:
                    sqlStr.append(
                            " house.cash > 1500 AND house.cash <= 2000");
                    break;
                case 4:
                    sqlStr.append(
                            " house.cash > 2000 AND house.cash <= 2500");
                    break;
                case 5:
                    sqlStr.append(
                            " house.cash > 2500");
                    break;
            }
        }

        //	如果没有查询条件产生 删除where
        if (sqlStr.toString().endsWith("WHERE")) sqlStr.setLength(sqlStr.length() - 5);

        // 限制距离
        if (distance != null && distance != 0) {
            sqlStr.append(
                    " HAVING distance < ?");
            parmList.add(distance);
        }

        // 按租金排序
        if (house.getCash() != null) {
            if (house.getCash() == 0)
                sqlStr.append(" ORDER BY house.cash");
            else
                sqlStr.append(" ORDER BY house.cash DESC");
        } else if (latest != null && latest == 1) {
            // 按时间排序
            sqlStr.append(" ORDER BY house.gmt_create DESC");
        } else
            // 默认距离排序
            sqlStr.append(" ORDER BY distance");

        Query query = entityManager.createNativeQuery(sqlStr.toString(), House.class);

        // 注入参数
        for (int i = 0; i < parmList.size(); i++) {
            query.setParameter(i + 1, parmList.get(i));
        }
        query.setFirstResult((pageNum - 1) * SINGLE_PAGE_NUM);
        query.setMaxResults(SINGLE_PAGE_NUM);
        List<House> result = query.getResultList();

        if (result.isEmpty()) {
            return ControllerUtil.getFalseResultMsgBySelf("未查找到数据");
        }
        return ControllerUtil.getSuccessResultBySelf(result);

    }

    @Override
    public List<House> getAllHouses() {
        return resp.findAll();
    }


    @Override
    public ResponseVO addHouse(House house) {
        House newAdd = resp.save(house);
        return ControllerUtil.getDataResult(newAdd);
    }

    @Override
    public ResponseVO modifyHouse(House newHouse) {
        if (newHouse == null) {
            return ControllerUtil.getFalseResultMsgBySelf("传入数据非法");
        }
        Integer id = newHouse.getId();
        if (id == null || id < 0){
            return ControllerUtil.getFalseResultMsgBySelf("非法ID传入" + id);
        }
        House oldHouse = houseRepository.findById(id).get();
        EntityUtil.update(newHouse,oldHouse);
        return ControllerUtil.getSuccessResultBySelf(resp.save(oldHouse));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseVO getHouseById(String token, Integer id) {
        Optional<House> house = resp.findById(id);
        if (!house.isPresent()) {
            return ControllerUtil.getFalseResultMsgBySelf("未找到该房屋");
        }

        //用token拿到当前用户信息（UserId）
        User user = userService.findByToken(token);
        if (user == null) {
            log.info("token不存在");
            return null;
        }

        Browse browse = browseRepository.findByUserIdAndHouseId(user.getId(), id);
        if (browse == null) {
            try {
                Integer userId = user.getId();
                browse = new Browse();
                browse.setUserId(userId);
                browse.setHouseId(id);
                browseRepository.save(browse);
            } catch (Exception e) {
                log.info("保存该用户游览记录失败");
            }
        }else {
            //更新一下游览时间
            browse.setGmtCreate(new Date());
            browseRepository.save(browse);
        }
        return ControllerUtil.getSuccessResultBySelf(house.get());
    }
    @Override
    public House findHouseById(Integer id) {
        Optional<House> house = resp.findById(id);
        if (!house.isPresent()) {
            return null;
        }
        return house.get();
    }

    @Override
    public ResponseVO getCollectedHouses(String token) {

        User user = userService.findByToken(token);
        if (user == null) {
            log.info("token不存在");
            return null;
        }
        Integer userId = user.getId();
        List<Collection> collections = collectionRepository.findCollectionsByUserId(userId);
        List<Optional<House>> houseList = new ArrayList<>();
        for (Collection c : collections) {
            houseList.add(resp.findById(c.getHouseId()));
        }
        return ControllerUtil.getDataResult(houseList);
    }


    @Override
    public ResponseVO rentHouseList(String token) {
        User user = userService.findByToken(token);
        if (user == null) {
            return ControllerUtil.getFalseResultMsgBySelf("非法操作");
        }
        if (user.getLandlord() != 1) {
            return ControllerUtil.getFalseResultMsgBySelf("请先认证房东");
        }
        return ControllerUtil.getDataResult(resp.findByUserId(user.getId()));
    }

    @Override
    public ResponseVO getRelatedHouse(String token) {
        User user = userService.findByToken(token);
        if (user == null) {
            return ControllerUtil.getFalseResultMsgBySelf("非法操作");
        }
        List<HouseSign> houseSigns =  houseSignRepository.findByUserId(user.getId());
        List list = new ArrayList();
        if (houseSigns.isEmpty()){return ControllerUtil.getFalseResultMsgBySelf("没有相关签约");}
        for (HouseSign houseSign:houseSigns){
            Date date = new Date();
            if (date.compareTo(houseSign.getEndDate())>0 || houseSign.getIsPaid()==0||houseSign.getIsDeleted()==1||houseSign.getIsFulfill()==0){
                continue;
            }
            House house = findHouseById(houseSign.getHouseId());
            list.add(house);
        }
        return ControllerUtil.getDataResult(resp.findHousesByTenantId(user.getId()));
    }

    @Override
    public ResponseVO houseIsFavor(String token, Integer houseId) {
        User user = userService.findByToken(token);
        if (user == null) {
            log.info("token不存在");
            return ControllerUtil.getFalseResultMsgBySelf("token不存在");
        }
        Map<String, Boolean> map = new HashMap<>();
        Collection collection = collectionRepository.findByUserIdAndHouseId(user.getId(), houseId);

        if (collection == null) {
            map.put("isfavor", false);
        } else {
            map.put("isfavor", true);
        }
        return ControllerUtil.getDataResult(map);
    }

    @Override
    public ResponseVO storeIsFavor(String token, Integer storeId) {
        User user = userService.findByToken(token);
        if (user == null) {
            log.info("token不存在");
            return ControllerUtil.getFalseResultMsgBySelf("token不存在");
        }
        Map<String, Boolean> map = new HashMap<>();
        FavorStore favorStore = favorStoreRepository.findByUserIdAndStoreId(user.getId(), storeId);
        if (favorStore == null) {
            map.put("isfavor", false);
        } else {
            map.put("isfavor", true);
        }
        return ControllerUtil.getDataResult(map);
    }
}
