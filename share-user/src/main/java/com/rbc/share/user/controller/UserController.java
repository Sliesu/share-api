package com.rbc.share.user.controller;

import com.rbc.share.common.resp.CommonResp;
import com.rbc.share.user.domain.dto.LoginDTO;
import com.rbc.share.user.domain.dto.UserAddBonusMsgDTO;
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

    @PostMapping("/updateBonus")
    public CommonResp<User> updateBonus(@RequestBody UserAddBonusMsgDTO userAddBonusMsgDTO) {
        Long userId = userAddBonusMsgDTO.getUserId();

        // 更新用户积分
        userService.updateBonus(UserAddBonusMsgDTO.builder()
                .userId(userId)
                .bonus(userAddBonusMsgDTO.getBonus())
                .description(userAddBonusMsgDTO.getDescription())
                .event(userAddBonusMsgDTO.getEvent())
                .build());

        // 获取更新后的用户信息
        CommonResp<User> resp = new CommonResp<>();
        resp.setData(userService.findById(userId));

        return resp;
    }

}
