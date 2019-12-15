package xyz.nadev.house.repository;

import xyz.nadev.house.entity.Message;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer>{

}
