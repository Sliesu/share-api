package com.rbc.share.content.controller;


import com.rbc.share.common.resp.CommonResp;
import com.rbc.share.content.domain.entity.Share;

import java.util.List;

import com.rbc.share.content.domain.resp.ShareResp;
import com.rbc.share.content.service.BonusProducer;
import com.rbc.share.content.service.ShareService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    private final BonusProducer bonusProducer;

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
    @PostMapping("/audit/{shareId}")
    public CommonResp<String> auditShare(@PathVariable Long shareId) {
        CommonResp<String> resp = new CommonResp<>();
        Share share = shareService.findById(shareId).getShare();
        if (share != null && "NOT_YET".equals(share.getAuditStatus())) {
            try {
                shareService.approveShare(share);
                bonusProducer.sendPointsMessage(share.getUserId(),50);
                resp.setMessage("审核成功");
                return resp;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }else{
            resp.setMessage("分享内容无法审核");
            resp.setSuccess(false);
            return resp;
        }

    }
}
