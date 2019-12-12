package xyz.nadev.house.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.nadev.house.entity.Systemmsg;
import xyz.nadev.house.service.SystemmsgService;
import xyz.nadev.house.repository.SystemmsgRepository;

@Service
public class SystemmsgServiceImpl implements SystemmsgService {
	@Autowired
	private SystemmsgRepository resp;
}
