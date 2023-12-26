package com.goorm.devlink.authservice.controller;

import com.goorm.devlink.authservice.dto.TokenDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class GitHubController {

    @GetMapping("/home")
    public ResponseEntity home(@RequestParam String accessToken, @RequestParam String refreshToken) {
        TokenDto token = new TokenDto(accessToken, refreshToken);
        return ResponseEntity.ok(token);
    }
}
