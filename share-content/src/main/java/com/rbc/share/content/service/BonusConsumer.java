package com.rbc.share.content.service;

import com.rbc.share.content.domain.dto.UserAddBonusMsgDTO;
import com.rbc.share.content.domain.entity.User;
import com.rbc.share.content.feign.UserService;
import lombok.AllArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * @author DingYihang
 */
@Service
@RocketMQMessageListener(topic = "BonusTopic_dyh",selectorExpression = "ShareApproved_dyh",consumerGroup = "bonus_group_dyh")
@AllArgsConstructor
public class BonusConsumer implements RocketMQListener<String> {

    private final UserService userService;

    @Override
    public void onMessage(String s) {
        String [] parts = s.split(":");
        Long userId = Long.parseLong(parts[0]);
        int bonus = Integer.parseInt(parts[1]);
        User user = userService.getUser(userId).getData();


        // 更新用户积分
        userService.updateBonus(UserAddBonusMsgDTO.builder()
                .userId(userId)
                .bonus(bonus)
                .description("审核通过，获得积分")
                .event("REWARDS")
                .build());
    }
}
