package com.rbc.share.common.handler;

import com.rbc.share.common.exception.BusinessException;
import com.rbc.share.common.resp.CommonResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author DingYihang
 */
@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public CommonResp<?> exceptionHandler(Exception e) throws Exception {
        CommonResp<?> resp = new CommonResp<>();
        log.error("系统异常", e);
        resp.setSuccess(false);
        resp.setMessage(e.getMessage());
        return resp;
    }

    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public CommonResp<?> exceptionHandler(BusinessException e) throws Exception {
        CommonResp<?> resp = new CommonResp<>();
        log.error("业务异常", e);
        resp.setSuccess(false);
        resp.setMessage(e.getE().getDesc());
        return resp;
    }

    /**
     * 全局统⼀异常处理类中，添加校验异常统⼀处理的代码
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public CommonResp<?> exceptionHandler(BindException e) throws Exception {
        CommonResp<?> resp = new CommonResp<>();
        log.error("校验异常:{}", e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        resp.setSuccess(false);
        resp.setMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return resp;
    }
}