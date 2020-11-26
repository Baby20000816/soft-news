package com.soft1851.user.service.impl;

import com.soft1851.api.service.BaseService;
import com.soft1851.enums.Sex;
import com.soft1851.pojo.AppUser;
import com.soft1851.pojo.Fans;
import com.soft1851.pojo.vo.RegionRatioVO;
import com.soft1851.user.mapper.FansMapper;
import com.soft1851.user.service.FanService;
import com.soft1851.user.service.UserService;
import com.soft1851.utils.RedisOperator;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FanServiceImpl extends BaseService implements FanService{
    public final FansMapper fansMapper;
    private final Sid sid;
    private final UserService userService;
    private final RedisOperator redis;
    @Override
    public Fans getFans(String writerId) {
        return fansMapper.selectByPrimaryKey(writerId);
    }

    @Override
    public boolean isMeFollowThisWriter(String writerId, String fanId) {
        Fans fans = new Fans();
        fans.setFanId(fanId);
        fans.setWriterId(writerId);
        int count = fansMapper.selectCount(fans);
        return count>0;
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void follow(String writerId, String fanId) {
        AppUser fanInfo = userService.getUser(fanId);
        String fanPkId = sid.nextShort();
        Fans fan = new Fans();
        fan.setId(fanPkId);
        fan.setFanId(fanId);
        fan.setFace(fanInfo.getFace());
        fan.setWriterId(writerId);
        fan.setFanNickname(fanInfo.getNickname());
        fan.setProvince(fanInfo.getProvince());
        fan.setSex(fanInfo.getSex());
        fansMapper.insert(fan);
        redis.increment(REDIS_WRITER_FANS_COUNTS+":"+writerId,1);
        redis.increment(REDIS_MY_FOLLOW_COUNTS+":"+fanId,1);
    }

    @Override
    public void unfollow(String writerId, String fanId) {
        Fans fans = new Fans();
        fans.setWriterId(writerId);
        fans.setFanId(fanId);
        fansMapper.delete(fans);
        redis.decrement(REDIS_WRITER_FANS_COUNTS+":"+writerId,1);
        redis.decrement(REDIS_MY_FOLLOW_COUNTS+":"+fanId,1);
    }

    @Override
    public Integer queryFansCounts(String writerId, Sex sex) {
        Fans fans = new Fans();
        fans.setWriterId(writerId);
        fans.setSex(sex.type);
        return fansMapper.selectCount(fans);
    }

    public static final String[] REGIONS = {"北京", "天津", "上海", "重庆",
            "河北", "山西", "辽宁", "吉林", "黑龙江", "江苏", "浙江", "安徽", "福建", "江西", "山东",
            "河南", "湖北", "湖南", "广东", "海南", "四川", "贵州", "云南", "陕西", "甘肃", "青海", "台湾",
            "内蒙古", "广西", "西藏", "宁夏", "新疆",
            "香港", "澳门"};

    @Override
    public List<RegionRatioVO> queryRegionRatioCounts(String writerId) {
        Fans fans = new Fans();
        fans.setWriterId(writerId);
        List<RegionRatioVO>list = new ArrayList<>();
        for (String r:REGIONS){
            fans.setProvince(r);
            Integer count  = fansMapper.selectCount(fans);
            RegionRatioVO regionRatioVO = new RegionRatioVO();
            regionRatioVO.setName(r);
            regionRatioVO.setValue(count);
            list.add(regionRatioVO);
        }
        return list;
    }
}
