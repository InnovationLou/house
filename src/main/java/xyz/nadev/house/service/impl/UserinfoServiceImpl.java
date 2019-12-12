package xyz.nadev.house.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.nadev.house.entity.Userinfo;
import xyz.nadev.house.service.UserinfoService;
import xyz.nadev.house.repository.UserinfoRepository;

import java.util.List;


@Service
public class UserinfoServiceImpl implements UserinfoService {
	@Autowired
	private UserinfoRepository resp;

	@Override
	public List<Userinfo> findAll() {
		return resp.findAll();
	}
}
