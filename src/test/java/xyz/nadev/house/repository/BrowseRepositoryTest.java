package xyz.nadev.house.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BrowseRepositoryTest {

    @Autowired
    BrowseRepository browseRepository;

    @Test
    void findBrowseByUserId() {
        Pageable pageable = PageRequest.of(2,10);
        System.out.println("结果为："+browseRepository.findBrowseByUserId(6, pageable));

    }
}