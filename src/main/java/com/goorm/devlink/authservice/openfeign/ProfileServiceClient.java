package com.goorm.devlink.authservice.openfeign;

import com.goorm.devlink.authservice.openfeign.vo.reqeust.ProfileCreateReqeust;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "profile-service")
public interface ProfileServiceClient {

    @PostMapping("/api/myprofile")
    ResponseEntity<Void> createProfile(@RequestBody ProfileCreateReqeust reqeust,
                                       @RequestHeader("userUuid") String userUuid);

    @DeleteMapping("/api/myprofile")
    ResponseEntity<Void> deleteMeProfile(@RequestHeader("userUuid") String userUuid);
}
