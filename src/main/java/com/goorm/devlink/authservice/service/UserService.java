package com.goorm.devlink.authservice.service;

import com.goorm.devlink.authservice.dto.UserDto;

import java.util.List;

public interface UserService {

    void join(UserDto userDto);

    UserDto getUserByUserUuid(String userUuid);

    List<UserDto> getUsers();

    void modifyUserinfo(String email, String userUuid, String nickname, String password);

    void deleteUser(String email, String userUuid);
}
