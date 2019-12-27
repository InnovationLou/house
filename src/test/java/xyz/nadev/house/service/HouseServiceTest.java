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
    void findByCondition() {
        House house = new House();
        house.setLat(29.987096);
        house.setLng(102.996122);
//        house.setDistrict("雨城区");
//        house.setCash(1);
//        house.setHasComplete(0);
        System.out.println(houseService.findByCondition(house, 0, null, 5, 1).toString());
    }

    @Test
    void testHouseList() {
    }

    @Test
    void addHouse() {
        House house = new House();
        house.setUserId(10001);
        house.setHouseInfo("testtesttest");
        house.setReleased(0);
        house.setRented(0);
        house.setTenantId(10002);
        house.setOrientation("南");

        System.out.println(houseService.addHouse(house));
    }

    @Test
    void modifyHouse() {
        Integer id = 2;
        House house = houseService.findHouseById(id);
        System.out.println(house);
        house.setHouseInfo("123456789");
        System.out.println(houseService.modifyHouse(house));
    }

    @Test
    void findHouseById() {
        Integer id = 2;
        System.out.println(houseService.findHouseById(id));
    }

    @Test
    void getCollectedHouses() {
//        Integer userId = 10001;
//        System.out.println(houseService.getCollectedHouses(userId));
        Integer userId = 10001;
        System.out.println(houseService.getCollectedHouses(userId.toString()));
    }

}