package com.rbc.share.user.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.jwt.JWTUtil;
import com.rbc.share.common.exception.BusinessException;
import com.rbc.share.common.exception.BusinessExceptionEnum;
import com.rbc.share.common.util.JwtUtil;
import com.rbc.share.common.util.SnowUtil;
import com.rbc.share.user.domain.dto.LoginDTO;
import com.rbc.share.user.domain.dto.UserAddBonusMsgDTO;
import com.rbc.share.user.domain.entity.BonusEventLog;
import com.rbc.share.user.domain.entity.User;
import com.rbc.share.user.domain.resp.UserLoginResp;
import com.rbc.share.user.mapper.BonusEventLogMapper;
import com.rbc.share.user.mapper.UserMapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * UserService 处理用户业务逻辑
 * @author DingYihang
 */
@Slf4j
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private BonusEventLogMapper bonusEventLogMapper;

    /**
     * 统计用户数量
     * @return 用户数量
     */
    public Long count() {
        return userMapper.selectCount(null);
    }


    /**
     * 根据用户ID查询用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    public User findById(Long userId) {
        return userMapper.selectById(userId);
    }

    /**
     * 用户登录方法
     * @param loginDTO 包含登录信息的DTO
     * @return 登录成功的用户信息
     */
    public UserLoginResp login(LoginDTO loginDTO) {
        // 根据手机号查询用户
        User user = userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getPhone, loginDTO.getPhone()));

        // 用户不存在，抛出异常
        if (user == null) {
            throw new BusinessException(BusinessExceptionEnum.PHONE_NOT_EXIST);
        }

        // 密码错误，抛出异常
        if (!user.getPassword().equals(loginDTO.getPassword())) {
            throw new BusinessException(BusinessExceptionEnum.PASSWORD_ERROR);
        }

        //都正确，返回
        UserLoginResp userLoginResp = UserLoginResp.builder()
                .user(user)
                .build();
//        String key = "hello world";
//        Map<String,Object> map = BeanUtil.beanToMap(userLoginResp);
        String token = JwtUtil.createToken(userLoginResp.getUser().getId(),userLoginResp.getUser().getPhone());
        userLoginResp.setToken(token);
        return userLoginResp;
    }

    /**
     * 用户注册方法
     * @param loginDTO 包含用户注册信息的DTO
     * @return 注册成功的用户ID
     * @throws BusinessException 如果手机号已存在
     */
    public Long register(LoginDTO loginDTO) {
        // 根据手机号查询用户，检查手机号是否已存在
        User userDb = userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getPhone, loginDTO.getPhone()));

        // 如果手机号已存在，抛出业务异常
        if (userDb != null) {
            throw new BusinessException(BusinessExceptionEnum.PHONE_EXIST);
        }

        // 创建并构建新用户对象
        User saveUser = User.builder()
                .id(SnowUtil.getSnowflakeNextId())
                .phone(loginDTO.getPhone())
                .password(loginDTO.getPassword())
                .nickname("新用户")
                .avatarUrl("https://niit-soft.oss-cn-hangzhou.aliyuncs.com/avatar/8.jpg")
                .bonus(100)
                .createTime(new Date())
                .updateTime(new Date())
                .build();

        // 插入新用户记录到数据库
        userMapper.insert(saveUser);

        // 返回新注册用户的ID
        return saveUser.getId();
    }

    public void updateBonus(UserAddBonusMsgDTO userAddBonusMsgDTO) {
        // 1. 为用户修改积分
        Long userId = userAddBonusMsgDTO.getUserId();
        Integer bonus = userAddBonusMsgDTO.getBonus();

        User user = userMapper.selectById(userId);
        user.setBonus(user.getBonus() + bonus);
        userMapper.update(user, new QueryWrapper<User>().lambda().eq(User::getId, userId));

        // 2. 记录日志到 bonus_event_log 表
        bonusEventLogMapper.insert(BonusEventLog.builder()
                .userId(userId)
                .value(bonus)
                .event(userAddBonusMsgDTO.getEvent())
                .description(userAddBonusMsgDTO.getDescription())
                .createTime(new Date())
                .build());

        log.info("用户ID: {} 积分更新成功，增加积分: {}", userId, bonus);
    }
}
