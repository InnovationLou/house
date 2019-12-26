package xyz.nadev.house.Enum;

public enum  PayItemType {
    cash(1), utilities(2), deposit(3);

    private int value;

    PayItemType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
