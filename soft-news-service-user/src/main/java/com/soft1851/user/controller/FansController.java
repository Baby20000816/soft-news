package com.soft1851.user.controller;

import com.soft1851.api.BaseController;
import com.soft1851.api.controller.user.FansControllerApi;
import com.soft1851.enums.Sex;
import com.soft1851.pojo.vo.FansCountsVO;
import com.soft1851.result.GraceResult;
import com.soft1851.user.service.FanService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FansController  extends BaseController implements FansControllerApi {
    public final FanService fanService;
    @Override
    public GraceResult isMeFollowThisWriter(String writerId, String fanId) {
        boolean result = fanService.isMeFollowThisWriter(writerId, fanId);
        return GraceResult.ok(result);
    }

    @Override
    public GraceResult follow(String writerId, String fanId) {
        fanService.follow(writerId, fanId);
        return GraceResult.ok();
    }

    @Override
    public GraceResult unfollow(String writerId, String fanId) {
        fanService.unfollow(writerId, fanId);
        return GraceResult.ok();
    }

    @Override
    public GraceResult queryRatio(String writerId) {
        int manCounts = fanService.queryFansCounts(writerId, Sex.man);
        int womanCounts = fanService.queryFansCounts(writerId,Sex.woman);
        FansCountsVO fansCountsVO = new FansCountsVO();
        fansCountsVO.setManCounts(manCounts);
        fansCountsVO.setWomanCounts(womanCounts);
        return GraceResult.ok(fansCountsVO);
    }

    @Override
    public GraceResult queryRatioByRegion(String writerId) {
        return GraceResult.ok(fanService.queryRegionRatioCounts(writerId));
    }
}
