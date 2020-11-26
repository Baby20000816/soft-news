package com.soft1851.user.service;

import com.soft1851.pojo.Fans;

public interface FanService {
    Fans getFans(String writerId);

    boolean isMeFollowThisWriter(String writerId, String fanId);

    public void follow(String writerId,String fanId);

    void unfollow(String writerId,String fanId);
}
