package xyz.nadev.house.controller.advice;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import xyz.nadev.house.util.ControllerUtil;
import xyz.nadev.house.vo.ResponseVO;

@RestControllerAdvice
public class MyException {
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseVO handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        return ControllerUtil.getFalseResultMsgBySelf("文件大小不得超过5MB");
    }
}
