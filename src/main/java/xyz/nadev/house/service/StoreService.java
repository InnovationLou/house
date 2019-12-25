package xyz.nadev.house.service;

import xyz.nadev.house.vo.ResponseVO;


public interface StoreService {
    /**
     * 根据商铺类型查找
     * @param type :
     * @return: xyz.nadev.house.vo.ResponseVO
     */
    ResponseVO findByType(String type);

    ResponseVO findById(Integer id);

    ResponseVO search(String keyword);
}
