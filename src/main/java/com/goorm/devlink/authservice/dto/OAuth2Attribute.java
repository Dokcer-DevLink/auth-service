package com.goorm.devlink.authservice.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class OAuth2Attribute {

    private Map<String, Object> attributes;
    private String attributeKey;
    private String email;
    private String nickname;
    private String githubUrl;

    private Long id;

    public static OAuth2Attribute of(String provider, String attributeKey,
                                     Map<String, Object> attributes) {
        switch (provider) {
            case "github":
                return ofGithub("id", attributes);
            default:
                throw new RuntimeException();
        }
    }

    private static OAuth2Attribute ofGithub(String attributeKey,
                                            Map<String, Object> attributes) {

        Integer id = (Integer) attributes.get("id");
        return OAuth2Attribute.builder()
                .id(Long.valueOf(id))
                .nickname((String) attributes.get("login"))
                .email((String) attributes.get("email"))
                .githubUrl((String) attributes.get("html_url"))
                .attributes(attributes)
                .attributeKey(attributeKey)
                .build();
    }

    public Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", attributeKey);
        map.put("key", attributeKey);
        map.put("nickname", nickname);
        map.put("email", email);
        map.put("url", githubUrl);
        map.put("id", id);
        return map;
    }
}
