package xyz.nadev.house.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xyz.nadev.house.service.TimService;
import xyz.nadev.house.util.ControllerUtil;
import xyz.nadev.house.util.TLSSigAPIv2;
import xyz.nadev.house.vo.ResponseVO;

@Slf4j
@Service
public class TimServiceImpl implements TimService {

    public static final long expire = 180 * 86400;

    @Value("${tim.app-id}")
    Long appId;

    @Value("${tim.app-key}")
    String appKey;


    @Override
    public ResponseVO genUserSig(String userId) {
        TLSSigAPIv2 tlsSigAPIv2 = new TLSSigAPIv2(appId, appKey);
        return ControllerUtil.getSuccessResultBySelf(tlsSigAPIv2.genSig(userId, expire));
    }
}
