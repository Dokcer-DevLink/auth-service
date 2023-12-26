package com.goorm.devlink.authservice.service;

import com.goorm.devlink.authservice.dto.UserDto;
import com.goorm.devlink.authservice.entity.constant.JoinType;

import java.util.List;

public interface UserService {

    void join(UserDto userDto, JoinType joinType);

    UserDto getUserByUserUuid(String userUuid);

    UserDto getUserByEmail(String email);

    List<UserDto> getUsers();

    void modifyUserinfo(String email, String userUuid, String nickname, String password);

    void deleteUser(String email, String userUuid);
}
