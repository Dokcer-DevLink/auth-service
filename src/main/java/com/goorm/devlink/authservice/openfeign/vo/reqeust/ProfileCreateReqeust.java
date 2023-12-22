package com.goorm.devlink.authservice.openfeign.vo.reqeust;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileCreateReqeust {
    private String nickname;
}
