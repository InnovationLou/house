package xyz.nadev.house.service;

import xyz.nadev.house.vo.ResponseVO;
import xyz.nadev.house.entity.House;

import java.util.List;

public interface HouseService {
    /**
     * 条件筛选房屋
     *
     * @param house    :
     * @param distance :
     * @param latest   :
     * @param pageNum  :
     * @return: xyz.nadev.house.vo.ResponseVO
     */
    ResponseVO findByCondition(House house, Integer distance, Integer latest, Integer price, Integer pageNum);

    /**
     * 获取所有房源记录
     *
     * @return
     */
    List<House> getAllHouses();

    /**
     * 新增房屋
     */
    ResponseVO addHouse(House house);

    /**
     * 修改房源信息
     *
     * @param house
     * @return
     */
    ResponseVO modifyHouse(House house);

    /**
     * 获取房屋byId
     *
     * @param id
     * @return
     */
    ResponseVO getHouseById(String token, Integer id);

    House findHouseById(Integer id);

    ResponseVO getCollectedHouses(String token);

    ResponseVO rentHouseList(String token);

    ResponseVO getRelatedHouse(String token);

    ResponseVO houseIsFavor(String token, Integer houseId);

    ResponseVO storeIsFavor(String token,Integer storeId);

    ResponseVO getRoomerBill(String token);
}
