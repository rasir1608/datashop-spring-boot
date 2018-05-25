package com.datashop.handler;

import com.datashop.exception.DatashopException;
import com.datashop.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by rasir on 2018/5/24.
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    private Map<String,Object> exceptionHandle(HttpServletRequest req,Exception e){

        for(int i = 0; i < e.getStackTrace().length ; i ++){
            System.out.println(e.getStackTrace()[i]);
        }

        if(e instanceof DatashopException){
            DatashopException de = (DatashopException) e;
            return ResultUtil.error(de.getMessage(),de.getCode());
        } else {
            for (int i = 0; i < e.getStackTrace().length; i ++){
                System.out.println(e.getStackTrace()[i]);
            }
        }

        return ResultUtil.error(e.getMessage(),500);

    }
}
