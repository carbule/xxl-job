package com.korant.youya.workplace.config;

import com.korant.youya.workplace.filter.JwtAuthenticationTokenFilter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @ClassName WebSecurityConfig
 * @Description
 * @Author chenyiqiang
 * @Date 2023/11/16 16:22
 * @Version 1.0
 */
@Configuration
@EnableMethodSecurity
@Slf4j
public class WebSecurityConfig {

    @Resource
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 禁用basic明文验证
                .httpBasic(HttpBasicConfigurer::disable)
                // 前后端分离架构不需要csrf保护
                .csrf(CsrfConfigurer::disable)
                // 禁用默认登录页
                .formLogin(FormLoginConfigurer::disable)
                // 禁用默认登出页
                .logout(AbstractHttpConfigurer::disable)
                // 前后端分离是无状态的，不需要session了，直接禁用。
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        // 允许所有OPTIONS请求
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // 允许直接访问授权登录接口
                        .requestMatchers("/user/loginByWechatCode", "/user/loginBySMSVerificationCode", "/user/loginByPassword", "/user/getVerificationCode").permitAll()
                        // 允许 SpringMVC 的默认错误地址匿名访问
                        .requestMatchers("/error").permitAll()
                        // 其他所有接口必须有Authority信息，Authority在登录成功后的UserDetailsImpl对象中默认设置“ROLE_USER”
                        //.requestMatchers("/**").hasAnyAuthority("ROLE_USER")
                        // 允许任意请求被已登录用户访问，不检查Authority
                        .anyRequest().authenticated())
                // 加我们自定义的过滤器，替代UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
