package xyz.nadev.house.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.nadev.house.entity.Store;
import xyz.nadev.house.repository.StoreRepository;
import xyz.nadev.house.service.StoreService;
import xyz.nadev.house.util.ControllerUtil;
import xyz.nadev.house.vo.ResponseVO;

import java.util.List;
import java.util.Optional;

@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreRepository resp;

    /**
     * 根据商铺类型查找
     *
     * @param type :
     * @return: xyz.nadev.house.vo.ResponseVO
     */
    @Override
    public ResponseVO findByType(String type) {
        return ControllerUtil.getSuccessResultBySelf(resp.findByType(type));
    }

    @Override
    public ResponseVO findById(Integer id) {
        Optional<Store> storeOptional = resp.findById(id);
        if (storeOptional.isPresent()) {
            return ControllerUtil.getSuccessResultBySelf(storeOptional.get());
        }
        return ControllerUtil.getFalseResultMsgBySelf("没有找到信息");
    }

    @Override
    public ResponseVO search(String type, String keyword) {
        List<Store> storeList = resp.search(type, keyword);
        if (storeList.isEmpty()) {
            return ControllerUtil.getFalseResultMsgBySelf("未查询到数据");
        }
        return ControllerUtil.getSuccessResultBySelf(storeList);
    }
}
