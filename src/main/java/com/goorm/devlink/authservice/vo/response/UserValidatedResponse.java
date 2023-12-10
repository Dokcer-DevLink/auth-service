package com.goorm.devlink.authservice.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserValidatedResponse {
    private boolean isActivated;
}
