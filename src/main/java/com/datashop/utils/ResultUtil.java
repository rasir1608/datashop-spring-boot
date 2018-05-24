package com.datashop.utils;
import java.util.HashMap;

/**
 * Created by 80002790 on 2018/4/26.
 */
public class ResultUtil {

    public static Result success(Object data){
        Result result = new Result();
        result.put("ok",true);
        result.put("data",data);
        result.put("msg","");
        result.put("state",200);
        return result;
    }
    public static Result success(){
        Result result = new Result();
        result.put("ok",true);
        result.put("data",null);
        result.put("msg","");
        result.put("state",200);
        return result;
    }
    public static Result error(String msg,Integer state){
        Result result = new Result();
        result.put("ok",false);
        result.put("data",null);
        result.put("msg",msg);
        result.put("state",state);
        return result;
    }
}

 class Result extends HashMap<String,Object> {
    public Result() {
    }
}
