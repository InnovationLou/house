package xyz.nadev.house.service;


import xyz.nadev.house.VO.ResponseVO;

public interface TimService {
    /**
     * 生成用户UserSig
     *
     * @param userId :
     * @return: xyz.nadev.house.vo.ResponseVO
     */
    ResponseVO genUserSig(String userId);
}
