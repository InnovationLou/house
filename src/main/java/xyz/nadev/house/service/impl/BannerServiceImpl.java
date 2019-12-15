package xyz.nadev.house.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.nadev.house.entity.Banner;
import xyz.nadev.house.service.BannerService;
import xyz.nadev.house.repository.BannerRepository;

@Service
public class BannerServiceImpl implements BannerService {
	@Autowired
	private BannerRepository resp;
}
