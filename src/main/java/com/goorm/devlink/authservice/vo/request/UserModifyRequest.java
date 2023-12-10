package com.goorm.devlink.authservice.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModifyRequest {

    private String nickname;
    private String password;
}
