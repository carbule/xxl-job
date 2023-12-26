package com.korant.youya.workplace.filter;

import com.alibaba.fastjson.JSONObject;
import com.korant.youya.workplace.constants.RedisConstant;
import com.korant.youya.workplace.enums.user.UserAccountStatusEnum;
import com.korant.youya.workplace.mapper.UserMapper;
import com.korant.youya.workplace.pojo.LoginUser;
import com.korant.youya.workplace.pojo.R;
import com.korant.youya.workplace.utils.JwtUtil;
import com.korant.youya.workplace.utils.RedisUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName JwtAuthenticationTokenFilter
 * @Description
 * @Author chenyiqiang
 * @Date 2023/11/7 15:24
 * @Version 1.0
 */
@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private UserMapper userMapper;

    private static final String ROLE_PREFIX = "ROLE_";

    public static final List<String> PERMIT_URL = new ArrayList<>();

    static {
        PERMIT_URL.add("/user/loginByWechatCode");
        PERMIT_URL.add("/user/loginBySMSVerificationCode");
        PERMIT_URL.add("/user/loginByPassword");
        PERMIT_URL.add("/user/getVerificationCode");
        PERMIT_URL.add("/job/queryHomePageList");
        PERMIT_URL.add("/huntJob/queryHomePageList");
        PERMIT_URL.add("/wechat/js/sign");
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            log.debug("OPTIONS请求,放行");
            filterChain.doFilter(request, response);
        }
        String requestURI = request.getRequestURI();
        if (PERMIT_URL.contains(requestURI)) {
            log.info("请求地址为：{},放行", requestURI);
            filterChain.doFilter(request, response);
            return;
        }
        log.info("接收到请求，请求路径：{}", requestURI);
        String token = request.getHeader("token");
        if (StringUtils.isBlank(token)) {
            log.error("请求路径：{},未携带token", requestURI);
            returnJson(response, HttpServletResponse.SC_UNAUTHORIZED, "401", "请求未携带token");
            return;
        }
        try {
            Long id = JwtUtil.getId(token);
            String key = String.format(RedisConstant.YY_USER_TOKEN, id);
            Object obj = redisUtil.get(key);
            if (null != obj) {
                String tokenCache = obj.toString();
                if (token.equals(tokenCache)) {
                    String cacheKey = String.format(RedisConstant.YY_USER_CACHE, id);
                    Object cacheObj = redisUtil.get(cacheKey);
                    if (null == cacheObj) {
                        LoginUser loginUser = userMapper.selectUserToLoginById(id);
                        if (null == loginUser) {
                            log.error("请求路径：{},用户未注册或已注销", requestURI);
                            returnJson(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "9999", "用户未注册或已注销");
                        } else {
                            redisUtil.set(cacheKey, JSONObject.toJSONString(loginUser), 7200);
                            Integer accountStatus = loginUser.getAccountStatus();
                            if (UserAccountStatusEnum.FROZEN.getStatus() == accountStatus) {
                                log.error("请求路径：{},账号:{}已被冻结", requestURI, id);
                                returnJson(response, HttpServletResponse.SC_FORBIDDEN, "403", "账号已被冻结,详情请咨询客服");
                            } else {
                                String role = loginUser.getRole();
                                ArrayList<SimpleGrantedAuthority> list = new ArrayList<>();
                                if (StringUtils.isNotBlank(role)) {
                                    SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(ROLE_PREFIX + role);
                                    list.add(simpleGrantedAuthority);
                                }
                                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, list);
                                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                                filterChain.doFilter(request, response);
                            }
                        }
                    } else {
                        LoginUser loginUser = JSONObject.parseObject(cacheObj.toString(), LoginUser.class);
                        Integer accountStatus = loginUser.getAccountStatus();
                        if (UserAccountStatusEnum.FROZEN.getStatus() == accountStatus) {
                            log.error("请求路径：{},账号:{}已被冻结", requestURI, id);
                            returnJson(response, HttpServletResponse.SC_FORBIDDEN, "403", "账号已被冻结,详情请咨询客服");
                        } else {
                            String role = loginUser.getRole();
                            ArrayList<SimpleGrantedAuthority> list = new ArrayList<>();
                            if (StringUtils.isNotBlank(role)) {
                                SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(ROLE_PREFIX + role);
                                list.add(simpleGrantedAuthority);
                            }
                            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, list);
                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                            filterChain.doFilter(request, response);
                        }
                    }
                } else {
                    log.error("请求路径：{},token校验失败", requestURI);
                    returnJson(response, HttpServletResponse.SC_UNAUTHORIZED, "401", "token校验失败");
                }
            } else {
                log.error("请求路径：{},token经校验已过期", requestURI);
                returnJson(response, HttpServletResponse.SC_UNAUTHORIZED, "401", "token过期");
            }
        } catch (Exception e) {
            log.error("请求路径：{},token校验失败,失败原因：", requestURI, e);
            returnJson(response, HttpServletResponse.SC_UNAUTHORIZED, "401", "token校验失败");
        }
    }

    /**
     * 向响应流中写入数据
     *
     * @param response
     * @param resCode
     * @param resMessage
     */
    private void returnJson(HttpServletResponse response, int status, String resCode, String resMessage) {
        R<?> r = R.error(resCode, resMessage);
        String json = JSONObject.toJSONString(r);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        response.setStatus(status);
        try (PrintWriter writer = response.getWriter()) {
            writer.print(json);
        } catch (IOException e) {
            log.error("IO异常,错误信息：", e);
        }
    }
}
