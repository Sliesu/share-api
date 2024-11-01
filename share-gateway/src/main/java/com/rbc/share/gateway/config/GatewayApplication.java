package com.rbc.share.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author DingYihang
 */

@SpringBootApplication
@Component("com.rbc")
@Slf4j
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(GatewayApplication.class);
        Environment env = app.run(args).getEnvironment();
        log.info("网关启动！");
        log.info("网关地址：http://127.0.0.1:{}", env.getProperty("server.port"));
    }
}
