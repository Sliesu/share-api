package com.rbc.share.content.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rbc.share.common.resp.CommonResp;
import com.rbc.share.content.domain.entity.MidUserShare;
import com.rbc.share.content.domain.entity.Share;
import com.rbc.share.content.domain.entity.User;
import com.rbc.share.content.domain.resp.ShareResp;
import com.rbc.share.content.feign.UserService;
import com.rbc.share.content.mapper.MidUserShareMapper;
import com.rbc.share.content.mapper.ShareMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author DingYihang
 */
@Service
public class ShareService {

    @Resource
    private ShareMapper shareMapper;

    private UserService userService;

    @Resource
    private MidUserShareMapper midUserShareMapper;

    public List<Share> getList(String title, Integer pageNo, Integer pageSize, Long userId) {
        // 构造查询条件
        LambdaQueryWrapper<Share> wrapper = new LambdaQueryWrapper<>();
        // 按照 id 降序查询所有数据
        wrapper.orderByDesc(Share::getId);

        // 如果标题的关键字不空，则加上模糊查询条件过滤查询结果，否则结果就是所有数据
        if (title != null) {
            wrapper.like(Share::getTitle, title);
        }

        // 过滤出所有已经通过审核并显示属性为 true 的数据
        wrapper.eq(Share::getAuditStatus, "PASS").eq(Share::getShowFlag, true);

        // 按条件查询
        Page<Share> page = Page.of(pageNo,pageSize);
        List<Share> shares = shareMapper.selectList(page,wrapper);
        List<Share> sharesDeal;

        // 1. 如果用户没有登录，那么 downloadUrl 全部设为 null
        if (userId != null) {
            sharesDeal = shares.stream()
                    .peek(share -> share.setDownloadUrl(null))
                    .toList();
        } else {
            // 2. 如果用户登录了，查询 mid_user_share 表
            sharesDeal = shares.stream()
                    .peek(share -> {
                        MidUserShare midUserShare = midUserShareMapper.selectOne(
                                new QueryWrapper<MidUserShare>().lambda()
                                        .eq(MidUserShare::getUserId, userId)
                                        .eq(MidUserShare::getShareId, share.getId())
                        );
                        if (midUserShare == null) {
                            share.setDownloadUrl(null);
                        }
                    })
                    .toList();
        }

        return sharesDeal;
    }

    public ShareResp findById(Long shareId) {
        Share share = shareMapper.selectById(shareId);

        // 调用 feign 方法，根据用户 id 查询到用户信息
        CommonResp<User> commonResp = userService.getUser(share.getUserId());

        return ShareResp.builder()
                .share(share)
                .nickname(commonResp.getData().getNickname())
                .avatarUrl(commonResp.getData().getAvatarUrl())
                .build();
    }
}