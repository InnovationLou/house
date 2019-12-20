package xyz.nadev.house.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.nadev.house.service.TimService;
import xyz.nadev.house.VO.ResponseVO;

@Slf4j
@RestController
@RequestMapping("/tim")
public class TimController {

    @Autowired
    TimService timService;

    @ApiOperation("返回用户UserSig")
    @GetMapping("/sig")
    public ResponseVO genSig(String userId) {
        return timService.genUserSig(userId);
    }
}
