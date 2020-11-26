package com.soft1851.pojo.vo;

import com.soft1851.pojo.AppUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppUserVO {
    private String id;
    private String nickname;
    private String face;
    private Integer activeStatus;
    private Integer myFollowCounts;
    private Integer myFansCounts;
    private AppUser user;
}
