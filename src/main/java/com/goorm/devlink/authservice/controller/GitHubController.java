package com.goorm.devlink.authservice.controller;

import com.goorm.devlink.authservice.dto.GitHubProfile;
import com.goorm.devlink.authservice.dto.OAuthInfo;
import com.goorm.devlink.authservice.dto.TokenDto;
import com.goorm.devlink.authservice.service.GithubService;
import com.goorm.devlink.authservice.vo.response.GithubInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GitHubController {

    private final GithubService githubService;

    @GetMapping("/api/login/github")
    public ResponseEntity<GithubInfoResponse> getGithubInfo() {
        GithubInfoResponse response = githubService.getGithubInfo();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/auth/github/callback")
    public ResponseEntity<TokenDto> githubLogin(@RequestParam String code) {
        OAuthInfo oAuthInfo = githubService.githubLogin(code);
        GitHubProfile githubProfile = githubService.getGithubProfile(oAuthInfo);

        TokenDto token = githubService.gitJoinAndLogin(githubProfile, oAuthInfo);
        return ResponseEntity.ok(token);
    }
}
