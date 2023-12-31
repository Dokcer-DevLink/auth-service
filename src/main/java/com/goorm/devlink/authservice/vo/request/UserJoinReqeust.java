package com.goorm.devlink.authservice.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserJoinReqeust {
    private String email;
    private String password;
    private String nickname;
}
