package com.goorm.devlink.authservice.dto;

import com.goorm.devlink.authservice.entity.constant.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OauthUserDto {
    private Long id;
    private String email;
    private String nickname;
    private String userUuid;
    private UserRole userRole;
}
