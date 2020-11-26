package com.soft1851.user.service.impl;

import com.soft1851.api.service.BaseService;
import com.soft1851.pojo.AppUser;
import com.soft1851.pojo.Fans;
import com.soft1851.user.mapper.FansMapper;
import com.soft1851.user.service.FanService;
import com.soft1851.user.service.UserService;
import com.soft1851.utils.RedisOperator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

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
}
