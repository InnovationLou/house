package xyz.nadev.house.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.nadev.house.entity.HouseRepair;
import xyz.nadev.house.service.HouseRepairService;
import xyz.nadev.house.repository.HouseRepairRepository;

@Service
public class HouseRepairServiceImpl implements HouseRepairService {
	@Autowired
	private HouseRepairRepository resp;
}
