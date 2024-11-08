package com.rbc.share.content.service;

import jakarta.annotation.Resource;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Service;

/**
 * @author DingYihang
 */
@Service
public class BonusProducer {
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    public void sendPointsMessage(Long userId,int bonus) {
        rocketMQTemplate.convertAndSend("BonusTopic_dyh:ShareApproved_dyh", userId + ":" + bonus);
    }
}
