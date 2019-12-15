package xyz.nadev.house.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.nadev.house.entity.House;
import xyz.nadev.house.service.HouseService;
import xyz.nadev.house.repository.HouseRepository;

@Service
public class HouseServiceImpl implements HouseService {
	@Autowired
	private HouseRepository resp;
}
