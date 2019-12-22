package xyz.nadev.house.service;

import xyz.nadev.house.entity.Banner;
import xyz.nadev.house.vo.ResponseVO;

public interface BannerService {
    /**
     * 获取所有Banner
     * @return
     */
    ResponseVO getAllBanner();

    /**
     * 返回url对应的banner
     * @param url
     * @return
     */
    ResponseVO getBannerByUrl(String url);

    /**
     * 上传banner
     * @param banner
     * @return
     */
    ResponseVO uploadBanner(Banner banner);
}
