package com.goorm.devlink.authservice.controller;

import com.goorm.devlink.authservice.dto.TokenDto;
import com.goorm.devlink.authservice.dto.UserDto;
import com.goorm.devlink.authservice.service.AuthService;
import com.goorm.devlink.authservice.service.UserService;
import com.goorm.devlink.authservice.vo.request.UserJoinReqeust;
import com.goorm.devlink.authservice.vo.request.UserLoginRequest;
import com.goorm.devlink.authservice.vo.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth-service/api")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/join")
    public ResponseEntity<Void> signUp(@RequestBody UserJoinReqeust reqeust) {
        UserDto userDto = new ModelMapper().map(reqeust, UserDto.class);
        userService.join(userDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody UserLoginRequest request) {
        TokenDto tokenDto = authService.authorize(request.getEmail(), request.getPassword());
        HttpHeaders headers = new HttpHeaders();
        headers.add("accessToken", tokenDto.getAccessToken());
        headers.add("refreshToken", tokenDto.getRefreshToken());

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @GetMapping("/users/{userUuid}")
    public ResponseEntity<UserResponse> getUserByUserUuid(@PathVariable String userUuid) {
        UserDto userDto = userService.getUserByUserUuid(userUuid);
        UserResponse userResponse = new ModelMapper().map(userDto, UserResponse.class);

        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getUsers() {
        List<UserDto> users = userService.getUsers();

        ModelMapper mapper = new ModelMapper();
        List<UserResponse> userResponseList = new ArrayList<>();
        users.forEach(u -> {
            userResponseList.add(mapper.map(u, UserResponse.class));
        });

        return ResponseEntity.ok(userResponseList);
    }
}
