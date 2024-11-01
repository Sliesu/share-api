package com.rbc.share.user.controller;

import com.rbc.share.common.resp.CommonResp;
import com.rbc.share.user.domain.dto.LoginDTO;
import com.rbc.share.user.domain.entity.User;
import com.rbc.share.user.domain.resp.UserLoginResp;
import com.rbc.share.user.service.UserService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author DingYihang
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping("/count")
    public Long count() {
        return userService.count();
    }

    @PostMapping("/login")
    public CommonResp<UserLoginResp> login(@Valid @RequestBody LoginDTO loginDTO) {
        UserLoginResp userLoginResp = userService.login(loginDTO);
        CommonResp<UserLoginResp> resp = new CommonResp<>();
        resp.setData(userLoginResp);
        return resp;
    }

    @PostMapping("/register")
    public CommonResp<Long> register(@Valid @RequestBody LoginDTO loginDTO) {
        Long id = userService.register(loginDTO);
        CommonResp<Long> resp = new CommonResp<>();
        resp.setData(id);
        return resp;
    }

    @GetMapping("/{id}")
    public CommonResp<User> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        CommonResp<User> resp = new CommonResp<>();
        resp.setData(user);
        return resp;
    }
}
