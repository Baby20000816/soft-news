package com.soft1851.user.controller;

import com.soft1851.api.BaseController;
import com.soft1851.api.controller.user.UserControllerApi;
import com.soft1851.pojo.AppUser;
import com.soft1851.pojo.Fans;
import com.soft1851.pojo.bo.UpdateUserInfoBO;
import com.soft1851.pojo.vo.AppUserVO;
import com.soft1851.pojo.vo.UserAccountInfoVo;
import com.soft1851.result.GraceResult;
import com.soft1851.result.ResponseStatusEnum;
import com.soft1851.user.mapper.AppUserMapper;
import com.soft1851.user.mapper.FansMapper;
import com.soft1851.user.service.FanService;
import com.soft1851.user.service.UserService;
import com.soft1851.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController extends BaseController implements UserControllerApi {
    private final UserService userService;
    @Resource
    private AppUserMapper appUserMapper;

    private final FanService fanService;
    @Resource
    private FansMapper fansMapper;

    @Override
    public GraceResult getAllUsers() {
        return GraceResult.ok(appUserMapper.selectAll());
    }

    @Override
    public GraceResult getUserInfo(String userId) {
        if (StringUtils.isBlank(userId)){
            return GraceResult.errorCustom(ResponseStatusEnum.UN_LOGIN);
        }
        AppUser user = getUser(userId);
        UserAccountInfoVo accountInfoVo = new UserAccountInfoVo();
        BeanUtils.copyProperties(user,accountInfoVo);
        return GraceResult.ok(accountInfoVo);
    }

    @Override
    public GraceResult updateUserInfo(@Valid UpdateUserInfoBO updateUserInfoBO, BindingResult result) {
        if (result.hasErrors()){
            Map<String,String> errorMap = getErrors(result);
            return GraceResult.errorMap(errorMap);
        }
        userService.updateUserInfo(updateUserInfoBO);
        return GraceResult.ok();
    }

    @Override
    public GraceResult getUserBasicInfo(String userId) {
        if (StringUtils.isBlank(userId))
        {
            return GraceResult.errorCustom(ResponseStatusEnum.UN_LOGIN);
        }
        AppUser user = getUser(userId);
        AppUserVO userVO = new AppUserVO();
        BeanUtils.copyProperties(user,userVO);
        return GraceResult.ok(userVO);
    }

    @Override
    public GraceResult getFansFollow(String writerId) {
        String fansJson = redis.get("粉丝:"+writerId);
        Fans fans;
        if (StringUtils.isNotBlank(fansJson)){
            fans = JsonUtil.jsonToPojo(fansJson,Fans.class);
        }else {
            fans = fanService.getFans(writerId);
            redis.set("粉丝:" + writerId, JsonUtil.objectToJson(fans), 1);
        }
        return GraceResult.ok(fans);
    }

    private AppUser getUser(String userId){
        String userJson = redis.get(REDIS_USER_INFO+":"+userId);
        AppUser user;
        if (StringUtils.isNotBlank(userJson)){
            user = JsonUtil.jsonToPojo(userJson,AppUser.class);
        }else {
            user = userService.getUser(userId);
            redis.set(REDIS_USER_INFO+":"+userId,JsonUtil.objectToJson(user),1);
        }
        return user;
    }

    @Override
    public GraceResult queryByIds(String userIds) {
        if (StringUtils.isBlank(userIds))
        {
            System.out.println("这里错了");
            return GraceResult.errorCustom(ResponseStatusEnum.USER_NOT_EXIST_ERROR);

        }
        List<AppUserVO> publisherList = new ArrayList<>();
        List<String> userIdList = JsonUtil.jsonToList(userIds, String.class);
        assert userIdList != null;
        for (String userId : userIdList){
            AppUserVO userVO = getBasicUserInfo(userId);
            publisherList.add(userVO);
        }
        System.out.println("这里");
        return GraceResult.ok(publisherList);
    }

    private AppUserVO getBasicUserInfo(String userId) {
        AppUser user = getUser(userId);
        System.out.println(userId);
        System.out.println(user);
        AppUserVO userVO = new AppUserVO();
        BeanUtils.copyProperties(user,userVO);
        return userVO;
    }


}