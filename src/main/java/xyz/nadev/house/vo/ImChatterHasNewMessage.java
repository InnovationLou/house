package xyz.nadev.house.vo;

import lombok.Data;
import xyz.nadev.house.entity.ImMsg;

/**
 * 聊天者, 包含聊天者的信息 、 是否有新消息 、 新消息是什么
 */
@Data
public class ImChatterHasNewMessage {
    // chatter id
    private int id;

    private String nickName;

    private String imgUrl;

    private String city;

    private boolean landLord;

    private boolean auth;

    private boolean hasNewMessage = false;

    //消息条数
    private Integer messageNumber = 0;

    //最近100条聊天记录
    ImMsg latestMessage;

    public boolean getLandLord() {
        return landLord;
    }

    public boolean getAuth() {
        return auth;
    }

    public boolean getHasNewMessage() {
        return hasNewMessage;
    }

}
