package xyz.nadev.house.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import xyz.nadev.house.util.constant.RespCode;
import xyz.nadev.house.vo.ResponseVO;

/**
 * 这个类封装着一些控制器中公共的代码块
 */
@Component
public class ControllerUtil {

    Logger logger = LoggerFactory.getLogger(ControllerUtil.class);

    /**
     * 代码这个东西浓缩就是精华
     *
     * @param rs 真真假假
     * @return 假假真真
     */
    public static ResponseVO getTrueOrFalseResult(boolean rs) {
        ResponseVO ResponseVO = new ResponseVO();
        if (rs)
            ResponseVO.success(null);
        else
            ResponseVO.error(RespCode.FAILURE_CODE, "方法执行时返回了false", "");
        return ResponseVO;
    }

    /**
     * 浓缩就是精华
     *
     * @param data 缩阴功
     * @param <T>  缩阴功
     * @return 缩阴功的结果
     */
    public static <T> ResponseVO<T> getDataResult(T data) {
        ResponseVO<T> ResponseVO = new ResponseVO<>();
        if (null != data)
            ResponseVO.success(data);
        else
            ResponseVO.error(RespCode.FAILURE_CODE, "ControllerUtil.getDataResult获得空的数据", null);
        return ResponseVO;
    }

    public static <T> ResponseVO<T> getSuccessResultBySelf(T data) {
        ResponseVO ResponseVO = new ResponseVO<>();
        ResponseVO.success(data);
        return ResponseVO;
    }

    public static <T> ResponseVO<T> getFalseResultMsgBySelf(String msg) {
        ResponseVO ResponseVO = new ResponseVO<>();
        ResponseVO.error(RespCode.FAILURE_CODE, msg, null);
        return ResponseVO;
    }

}
