package xyz.nadev.house.comparator;

import xyz.nadev.house.entity.ImMsg;

import java.util.Comparator;

/**
 * 比较器 通过发送时间比较
 *
 */
public class ImMsgComparator implements Comparator<ImMsg> {


    @Override
    public int compare(ImMsg o1, ImMsg o2) {
        long o1t = o1.getGmtSend().getTime();
        long o2t = o2.getGmtSend().getTime();
        if (o1t < o2t){
            return -1;
        }else if(o1t == o2t){
            return 0;   //impossible
        }else {
            return 0;
        }
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }
}
