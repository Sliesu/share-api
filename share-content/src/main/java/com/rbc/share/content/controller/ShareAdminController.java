package com.rbc.share.content.controller;


import com.rbc.share.common.resp.CommonResp;
import com.rbc.share.content.domain.entity.Share;

import java.util.List;

import com.rbc.share.content.service.ShareService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * @author DingYihang
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/share/admin")
public class ShareAdminController {

    private final ShareService shareService;

    /**
     * 获取尚未审核且显示标志为 false 的 Share 列表
     *
     * @return CommonResp<List<Share>> 返回符合条件的 Share 列表
     */
    @GetMapping("/list")
    public CommonResp<List<Share>> getSharesNotYet() {
        CommonResp<List<Share>> resp = new CommonResp<>();
        try {
            // 查询尚未审核的 Share 列表
            List<Share> shares = shareService.queryShareNotYet();
            resp.setData(shares);
        } catch (Exception e) {
            // 异常处理，记录日志并返回错误响应
            log.error("Error fetching shares: ", e);
            resp.setSuccess(false);
            resp.setMessage("Error fetching shares.");
        }
        return resp;
    }

    /**
     * 审核 Share
     *
     * @param shareId Share 的 ID
     */
    @PostMapping("/audit")
    public void auditShare(Long shareId) {

    }
}
