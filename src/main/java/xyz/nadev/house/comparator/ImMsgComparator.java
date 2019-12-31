package xyz.nadev.house.comparator;

import xyz.nadev.house.Enum.ImMsgSortFieldEnum;
import xyz.nadev.house.entity.ImMsg;

import java.util.Comparator;

/**
 * 比较器 通过发送时间比较
 *
 */
public class ImMsgComparator implements Comparator<ImMsg> {

    private final static Integer LESS = -1;
    private final static Integer EQUAL = 0;
    private final static Integer BIG = 1;
    /**
     * 排序
     * ID 或者 时间
     * 默认通过ID排序
     */
    private static ImMsgSortFieldEnum type = ImMsgSortFieldEnum.ID;

    @Override
    public int compare(ImMsg o1, ImMsg o2) {
        if (type == ImMsgSortFieldEnum.ID){
            if (o1.getId() < o2.getId()){
                return this.LESS;
            }else if(o1.getId() == o2.getId()){
                return this.EQUAL;   //impossible
            }else {
                return this.BIG;
            }
        } else if (type == ImMsgSortFieldEnum.GMT_READ || type == ImMsgSortFieldEnum.GMT_READ){
            long o1t = (type == ImMsgSortFieldEnum.GMT_READ)?o1.getGmtRead().getTime():o1.getGmtSend().getTime();
            long o2t = (type == ImMsgSortFieldEnum.GMT_READ)?o2.getGmtRead().getTime():o2.getGmtSend().getTime();
            if (o1t < o2t){
                return LESS;
            }else if(o1t == o2t){
                return EQUAL;   //impossible
            }else {
                return BIG;
            }
        } else {
            // 默认按照ID排序
            if (o1.getId() < o2.getId()){
                return this.LESS;
            }else if(o1.getId() == o2.getId()){
                return this.EQUAL;   //impossible
            }else {
                return this.BIG;
            }
        }
    }
    @Override
    public boolean equals(Object obj) {
        return false;
    }
    public static ImMsgSortFieldEnum getType() {
        return type;
    }

    public static void setType(ImMsgSortFieldEnum type) {
        ImMsgComparator.type = type;
    }
}
