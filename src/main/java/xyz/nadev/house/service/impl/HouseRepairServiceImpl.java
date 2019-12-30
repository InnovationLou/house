package xyz.nadev.house.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.nadev.house.entity.House;
import xyz.nadev.house.entity.HouseRepair;
import xyz.nadev.house.entity.HouseRepairImg;
import xyz.nadev.house.entity.User;
import xyz.nadev.house.repository.HouseRepairImgRepository;
import xyz.nadev.house.repository.HouseRepository;
import xyz.nadev.house.service.HouseRepairService;
import xyz.nadev.house.repository.HouseRepairRepository;
import xyz.nadev.house.service.UserService;
import xyz.nadev.house.util.ControllerUtil;
import xyz.nadev.house.vo.ResponseVO;

import java.util.Date;
import java.util.List;

@Service
public class HouseRepairServiceImpl implements HouseRepairService {
	@Autowired
	private HouseRepairRepository resp;

	@Autowired
	private HouseRepairRepository repairRepository;

	@Autowired
	private HouseRepairImgRepository imgRepository;

	@Autowired
	private HouseRepository houseRepository;



	@Autowired
	private UserService userService;
	@Override
	public ResponseVO getRepairListByUserToken(String token) {
		User user=userService.findByToken(token);
		if(user==null){
			return ControllerUtil.getFalseResultMsgBySelf("非法操作");
		}
		List<HouseRepair> list=repairRepository.findHouseRepairsByUserId(user.getId());
		for (HouseRepair hr :
				list) {
			hr.setHouse(houseRepository.findById(hr.getHouseId()).get());
		}
		return ControllerUtil.getDataResult(list);
	}

	@Override
	public ResponseVO uploadRepair(String token, Integer houseId, String phone,Date repairTime, String content, HouseRepairImg img) {
		User user=userService.findByToken(token);
		if(user==null){
			return ControllerUtil.getFalseResultMsgBySelf("上传失败，非法操作");
		}
		HouseRepair repair=new HouseRepair();
		repair.setContent(content);
		repair.setRepaireTime(repairTime);
		repair.setHouseId(houseId);
		repair.setPhone(phone);
		repair.setStatus(0);
		repair.setUserId(user.getId());
		repairRepository.save(repair);
		imgRepository.save(img);
		return ControllerUtil.getDataResult("上传成功:"+repair);
	}
}