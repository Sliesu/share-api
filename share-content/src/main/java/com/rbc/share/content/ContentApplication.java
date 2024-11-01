package com.rbc.share.content;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

/**
 * @author DingYihang
 */
@SpringBootApplication
@ComponentScan("com.rbc")
@MapperScan("com.rbc.share.*.mapper")
@Slf4j
public class ContentApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ContentApplication.class);
        Environment env = app.run(args).getEnvironment();
        log.info("启动成功！");
        log.info("测试地址：http://127.0.0.1:{}{}/hello",
                env.getProperty("server.port"),
                env.getProperty("server.servlet.context-path"));
    }
}
