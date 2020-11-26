package com.soft1851.user.service;

import com.soft1851.enums.Sex;
import com.soft1851.pojo.Fans;
import com.soft1851.pojo.vo.RegionRatioVO;

import java.util.List;

public interface FanService {
    Fans getFans(String writerId);

    boolean isMeFollowThisWriter(String writerId, String fanId);

    public void follow(String writerId,String fanId);

    void unfollow(String writerId,String fanId);

    Integer queryFansCounts(String writerId, Sex sex);

    List<RegionRatioVO> queryRegionRatioCounts(String writerId);
}
