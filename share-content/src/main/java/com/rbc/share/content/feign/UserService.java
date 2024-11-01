package com.rbc.share.content.feign;

import com.rbc.share.common.resp.CommonResp;
import com.rbc.share.content.domain.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author DingYihang
 */
@FeignClient(value = "user-service", path = "/user")
public interface UserService {

    @GetMapping("/{id}")
    CommonResp<User> getUser(@PathVariable Long id);
}