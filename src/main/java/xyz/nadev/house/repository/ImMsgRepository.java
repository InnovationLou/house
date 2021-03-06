package xyz.nadev.house.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.nadev.house.entity.ImMsg;

import java.util.List;

@Repository
public interface ImMsgRepository extends JpaRepository<ImMsg, Integer> {

    List<ImMsg> findAllByReadFalseAndReceiverIdAndSenderId(int receiverId, int senderId);

    List<ImMsg> findAllBySenderIdAndReceiverId(int senderId, int rcvId);

    List<ImMsg> findAllByReceiverId(int rcvId);

    List<ImMsg> findAllBySenderId(int senderId);

    void deleteAllBySenderIdAndReceiverId(int senderId, int receiverId);
}
