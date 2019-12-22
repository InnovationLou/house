package xyz.nadev.house.service;

import xyz.nadev.house.vo.ResponseVO;
import xyz.nadev.house.entity.House;

import java.util.List;

public interface HouseService {
    /**
     * 根据条件筛选房屋
     *
     * @param house   : 条件
     * @param pageNum : 页数
     * @return: xyz.nadev.house.vo.ResponseVO
     */
    ResponseVO findByCondition(House house, int pageNum);

    /**
     * 获取所有房源记录
     * @return
     */
    List<House> getAllHouses();

    ResponseVO houseList();

    /**
     * 新增房屋
     */
    ResponseVO addHouse(House house);

    /**
     * 修改房源信息
     * @param house
     * @return
     */
    ResponseVO modifyHouse(House house);

    /**
     * 获取房屋byId
     * @param id
     * @return
     */
    ResponseVO getHouseById(Integer id);
    House findHouseById(Integer id);
}
