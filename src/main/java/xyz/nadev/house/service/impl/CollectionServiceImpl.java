package xyz.nadev.house.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.nadev.house.entity.Collection;
import xyz.nadev.house.service.CollectionService;
import xyz.nadev.house.repository.CollectionRepository;

@Service
public class CollectionServiceImpl implements CollectionService {
	@Autowired
	private CollectionRepository resp;
}
