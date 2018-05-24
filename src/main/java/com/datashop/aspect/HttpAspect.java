package com.datashop.aspect;

import com.datashop.utils.CookieUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by rasir on 2018/5/24.
 */

@Aspect
@Component
public class HttpAspect {

    @Pointcut("execution( * com.datashop.controller.*.*(..))")
    public void cutControllers(){}

    @Before("cutControllers()")
    public void doBefore(JoinPoint joinPoint){

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletRequest request = attributes.getRequest();

        Map cookie = CookieUtil.getCookie("bear",Map.class);

        if(cookie != null) {
            System.out.println(cookie.get("userInfo"));
            System.out.println(new Long((String) cookie.get("maxAge")));
            System.out.println(new Long((String) cookie.get("beginTime")));
        }
    }

}
