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
        userService.save(user);

        User newUser = new User();
        newUser.setOpenId("1X98M5D_n2lS0nsBAfYSDABoBmh1");
        newUser.setNickName("via");
        userService.save(newUser);
        System.out.println(userService.findById(1).toString());
    }

    @Test
    void login() {
        userService.login("033KP6S70M0DqF1nDdS70WdRR70KP6Sh");
    }

    @Test
    void testRedis() {
        System.out.println(redisTemplate.opsForValue().get("ad2763027cb847aaac53319e69d80396"));
    }
}