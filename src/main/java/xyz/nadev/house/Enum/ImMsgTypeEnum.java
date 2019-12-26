package xyz.nadev.house.Enum;

public enum ImMsgTypeEnum {

    PRODUCT("product"), MESSAGE("message");

    private String value;

    ImMsgTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
