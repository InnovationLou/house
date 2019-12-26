package xyz.nadev.house.repository;

import xyz.nadev.house.entity.Message;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer>{
    List<Message> findByGmtCreateBetween(Date oldDate,Date now);
}
