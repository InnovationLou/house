package xyz.nadev.house.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import xyz.nadev.house.entity.User;

import java.io.IOException;

@SpringBootTest
class UserServiceTest {
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Autowired
    UserService userService;

    @Test
    void findAll() {
        System.out.println(userService.findAll());
    }

    @Test
    void getOpenId() {
        try {
            System.out.println(userService.getWxOpenId("033aLrQm0bzuUl1zRePm0EejQm0aLrQr"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void findByOpenId() {
        System.out.println(userService.findByOpenId("oX98M5D_n2lS0nsBAfYSDABoBmh0").toString());
    }

    @Test
    void findById() {
        System.out.println(userService.findById(1).toString());
    }

    @Test
    void save() {
        User user = userService.findById(1);
        user.setNickName("nadev");
        userService.saveUser(user);

        User newUser = new User();
        newUser.setOpenId("1X98M5D_n2lS0nsBAfYSDABoBmh1");
        newUser.setNickName("via");
        userService.saveUser(newUser);
        System.out.println(userService.findById(1).toString());
    }

    @Test
    void login() {
        System.out.println(userService.login("043APZPy1b4gS906ecQy13SjQy1APZP6").toString());
    }

    @Test
    void testRedis() {
        System.out.println(redisTemplate.opsForValue().get("ad2763027cb847aaac53319e69d80396"));
    }

    @Test
    void getUserInfo() {
        System.out.println(userService.getUserInfo("9b36ef9a79034b53b1c747a2068a945b").toString());
    }

    @Test
    void checkToken() {
        System.out.println(userService.checkToken("9b36ef9a79034b53b1c747a2068a945b").toString());
    }

    @Test
    void updateUser() {
        User user = new User();
        user.setGender(1);
        System.out.printf("", userService.updateUser("9b36ef9a79034b53b1c747a2068a945b", user).toString());
    }

    @Test
    void findByToken() {
        System.out.println(userService.findByToken("a297939a253d47a2afc411956b376f09"));
    }
}