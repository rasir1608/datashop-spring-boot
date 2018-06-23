package com.datashop.aspect;

import com.datashop.exception.DatashopException;
import com.datashop.utils.CookieUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by rasir on 2018/5/24.
 */

@Aspect
@Component
public class HttpAspect {

    @Value(value = "#${server.context-path}")
    private String contextPath;

    @Pointcut("execution( * com.datashop.controller.*.*(..))")
    public void cutControllers(){}

    @Before("cutControllers()")
    public void doBefore(JoinPoint joinPoint){

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletRequest request = attributes.getRequest();

        String url = request.getRequestURI();

        String pattern = contextPath.substring(1) + "/duser/((login)|(create)|(logout))$";
        if(Pattern.matches(pattern,url)){
            CookieUtil.removeCookie("bear");
        } else {
            Cookie cookie = CookieUtil.getCookie("bear", Cookie.class);
            if(cookie == null) {
                throw new DatashopException("请先登陆！",302);
            } else {
                CookieUtil.removeCookie("bear");
                CookieUtil.addCookie("bear",cookie);
            }
        }

    }

}
