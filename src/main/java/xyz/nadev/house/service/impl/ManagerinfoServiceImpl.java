package xyz.nadev.house.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.nadev.house.entity.Managerinfo;
import xyz.nadev.house.service.ManagerinfoService;
import xyz.nadev.house.repository.ManagerinfoRepository;

@Service
public class ManagerinfoServiceImpl implements ManagerinfoService {
	@Autowired
	private ManagerinfoRepository resp;
}
