package com.rbc.share.content.domain.resp;

import com.rbc.share.content.domain.entity.Share;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author DingYihang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShareResp {

    /**
     * 分享的内容
     */
    private Share share;

    /**
     * 发布者的昵称
     */
    private String nickname;

    /**
     * 发布者的头像
     */
    private String avatarUrl;
}