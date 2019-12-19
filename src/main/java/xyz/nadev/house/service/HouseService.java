package xyz.nadev.house.service;

import xyz.nadev.house.entity.House;
import xyz.nadev.house.vo.ResponseVO;

import java.util.List;

public interface HouseService {

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
