package com.goorm.devlink.authservice.controller;

import com.goorm.devlink.authservice.dto.TokenDto;
import com.goorm.devlink.authservice.dto.UserDto;
import com.goorm.devlink.authservice.service.AuthService;
import com.goorm.devlink.authservice.service.UserService;
import com.goorm.devlink.authservice.vo.request.UserJoinReqeust;
import com.goorm.devlink.authservice.vo.request.UserLoginRequest;
import com.goorm.devlink.authservice.vo.request.UserModifyRequest;
import com.goorm.devlink.authservice.vo.response.TokenAndUserUuidResponse;
import com.goorm.devlink.authservice.vo.response.UserResponse;
import com.goorm.devlink.authservice.vo.response.UserValidatedResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
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
    public ResponseEntity<TokenAndUserUuidResponse> login(@RequestBody UserLoginRequest request) {
        TokenDto tokenDto = authService.authorize(request.getEmail(), request.getPassword());

        UserDto user = userService.getUserByEmail(request.getEmail());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenDto.getAccessToken());
        headers.set("refreshToken", tokenDto.getRefreshToken());
        headers.set("userUuid", user.getUserUuid());

        TokenAndUserUuidResponse response = TokenAndUserUuidResponse.builder()
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .userUuid(user.getUserUuid())
                .build();

        return ResponseEntity.ok()
                .headers(headers)
                .body(response);
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

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(
            @RequestHeader("Authorization") String accessToken,
            @RequestHeader("refreshToken") String refreshToken) {

        TokenDto tokenDto = authService.reissue(accessToken, refreshToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add("accessToken", tokenDto.getAccessToken());
        headers.add("refreshToken", tokenDto.getRefreshToken());

        return ResponseEntity.ok(tokenDto);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader("Authorization") String accessToken,
            @RequestHeader("refreshToken") String refreshToken) {

        authService.logout(accessToken, refreshToken);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/users")
    public ResponseEntity<Void> modifyUserInfo(Authentication authentication,
                                               @RequestHeader("userUuid") String userUuid,
                                               @RequestBody UserModifyRequest request) {
        userService.modifyUserinfo(authentication.getName(), userUuid, request.getNickname(), request.getPassword());

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/validate")
    public ResponseEntity<UserValidatedResponse> validateCheck(Authentication authentication,
                                                 @RequestHeader("userUuid") String userUuid) {

        boolean isActivated = authService.validateCheck(authentication.getName(), userUuid);

        return ResponseEntity.ok(new UserValidatedResponse(isActivated));
    }

    @DeleteMapping("/users")
    public ResponseEntity<Void> deleteUser(Authentication authentication,
                                           @RequestHeader("userUuid") String userUuid,
                                           @RequestHeader("Authorization") String accessToken,
                                           @RequestHeader("refreshToken") String refreshToken) {

        userService.deleteUser(authentication.getName(), userUuid);
        authService.logout(accessToken, refreshToken);

        return ResponseEntity.ok().build();
    }
}
