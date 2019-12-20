package xyz.nadev.house.Enum;

public enum HouseType {
    ONE_ROOM(1), TWO_ROOM(2), TREE_ROOM(3);

    private int value;

    HouseType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
