package xyz.nadev.house.vo;

import xyz.nadev.house.entity.ImMsg;

import java.util.List;

/**
 * 聊天者信息和其最近的100条信息
 */
public class ImChatterWithLastest100MsgsVO {

    // chatter id
    private int id;

    private String nickName;

    private String city;

    private boolean landLord;

    private boolean auth;

    //最近100条聊天记录
    List<ImMsg> latest100Msgs;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isLandLord() {
        return landLord;
    }
    //hand write for entity util use.
    public boolean getLandLord() {
        return landLord;
    }

    public void setLandLord(boolean landLord) {
        this.landLord = landLord;
    }

    public boolean isAuth() {
        return auth;
    }
    //hand write for entity util use.
    public boolean getAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public List<ImMsg> getLatest100Msgs() {
        return latest100Msgs;
    }

    public void setLatest100Msgs(List<ImMsg> latest100Msgs) {
        this.latest100Msgs = latest100Msgs;
    }
}
