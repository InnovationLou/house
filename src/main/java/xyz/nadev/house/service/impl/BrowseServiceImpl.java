package xyz.nadev.house.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.nadev.house.entity.Browse;
import xyz.nadev.house.service.BrowseService;
import xyz.nadev.house.repository.BrowseRepository;

@Service
public class BrowseServiceImpl implements BrowseService {
	@Autowired
	private BrowseRepository resp;
}
