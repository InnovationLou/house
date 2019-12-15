package xyz.nadev.house.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.nadev.house.entity.HouseImg;
import xyz.nadev.house.service.HouseImgService;
import xyz.nadev.house.repository.HouseImgRepository;

@Service
public class HouseImgServiceImpl implements HouseImgService {
	@Autowired
	private HouseImgRepository resp;
}
