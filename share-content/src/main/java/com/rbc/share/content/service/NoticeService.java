package com.rbc.share.content.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rbc.share.content.domain.entity.Notice;
import com.rbc.share.content.mapper.NoticeMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author DingYihang
 */
@Service
public class NoticeService {

    @Resource
    private NoticeMapper noticeMapper;

    public Notice getLatest() {
        LambdaQueryWrapper<Notice> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Notice::getCreateTime)  // 按创建时间降序排序
                .last("LIMIT 1");  // 只获取最新的一条记录

        return noticeMapper.selectOne(queryWrapper);
    }
}