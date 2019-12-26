package xyz.nadev.house.util;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.StorageClass;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import xyz.nadev.house.vo.ResponseVO;

import java.io.IOException;
import java.net.URL;
import java.util.Date;


@Slf4j
public class COSUtil {

    public static String accessKey;

    public static String secretKey;

    public static String region;

    public static String bucketName;


    // 图片 URL 有效期 二十年 毒奶?
    public static final Long EXPIRATION_ADD_TIME = 3600L * 1000 * 24 * 365 * 20;


    public static ResponseVO uploadImg(MultipartFile file, String imgType) {

        // 1 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials(accessKey, secretKey);
        // 2 设置bucket的区域, COS地域的简称请参照 https://www.qcloud.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region(region));
        // 3 生成cos客户端
        COSClient cosclient = new COSClient(cred, clientConfig);
        // bucket名需包含appid

        String key = imgType + "/" + file.getOriginalFilename();

        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            // 从输入流上传必须制定content length, 否则http客户端可能会缓存所有数据，存在内存OOM的情况
            objectMetadata.setContentLength(file.getSize());

            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(bucketName, key, file.getInputStream(), objectMetadata);
            // 设置存储类型, 默认是标准(Standard), 低频(standard_ia)
            putObjectRequest.setStorageClass(StorageClass.Standard);
            PutObjectResult putObjectResult = cosclient.putObject(putObjectRequest);
            // putobjectResult会返回文件的etag
            String etag = putObjectResult.getETag();
            Date expiration = new Date(new Date().getTime() + EXPIRATION_ADD_TIME);
            URL url = cosclient.generatePresignedUrl(bucketName, key, expiration);
            return ControllerUtil.getSuccessResultBySelf(url);
        } catch (CosServiceException e) {
            log.error("CosServiceException: {}", e.toString());
        } catch (CosClientException e) {
            log.error("CosClientException: {}", e.toString());
        } catch (IOException e) {
            log.error("IOException: {}", e.toString());
        }
        return ControllerUtil.getFalseResultMsgBySelf("上传失败");
    }

    public static void setAccessKey(String accessKey) {
        COSUtil.accessKey = accessKey;
    }

    public static void setSecretKey(String secretKey) {
        COSUtil.secretKey = secretKey;
    }

    public static void setRegion(String region) {
        COSUtil.region = region;
    }

    public static void setBucketName(String bucketName) {
        COSUtil.bucketName = bucketName;
    }

}
