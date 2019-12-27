package xyz.nadev.house.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.nadev.house.util.COSUtil;

//给Util类设置yml值
@Configuration
public class YamlConfig {

    @Value("${cos.access-key}")
    String accessKey;

    @Value("${cos.secret-key}")
    String secretKey;

    @Value("${cos.region}")
    String region;

    @Value("${cos.bucket-name}")
    String bucketName;

    @Value("${cos.domain}")
    String domain;

    @Bean
    public void initStatic() {
        COSUtil.setBucketName(bucketName);
        COSUtil.setAccessKey(accessKey);
        COSUtil.setRegion(region);
        COSUtil.setSecretKey(secretKey);
        if (!domain.endsWith("/"))
            domain += "/";
        COSUtil.setDomain(domain);
    }
}
