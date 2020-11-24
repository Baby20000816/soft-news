package com.soft1851.admin.repository;

import com.soft1851.pojo.mo.FriendLinkMO;
import io.swagger.models.auth.In;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.util.List;

public interface FriendLinkRepository extends MongoRepository<FriendLinkMO,String> {
    List<FriendLinkMO> getAllByIsDelete(Integer isDelete);
}
