package com.datashop.controller;

import com.alibaba.fastjson.JSONObject;
import com.datashop.domain.DUser;
import com.datashop.exception.DatashopException;
import com.datashop.server.impl.UserServiceImpl;
import com.datashop.utils.CookieUtil;
import com.datashop.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.Result;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rasir on 2018/5/24.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    private BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder(11);

    @GetMapping("/{id}")
    public Map getUserById(@PathVariable int id){
        DUser user = userService.getUserById(id);
        if(user != null) {
            user.setPassword(null);
            return ResultUtil.success(user);
        } else {
            throw new DatashopException("您输入帐号有误",404);
        }
    }

    @PostMapping("/register")
    @Transactional
    public Map registerUser(@RequestBody JSONObject req){
        String account = (String) req.get("account");
        System.out.println(CookieUtil.getCookie("bear"));
        DUser target = userService.getUserByAccount(account);
        if(target != null) {
            throw new DatashopException("帐号已被注册，请重新注册",400);
        } else {
            DUser user = new DUser();
            user.setAccount((String) req.get("account"));
            if(req.get("name") == null) user.setName((String) req.get("account"));
            else user.setName((String) req.get("name"));
            user.setRole(1);
            user.setCreateTime(String.valueOf(new Date().getTime()));
            user.setUpdateTime(String.valueOf(new Date().getTime()));
            String passwordHash = bCrypt.encode((String) req.get("password"));
            user.setPassword(passwordHash);
            user = userService.saveUser(user);
            user.setPassword(null);
            return ResultUtil.success(user);
        }
    }


    @PostMapping("/login")
    public Map login(@RequestBody JSONObject req){
        if(req.get("account") == null || req.get("password") == null) {
            throw new DatashopException("帐号密码不能为空！",400);
        }
        DUser user = userService.getUserByAccount((String) req.get("account"));
        if(user == null) {
            throw new DatashopException("帐号不存在，请重新输入！",404);
        } else if(!bCrypt.matches((String) req.get("password"),user.getPassword())){
            throw new DatashopException("密码错误，请重新输入！",401);
        } else {
            user.setPassword(null);
            Map<String,Object> cookie = new HashMap<>();
            cookie.put("userInfo",user);
            cookie.put("maxAge",String.valueOf(60*60*1000));
            cookie.put("beginTime",String.valueOf(new Date().getTime()));
            CookieUtil.addCookie("bear",cookie);
            return ResultUtil.success(user);
        }
    }

    @GetMapping("/logout")
    public Map logout(){
        CookieUtil.removeCookie("bear");
        return ResultUtil.success("帐号注销成功！");
    }

}
