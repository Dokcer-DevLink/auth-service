package com.goorm.devlink.authservice.service;

import com.goorm.devlink.authservice.dto.UserDto;
import com.goorm.devlink.authservice.entity.User;
import com.goorm.devlink.authservice.vo.request.UserJoinReqeust;

public interface UserService {

    void join(UserDto userDto);
}
