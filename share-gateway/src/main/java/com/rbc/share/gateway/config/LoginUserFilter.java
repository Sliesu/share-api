package com.rbc.share.gateway.config;

import com.rbc.share.gateway.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


/**
 * LoginUserFilter 是一个全局过滤器，主要用于对特定接口进行登录验证。
 * 对 "/admin"、"/hello"、"/user-service/user/login" 和 "/user-service/user/register" 等路径无需登录验证。
 * 其余接口路径会验证请求头中的 token 是否有效。
 * <p>
 * 如果 token 无效，则返回 401 Unauthorized 响应。
 * </p>
 *
 * @author Ding
 */
@Component
@Slf4j
public class LoginUserFilter implements Ordered, GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // 不需要登录验证的路径
        if (path.contains("/admin")
            ||  path.contains("/hello")
            ||  path.contains("/user-service/user/login")
            ||  path.contains("/user-service/user/register")
            ||  path.contains("/content-service/share/notice")
            ||  path.contains("/content-service/share/list")){
            log.info("无需登录验证的路径：{}", path);
            return chain.filter(exchange);
        } else {
            log.info("需要登录验证的路径：{}", path);
        }

        // 获取请求头中的 token
        String token = exchange.getRequest().getHeaders().getFirst("token");
        log.info("会员登录验证开始，token: {}", token);

        // 验证 token 是否存在
        if (token == null || token.isEmpty()) {
            log.warn("token 为空，请求被拦截！");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 验证 token 是否有效
        boolean validate = JwtUtil.validate(token);
        if (validate) {
            log.info("token 有效，放行请求");
            return chain.filter(exchange);
        } else {
            log.warn("token 无效，请求被拦截！");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return 0;  // 设置过滤器的顺序，数值越小优先级越高
    }
}
