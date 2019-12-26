package xyz.nadev.house.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StoreServiceTest {
    @Autowired
    StoreService storeService;

    @Test
    void findByType() {
        System.out.println(storeService.findByType("餐饮美食"));
    }

    @Test
    void findById() {
        System.out.println(storeService.findById(1));
    }

    @Test
    void search() {
        System.out.println(storeService.search("成华区"));
        System.out.println(storeService.search("宜宾"));
    }
}