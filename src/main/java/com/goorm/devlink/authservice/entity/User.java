package com.goorm.devlink.authservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.goorm.devlink.authservice.dto.GitHubProfile;
import com.goorm.devlink.authservice.dto.OAuthInfo;
import com.goorm.devlink.authservice.entity.constant.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "is_deleted=false and activated=true")
public class User extends AuditingFields {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "email", length = 50, unique = true)
    private String email;

    @JsonIgnore
    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "nickname", length = 50)
    private String nickname;

    @JsonIgnore
    @Column(name = "activated", columnDefinition = "boolean default true NOT NULL")
    private boolean activated;

    @Column(name = "user_uuid")
    private String userUuid;

    @Column(name = "is_deleted", columnDefinition = "boolean default false NOT NULL")
    private boolean isDeleted;

//    @ManyToMany
//    @JoinTable(
//            name = "user_authority",
//            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
//            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
//    private Set<Authority> authorities;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    public void modifyUserInfo(String nickname, String password) {
        if(nickname != null) {
            this.nickname = nickname;
        }

        if(password != null) {
            this.password = password;
        }
    }

    public void changeDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void updateGithubInfo(GitHubProfile githubProfile, String password) {
        this.email = githubProfile.getEmail();
        this.nickname = githubProfile.getLogin();
        this.password = password;
    }
}
