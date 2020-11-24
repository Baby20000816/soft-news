package com.soft1851.admin.service;

import com.soft1851.pojo.mo.FriendLinkMO;

import java.util.List;

public interface FriendLinkService {
    void saveOrUpdateFriendLink(FriendLinkMO friendLinkMO);

    List<FriendLinkMO> queryAllFriendLinkList();
}
