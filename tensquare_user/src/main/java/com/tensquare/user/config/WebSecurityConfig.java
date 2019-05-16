package com.tensquare.user.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 安全配置类（作为模板直接复制就行了）
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //authorizeRequests所有Security全注解实现的开端，表示开始说明需要的权限。
        //需要的权限分两部分，第一部分拦截的路径，第二部分访问该路径需要的权限
        //antMatchers拦截的路径，permitAll任何权限都可以访问,直接放行所有
        //anyRequest任意的请求，authenticated认证后才能访问
        //and().csrf().disable()固定写法，表示csrf拦截时效
        http
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable();
    }
}
