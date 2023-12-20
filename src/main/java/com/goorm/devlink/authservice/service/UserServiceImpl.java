package com.goorm.devlink.authservice.service;

import com.goorm.devlink.authservice.dto.UserDto;
import com.goorm.devlink.authservice.entity.User;
import com.goorm.devlink.authservice.entity.constant.UserRole;
import com.goorm.devlink.authservice.exception.AuthServiceException;
import com.goorm.devlink.authservice.exception.ErrorCode;
import com.goorm.devlink.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void join(UserDto userDto) {
        if(userRepository.findByEmail(userDto.getEmail()).orElse(null) != null) {
            throw new AuthServiceException(ErrorCode.DUPLICATED_USER_EMAIL);
        }

        User user = User.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .userUuid(UUID.randomUUID().toString())
                .role(UserRole.USER)
                .activated(true)
                .build();

        userRepository.save(user);
    }

    @Override
    public UserDto getUserByUserUuid(String userUuid) {
        User user = userRepository.findByUserUuid(userUuid).orElseThrow(() ->
            new AuthServiceException(ErrorCode.USER_NOT_FOUND));

        return new ModelMapper().map(user, UserDto.class);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new AuthServiceException(ErrorCode.USER_NOT_FOUND));

        return new ModelMapper().map(user, UserDto.class);
    }

    @Override
    public List<UserDto> getUsers() {
        List<User> userList = userRepository.findAll();

        ModelMapper mapper = new ModelMapper();

        List<UserDto> userDtoList = new ArrayList<>();
        userList.forEach(u -> {
            userDtoList.add(mapper.map(u, UserDto.class));
        });

        return userDtoList;
    }

    @Override
    public void modifyUserinfo(String email, String userUuid, String nickname, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new AuthServiceException(ErrorCode.USER_NOT_FOUND));

        if(!user.getUserUuid().equals(userUuid)) {
            throw new AuthServiceException(ErrorCode.INVALID_USER_UUID);
        }

        user.modifyUserInfo(nickname, passwordEncoder.encode(password));

        userRepository.save(user);
    }

    @Override
    public void deleteUser(String email, String userUuid) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new AuthServiceException(ErrorCode.USER_NOT_FOUND));

        if(!user.getUserUuid().equals(userUuid)) {
            throw new AuthServiceException(ErrorCode.INVALID_USER_UUID);
        }

        user.changeDeleted(true);
        userRepository.save(user);
    }
}
