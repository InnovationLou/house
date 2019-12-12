package xyz.nadev.house.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.nadev.house.entity.Swipperpics;
import xyz.nadev.house.service.SwipperpicsService;
import xyz.nadev.house.repository.SwipperpicsRepository;

@Service
public class SwipperpicsServiceImpl implements SwipperpicsService {
	@Autowired
	private SwipperpicsRepository resp;
}
