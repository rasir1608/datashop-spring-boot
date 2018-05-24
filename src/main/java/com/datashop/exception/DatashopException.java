package com.datashop.exception;

/**
 * Created by rasir on 2018/5/24.
 */
public class DatashopException extends RuntimeException {

    private Integer code;

    public DatashopException(String msg,Integer code){
        super(msg);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
