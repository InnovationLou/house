package xyz.nadev.house.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.nadev.house.entity.House;
import xyz.nadev.house.entity.User;
import xyz.nadev.house.service.HouseService;
import xyz.nadev.house.repository.HouseRepository;
import xyz.nadev.house.util.ControllerUtil;
import xyz.nadev.house.vo.ResponseVO;

import java.util.List;
import java.util.Optional;

@Service
public class HouseServiceImpl implements HouseService {
	@Autowired
	private HouseRepository resp;

	@Override
	public List<House> getAllHouses() {
		return resp.findAll();
	}

	@Override
	public ResponseVO houseList() {
		List<House> list=null;
		list=getAllHouses();
		if(list.isEmpty()){
			return ControllerUtil.getFalseResultMsgBySelf("当前无房屋数据");
		}
		return ControllerUtil.getDataResult(list);
	}

	@Override
	public ResponseVO addHouse(House house) {
		House newAdd=resp.save(house);
		return ControllerUtil.getDataResult(newAdd);
	}

	@Override
	public ResponseVO modifyHouse(House house) {
		if(house == null){
			return ControllerUtil.getFalseResultMsgBySelf("传入数据非法");
		}
		return ControllerUtil.getSuccessResultBySelf(resp.save(house));
	}

	@Override
	public ResponseVO getHouseById(Integer id) {
		Optional<House> house=resp.findById(id);
		if(!house.isPresent()){
			return ControllerUtil.getFalseResultMsgBySelf("未找到该房屋");
		}
		return ControllerUtil.getSuccessResultBySelf(house.get());
	}

	@Override
	public House findHouseById(Integer id) {
		Optional<House> house=resp.findById(id);
		if(!house.isPresent()){
			return null;
		}
		return house.get();
	}
}
