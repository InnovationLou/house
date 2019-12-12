package xyz.nadev.house.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserinfoServiceTest {

    @Autowired
    UserinfoService userinfoService;
    
    @Test
    void findAll() {
        System.out.println(userinfoService.findAll());
    }
}