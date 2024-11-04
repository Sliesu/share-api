package com.rbc.share.content.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rbc.share.common.resp.CommonResp;
import com.rbc.share.content.domain.dto.ExchangeDTO;
import com.rbc.share.content.domain.dto.UserAddBonusMsgDTO;
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

    @Resource
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

    public Share exchange(ExchangeDTO exchangeDTO) {
        Long userId = exchangeDTO.getUserId();
        Long shareId = exchangeDTO.getShareId();

        // 1. 根据 id 查询 share，校验需要兑换的内容是否存在
        Share share = shareMapper.selectById(shareId);
        if (share == null) {
            throw new IllegalArgumentException("该分享内容不存在！");
        }

        // 2. 如果当前用户已经兑换过，则直接返回该分享内容（不需要扣积分）
        MidUserShare midUserShare = midUserShareMapper.selectOne(
                new QueryWrapper<MidUserShare>().lambda()
                        .eq(MidUserShare::getUserId, userId)
                        .eq(MidUserShare::getShareId, shareId)
        );
        if (midUserShare != null) {
            return share; // 用户已兑换过，直接返回分享内容
        }

        // 3. 判断用户积分是否足够兑换该内容
        CommonResp<User> commonResp = userService.getUser(userId);
        User user = commonResp.getData();
        Integer price = share.getPrice(); // 兑换这条内容需要的积分

        // 用户的积分不够
        if (price > user.getBonus()) {
            throw new IllegalArgumentException("用户积分不够！");
        }

        // 4. 修改积分：乘以 -1 变成负值，就是扣分
        userService.updateBonus(UserAddBonusMsgDTO.builder()
                .userId(userId)
                .bonus(price * -1)
                .build());

        // 5. 向 mid_user_share 表插入一条数据，记录这个用户已经兑换过，就拥有了下载权限（前端页面）
        midUserShareMapper.insert(MidUserShare.builder()
                .userId(userId)
                .shareId(shareId)
                .build());

        return share; // 返回分享内容
    }

}