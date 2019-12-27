package xyz.nadev.house.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@SpringBootTest
class BrowseRepositoryTest {

    @Autowired
    BrowseRepository browseRepository;

    @Test
    void findBrowseByUserId() {
        Pageable pageable = PageRequest.of(0,10);
        System.out.println(browseRepository.findBrowseByUserId( 6, pageable));
    }
}