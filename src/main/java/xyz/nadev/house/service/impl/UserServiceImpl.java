package xyz.nadev.house.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.nadev.house.entity.User;
import xyz.nadev.house.service.UserService;
import xyz.nadev.house.repository.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository resp;

	@Override
	public List<User> findAll() {
		return resp.findAll();
	}
}
