package xyz.nadev.house.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xyz.nadev.house.util.COSUtil;
import xyz.nadev.house.vo.ResponseVO;

@Slf4j
@RequestMapping("/image")
@RestController
public class ImageController {

    @ApiOperation("上传图片 返回url imgType: house")
    @PostMapping("")
    public ResponseVO uploadImg(MultipartFile file, String imgType)  {
        return COSUtil.uploadImg(file, imgType);
    }

}
