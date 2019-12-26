package xyz.nadev.house.repository.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import xyz.nadev.house.entity.ImMsg;
import xyz.nadev.house.repository.ImMsgRepository;

import javax.persistence.PostLoad;
import java.util.List;

/**
 *
 * 拦截修改消息为已读无效 , 暂时丢弃该类
 * 采用业务逻辑实现
 *
 */
@Deprecated
@Configurable
@Slf4j
public class ReadMsgListener {

    @Autowired
    ImMsgRepository imMsgRepository;

    /**
     *  读取消息的话消息要被做成已读状态
     * @param source
     */
    @PostLoad
    public void readMsg(Object source){
        System.out.println("憨憨" + source);
        ImMsg imMsg = null;
        List<ImMsg> msgs = null;
        try {
            imMsg = (ImMsg)source;
            System.out.println("憨憨1" + source);
        } catch (Exception e) {
            log.info("not a ImMsg boject" + e.getMessage() + source);
            try {
                msgs = (List<ImMsg>)source;
                System.out.println("憨憨2" + source);
            } catch (Exception e2) {
                log.info("not many immsgs:" + e2.getMessage() + source);
            }
        }
        if (null != imMsg){
            // 如果是未读消息, 直接设置成已读
            if (!imMsg.isRead()){
                imMsg.setRead(true);
                imMsgRepository.saveAndFlush(imMsg);
            }
        }else if (null != msgs){
            // 将其中未读消息全部找出来, 变成已读的
            for (ImMsg msg :
                    msgs) {
                if (!msg.isRead()) {
                    msg.setRead(true);
                }
            }
            imMsgRepository.saveAll(msgs);
        }

    }
}
