package xyz.nadev.house.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.nadev.house.entity.Banner;
import xyz.nadev.house.service.BannerService;
import xyz.nadev.house.repository.BannerRepository;
import xyz.nadev.house.util.ControllerUtil;
import xyz.nadev.house.vo.ResponseVO;

@Service
public class BannerServiceImpl implements BannerService {
	@Autowired
	private BannerRepository resp;

	@Override
	public ResponseVO getAllBanner() {
		return ControllerUtil.getDataResult(resp.findAll());
	}

	@Override
	public ResponseVO getBannerByUrl(String url) {
		Banner banner =resp.findBannerByImgUrl(url);
		if(banner == null ){
			return ControllerUtil.getFalseResultMsgBySelf("未找到指定url的banner:"+url);
		}
		return ControllerUtil.getDataResult(resp.findBannerByImgUrl(url));
	}

	@Override
	public ResponseVO uploadBanner(Banner banner) {
		return ControllerUtil.getSuccessResultBySelf(resp.save(banner));
	}
}
