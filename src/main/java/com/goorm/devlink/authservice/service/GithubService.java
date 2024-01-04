package com.goorm.devlink.authservice.service;

import com.goorm.devlink.authservice.dto.GitHubProfile;
import com.goorm.devlink.authservice.dto.OAuthInfo;
import com.goorm.devlink.authservice.dto.TokenDto;
import com.goorm.devlink.authservice.dto.UserDto;
import com.goorm.devlink.authservice.entity.User;
import com.goorm.devlink.authservice.repository.UserRepository;
import com.goorm.devlink.authservice.vo.response.GithubInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GithubService {

    @Value("${github.client-id}")
    private String clientId;
    @Value("${github.redirect-uri}")
    private String redirectUri;
    @Value("${github.client-secret}")
    private String clientSecret;

    private final String TOKEN_REQEUST_URL = "https://github.com/login/oauth/access_token";
    private final String PROFILE_REQEUST_URL = "https://api.github.com/user";

    private final UserRepository userRepository;
    private final UserService userService;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    public GithubInfoResponse getGithubInfo() {
        return GithubInfoResponse.builder()
                .clientId(clientId)
                .redirectUri(redirectUri)
                .build();
    }

    public OAuthInfo githubLogin(String code) {
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.exchange(TOKEN_REQEUST_URL,
                HttpMethod.POST,
                getAccessToken(code),
                OAuthInfo.class).getBody();
    }

    public GitHubProfile getGithubProfile(OAuthInfo oAuthInfo) {
        RestTemplate restTemplate = new RestTemplate();
         return restTemplate.exchange(PROFILE_REQEUST_URL,
                HttpMethod.GET,
                getProfileRequestEntity(oAuthInfo),
                GitHubProfile.class).getBody();
    }

//    @Transactional
    public TokenDto gitJoinAndLogin(GitHubProfile githubProfile, OAuthInfo oAuthInfo) {
        Optional<User> userOptional = userRepository.findByEmail(githubProfile.getEmail());

        if(userOptional.isEmpty()) {
            UserDto userDto = UserDto.builder()
                    .email(githubProfile.getEmail())
                    .nickname(githubProfile.getLogin())
                    .password(oAuthInfo.getAccessToken())
                    .build();

            userService.joinForGitHub(userDto, githubProfile.getHtmlUrl());
        } else {
            User user = userOptional.get();
            user.updateGithubInfo(githubProfile, passwordEncoder.encode(oAuthInfo.getAccessToken()));
//            userRepository.save(user);
            userService.userUpdate(user);
        }

        return authService.authorize(githubProfile.getEmail(), oAuthInfo.getAccessToken());
    }

    private HttpEntity<MultiValueMap<String, String>> getAccessToken(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("code", code);

        HttpHeaders headers = new HttpHeaders();
        return new HttpEntity<>(params, headers);
    }

    private HttpEntity<MultiValueMap<String, String>> getProfileRequestEntity(OAuthInfo oAuthInfo) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "token " + oAuthInfo.getAccessToken());
        return new HttpEntity<>(headers);
    }
}
