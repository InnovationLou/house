package xyz.nadev.house.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.nadev.house.entity.HouseRepairImg;
import xyz.nadev.house.service.HouseRepairImgService;
import xyz.nadev.house.repository.HouseRepairImgRepository;

@Service
public class HouseRepairImgServiceImpl implements HouseRepairImgService {
	@Autowired
	private HouseRepairImgRepository resp;
}
