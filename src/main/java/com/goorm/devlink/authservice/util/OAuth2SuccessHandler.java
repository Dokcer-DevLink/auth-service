package com.goorm.devlink.authservice.util;

import com.goorm.devlink.authservice.dto.TokenDto;
import com.goorm.devlink.authservice.dto.UserDto;
import com.goorm.devlink.authservice.jwt.TokenProvider;
import com.goorm.devlink.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();

        log.info("Principal에서 꺼낸 OAuth2User = {}", oAuth2User);

        UserDto userDto = UserDto.builder()
                .email(oAuth2User.getAttribute("email"))
                .nickname(oAuth2User.getAttribute("nickname"))
                .password("")
                .build();

        // 회원가입 로직
        userService.joinForGitHub(userDto, oAuth2User.getAttribute("url"));

        // JWT 토큰 발행 로직
        log.info("토큰 발행 시작");
        String authorities = getAuthorities(authentication);
        TokenDto token = tokenProvider.createToken(oAuth2User.getAttribute("email"), authorities);

        log.info("token = {}", token);
        // TODO: Front와 작업 및 합의 해서 리다이랙션 URI 수정
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("/home")
                .queryParam("accessToken", token.getAccessToken())
                .queryParam("refreshToken", token.getRefreshToken());
        String redirectUrl = uriBuilder.toUriString();
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    // 권한을 가져오는 메소드
    public String getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }
}
