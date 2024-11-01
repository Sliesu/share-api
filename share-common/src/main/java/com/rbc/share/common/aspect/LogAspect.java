package com.rbc.share.common.aspect;


import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.PropertyPreFilters;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author DingYihang
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    public LogAspect() {
        log.info("LogAspect initialized");
    }

    /**
     * 定义一个切点
     */
    @Pointcut("execution(public * com.rbc..*Controller.*(..))")
    public void controllerPointcut() {
    }

    /**
     * 前置增强
     * @param joinPoint 切点
     */
    @Before("controllerPointcut()")
    public void deBefore(JoinPoint joinPoint) {
        // 增加日志流水号
//        MDC.put("LOG_ID", System.currentTimeMillis() + RandomUtil.randomString(3));

        // 开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        Signature signature = joinPoint.getSignature();
        String methodName = signature.getName();

        // 打印请求信息
        log.info("-------------- 开始 ----------------");
        log.info("请求地址：{}，方法：{}", request.getRequestURL(), request.getMethod());
        log.info("类名方法：{}，{}", signature.getDeclaringTypeName(), methodName);
        log.info("远程地址：{}", request.getRemoteAddr());

        // 打印请求参数
        Object[] args = joinPoint.getArgs();
        log.info("请求参数：{}", JSONObject.toJSONString(args));
    }

    @Around("controllerPointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();

        // 排除字段，敏感字段或太长的字段不显示：如手机号、密码等
        String[] excludeProperties = {"phone", "password"};
        PropertyPreFilters filters = new PropertyPreFilters();
        PropertyPreFilters.MySimplePropertyPreFilter excludeFilter = filters.addFilter();
        excludeFilter.addExcludes(excludeProperties);

        // 打印返回结果
        log.info("返回结果：{}", JSONObject.toJSONString(result, excludeFilter));
        log.info("----------------- 结束，耗时：{} ms -------------------", System.currentTimeMillis() - startTime);

        return result;
    }
}
