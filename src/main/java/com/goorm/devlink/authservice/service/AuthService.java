package com.goorm.devlink.authservice.service;

import com.goorm.devlink.authservice.dto.TokenDto;

public interface AuthService {

    TokenDto authorize(String email, String password);

    TokenDto reissue(String accessToken, String refreshToken);

    void logout(String accessToken, String refreshToken);

    boolean validateCheck(String email, String userUuid);
}
