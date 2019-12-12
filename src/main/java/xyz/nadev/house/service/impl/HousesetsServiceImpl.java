package xyz.nadev.house.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.nadev.house.entity.Housesets;
import xyz.nadev.house.service.HousesetsService;
import xyz.nadev.house.repository.HousesetsRepository;

@Service
public class HousesetsServiceImpl implements HousesetsService {
	@Autowired
	private HousesetsRepository resp;
}
