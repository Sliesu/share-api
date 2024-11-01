package com.rbc.share.common.util;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * JwtUtil 工具类，封装了 Hutool 的 JWT 工具，提供了生成、校验和解析 JWT token 的功能。
 * 重要：盐值不能泄露，建议在生产环境中使用更复杂的密钥，并存放在安全的配置文件中。
 * <p>
 * @author Ding
 */
@Slf4j
public class JwtUtil {

    /**
     * 加密盐值，建议在配置文件中设置，以确保安全。
     */
    private static final String KEY = "helloworld";

    /**
     * 创建 JWT token
     *
     * @param id    用户 ID
     * @param phone 用户手机号
     * @return 生成的 JWT token
     */
    public static String createToken(Long id, String phone) {
        DateTime now = DateTime.now();
        //DateTime expTime = now.offsetNew(DateField.SECOND, 100000);
        DateTime expTime = now.offsetNew(DateField.HOUR,48);
        Map<String, Object> payload = new HashMap<>();

        // 设置 JWT 载荷信息：签发时间、过期时间、生效时间和用户信息
        payload.put(JWTPayload.ISSUED_AT, now);
        payload.put(JWTPayload.EXPIRES_AT, expTime);
        payload.put(JWTPayload.NOT_BEFORE, now);
        payload.put("id", id);
        payload.put("phone", phone);

        String token = JWTUtil.createToken(payload, KEY.getBytes());
        log.info("生成 JWT token：{}", token);
        return token;
    }

    /**
     * 校验 JWT token 是否有效
     *
     * @param token JWT token
     * @return 校验结果，true 表示有效，false 表示无效
     */
    public static boolean validate(String token) {
        JWT jwt = JWTUtil.parseToken(token).setKey(KEY.getBytes());
        boolean validate = jwt.validate(0); // 包含了 verify 验证
        log.info("JWT token 校验结果：{}", validate);
        return validate;
    }

    /**
     * 获取 JWT token 中的原始内容
     *
     * @param token JWT token
     * @return 解析出的载荷信息 JSONObject
     */
    public static JSONObject getJSONObject(String token) {
        JWT jwt = JWTUtil.parseToken(token).setKey(KEY.getBytes());
        JSONObject payloads = jwt.getPayloads();

        // 去除不必要的默认信息
        payloads.remove(JWTPayload.ISSUED_AT);
        payloads.remove(JWTPayload.EXPIRES_AT);
        payloads.remove(JWTPayload.NOT_BEFORE);

        log.info("根据 token 获取原始内容：{}", payloads);
        return payloads;
    }

    /**
     * 主方法测试 JWT 生成、校验与解析
     *
     * @param args 主方法参数
     */
    public static void main(String[] args) {
        String token = createToken(1L, "13951905171");
        System.out.println("生成的 Token: " + token);

        try {
            Thread.sleep(12000); // 模拟过期情况
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        validate(token); // 校验 token
        getJSONObject(token); // 获取 token 内容
    }
}
