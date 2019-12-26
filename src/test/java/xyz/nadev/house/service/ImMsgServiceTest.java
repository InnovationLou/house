package xyz.nadev.house.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.nadev.house.repository.ImMsgRepository;

@SpringBootTest
public class ImMsgServiceTest {

    @Autowired
    ImMsgService imMsgService;

    @Autowired
    ImMsgRepository imMsgRepository;
    @Test
    void getFriends(){
        System.out.println(imMsgService.getChaterListAndLatestWords("dv5svsvPYA").toString());
        System.out.println(imMsgRepository.findAllByReceiverId(9));
    }
}
