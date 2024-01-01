package com.goorm.devlink.authservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GitHubProfile {
    private String login;
    @JsonProperty("html_url")
    private String htmlUrl;
    private String email;
}
