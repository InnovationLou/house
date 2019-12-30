package xyz.nadev.house.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.nadev.house.service.MessageService;
import xyz.nadev.house.repository.MessageRepository;
import xyz.nadev.house.util.ControllerUtil;
import xyz.nadev.house.vo.ResponseVO;

import java.util.Date;

@Service
public class MessageServiceImpl implements MessageService {
	@Autowired
	private MessageRepository resp;

	@Override
	public ResponseVO getNotifies(Integer days) {
		Date now=new Date();
		// milliseconds
		Date old=new Date(now.getTime() - days * 24 * 3600 * 1000l);
		System.out.println("现在:" + now.getTime() + ", " + new Date(now.getTime()));
		System.out.println("过去:" + old.getTime() + ", " + new Date(old.getTime()));
		return ControllerUtil.getDataResult(resp.findByGmtCreateBetween(old,now));
	}
}
