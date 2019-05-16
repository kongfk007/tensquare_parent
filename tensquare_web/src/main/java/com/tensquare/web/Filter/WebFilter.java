package com.tensquare.web.Filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class WebFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";//前置过滤器
    }

    @Override
    public int filterOrder() {
        return 0;//优先级为0，数字越小，优先级越高
    }

    @Override
    public boolean shouldFilter() {
        return true;//是否执行该过滤器，true为执行,false不执行
    }

    /**
     * 过滤器的具体逻辑
     * 过滤器中接收header，转发给微服务(不然会丢失)
     */
    @Override
    public Object run() throws ZuulException {
        System.out.println("======zuul过滤器======");
        RequestContext requestContext  = RequestContext.getCurrentContext();
        //获取header
        HttpServletRequest request = requestContext.getRequest();
        String authorization = request.getHeader("Authorization");
        if (authorization!=null && !"".equals(authorization)){
            requestContext.addZuulRequestHeader("Authorization",authorization);
        }
        return null;//无论返回什么都执行
    }
}
