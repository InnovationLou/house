package xyz.nadev.house.service;

import xyz.nadev.house.entity.House;
import xyz.nadev.house.entity.HouseRepairImg;
import xyz.nadev.house.vo.ResponseVO;

public interface HouseRepairService {
    ResponseVO getRepairListByUserToken(String token);

    ResponseVO uploadRepair(String token, House house, String phone, String content, HouseRepairImg img);
}
