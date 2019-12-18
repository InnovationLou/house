package xyz.nadev.house.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TimServiceTest {

    @Autowired
    TimService timService;

    @Test
    void genUserSig() {
        System.out.println(timService.genUserSig("xiangning"));
    }
}