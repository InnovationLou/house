package xyz.nadev.house.service;

import xyz.nadev.house.entity.House;
import xyz.nadev.house.entity.HouseRepairImg;
import xyz.nadev.house.vo.ResponseVO;

import java.util.Date;

public interface HouseRepairService {
    ResponseVO getRepairListByUserToken(String token);

    ResponseVO uploadRepair(String token, Integer houseId, String phone, Date repairTime, String content, HouseRepairImg img);
}
