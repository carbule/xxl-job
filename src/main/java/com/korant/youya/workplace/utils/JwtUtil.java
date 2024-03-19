package com.korant.youya.workplace.utils;

import cn.hutool.core.lang.UUID;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

/**
 * @ClassName JwtUtil
 * @Description
 * @Author chenyiqiang
 * @Date 2023/7/23 14:51
 * @Version 1.0
 */
public class JwtUtil {

    //过期时长
    private static final long EXPIRE_TIME = 1000 * 60 * 60 * 24;
    //密钥盐
    private static final String TOKEN_SECRET = "korant.youya";

    /**
     * 签名生成
     *
     * @return
     */
    public static String createToken(Long userId) {
        String token = null;
        try {
            Date expiresAt = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            token = JWT.create()
                    .withJWTId(UUID.randomUUID().toString())
                    //签发者
                    .withIssuer("auth0")
                    //接收方
                    .withAudience("youya")
                    //签发时间
                    .withIssuedAt(new Date())
                    //过期时间
//                    .withExpiresAt(expiresAt)
                    .withClaim("userId", userId)
                    // 使用了HMAC256加密算法。
                    .sign(Algorithm.HMAC256(TOKEN_SECRET));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    /**
     * 签名验证
     *
     * @param token
     * @return
     */
    public static boolean verify(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("auth0").build();
            DecodedJWT jwt = verifier.verify(token);
            System.out.println("认证通过：");
            System.out.println("签发者: " + jwt.getIssuer());
            System.out.println("接收方: " + jwt.getAudience());
            System.out.println("userId: " + jwt.getClaim("userId").asLong());
            System.out.println("签发时间: " + jwt.getIssuedAt());
            System.out.println("过期时间：      " + jwt.getExpiresAt());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取token中的用户id
     *
     * @param token
     * @return
     */
    public static Long getId(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("auth0").build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("userId").asLong();
    }

    public static void main(String[] args) {
        System.out.println(createToken(1764941765427163138L));
    }
}
