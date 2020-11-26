package com.soft1851.api.controller.user;

import com.soft1851.result.GraceResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Api(value = "粉丝管理",tags = {"粉丝管理"})
@RequestMapping("fans")
public interface FansControllerApi {
    @PostMapping("isMeFollowThisWriter")
    @ApiOperation(value = "查询当前用户是否关注作者",notes = "查询当前用户是否关注作者",httpMethod = "POST")
    GraceResult isMeFollowThisWriter(@RequestParam String writerId,@RequestParam String fanId);

    @PostMapping("follow")
    @ApiOperation(value = "关注作者，成为粉丝",notes = "关注作者，成为粉丝",httpMethod = "POST")
    GraceResult follow(@RequestParam String writerId,@RequestParam String fanId);

    @PostMapping("/unfollow")
    @ApiOperation(value = "取消关注，减少粉丝",notes = "取消关注，减少粉丝",httpMethod = "POST")
    GraceResult unfollow(@RequestParam String writerId,@RequestParam String fanId);
}
