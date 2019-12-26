package xyz.nadev.house.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.nadev.house.entity.Store;
import xyz.nadev.house.entity.StoreImg;
import xyz.nadev.house.service.StoreImgService;
import xyz.nadev.house.repository.StoreImgRepository;
import xyz.nadev.house.util.ControllerUtil;
import xyz.nadev.house.vo.ResponseVO;

import java.util.ArrayList;
import java.util.List;

@Service
public class StoreImgServiceImpl implements StoreImgService {
    @Autowired
    private StoreImgRepository resp;

    @Override
    public ResponseVO findById(Integer id) {
        List<StoreImg> storeImgList = resp.findByStoreId(id);
        if (storeImgList.isEmpty()) {
            return ControllerUtil.getFalseResultMsgBySelf("未找到数据");
        }
        List<String> imgUrlList = new ArrayList<>();
        for (StoreImg img : storeImgList
        ) {
            imgUrlList.add(img.getImgUrl());
        }
        return ControllerUtil.getSuccessResultBySelf(imgUrlList);
    }
}
