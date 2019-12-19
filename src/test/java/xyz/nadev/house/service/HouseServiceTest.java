package xyz.nadev.house.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.nadev.house.entity.House;
import xyz.nadev.house.util.ControllerUtil;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HouseServiceTest {

    @Autowired
    HouseService houseService;

    @Test
    void houseList() {
        System.out.println(houseService.houseList());
    }

    @Test
    void testHouseList() {
    }

    @Test
    void addHouse() {
        House house=new House();
        house.setUserId(10001);
        house.setHouseInfo("testtesttest");
        house.setReleased(0);
        house.setRented(0);
        house.setTenantId(10002);
        house.setOrientation("南");
        house.setGmtStart(new Date());
        house.setGmtEnd(new Date());
        System.out.println(houseService.addHouse(house));
    }

    @Test
    void modifyHouse() {
        Integer id=2;
        House house=houseService.findHouseById(id);
        System.out.println(house);
        house.setHouseInfo("123456789");
        System.out.println(houseService.modifyHouse(house));
    }

    @Test
    void findHouseById(){
        Integer id=2;
        System.out.println(houseService.findHouseById(id));
    }
}