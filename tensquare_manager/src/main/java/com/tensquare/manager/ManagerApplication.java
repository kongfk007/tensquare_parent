package com.tensquare.manager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import util.JwtUtil;

/**
 * EnableEurekaClient 设置为客户端
 * EnableDiscoveryClient 启动发现客户端
 * EnableFeignClients	使用Feign发现客户端
 * EnableZuulProxy 开启网关
 */
@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
public class ManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManagerApplication.class, args);
	}

	@Bean
    public JwtUtil jwtUtil(){
	    return new JwtUtil();
    }
}
