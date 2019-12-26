package xyz.nadev.house.service;

import xyz.nadev.house.vo.ResponseVO;

import java.util.Date;

public interface MessageService {
    ResponseVO getNotifies(Integer days);
}
