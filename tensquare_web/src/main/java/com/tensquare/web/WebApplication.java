package com.tensquare.web;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * EnableEurekaClient 设置为客户端
 * EnableDiscoveryClient 启动发现客户端
 * EnableFeignClients	使用Feign发现客户端
 * EnableZuulProxy 开启网关
 */
@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
public class WebApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}

}
