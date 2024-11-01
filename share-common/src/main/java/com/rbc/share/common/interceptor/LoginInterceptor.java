package com.rbc.share.common.interceptor;

import cn.hutool.core.util.RandomUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * LoginInterceptor 是一个请求拦截器，主要用于在处理请求前将日志 ID 放入 MDC 中。
 * 这可以帮助在日志中追踪请求的处理过程。
 *
 * @author Ding
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 在请求处理之前执行该方法。
     * 将一个唯一的日志 ID 放入 MDC 中，以便于后续日志记录时使用。
     *
     * @param request  当前的 HTTP 请求
     * @param response 当前的 HTTP 响应
     * @param handler  被调用的处理器对象
     * @return boolean  返回 true 表示继续处理请求，返回 false 表示中断请求
     * @throws Exception 可能抛出的异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 生成唯一的日志 ID，并放入 MDC 中
        MDC.put("LOG_ID", System.currentTimeMillis() + RandomUtil.randomString(3));
        // 继续处理请求
        return true;
    }
}