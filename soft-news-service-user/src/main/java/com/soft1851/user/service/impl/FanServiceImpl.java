package com.soft1851.user.service.impl;

import com.soft1851.pojo.Fans;
import com.soft1851.user.mapper.FansMapper;
import com.soft1851.user.service.FanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FanServiceImpl implements FanService {
    public final FansMapper fansMapper;
    @Override
    public Fans getFans(String writerId) {
        return fansMapper.selectByPrimaryKey(writerId);
    }
}
