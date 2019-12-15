package xyz.nadev.house.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.nadev.house.entity.Message;
import xyz.nadev.house.service.MessageService;
import xyz.nadev.house.repository.MessageRepository;

@Service
public class MessageServiceImpl implements MessageService {
	@Autowired
	private MessageRepository resp;
}
