package com.datashop.controller;

import com.alibaba.fastjson.JSONObject;
import com.datashop.bean.Auth;
import com.datashop.domain.DUser;
import com.datashop.exception.DatashopException;
import com.datashop.server.impl.UserServiceImpl;
import com.datashop.utils.CookieUtil;
import com.datashop.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rasir on 2018/5/24.
 */
@RestController
@RequestMapping("/duser")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    private BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder(11);

    @GetMapping("/getUserById/{id}")
    public Map getUserById(@PathVariable int id){
        DUser user = userService.getUserById(id);
        if(user != null) {
            user.setPassword(null);
            return ResultUtil.success(user);
        } else {
            throw new DatashopException("您输入ID有误",404);
        }
    }

//    注册帐号
    @PostMapping("/create")
    @Transactional
    public Map create(@RequestBody JSONObject req){
        String account = (String) req.get("account");
        System.out.println(account);
        DUser target = userService.getUserByAccount(account);
        if(target != null) {
            throw new DatashopException("帐号已被注册，请重新注册",400);
        } else {
            DUser user = new DUser();
            user.setAccount((String) req.get("account"));
            if(req.get("name") == null) user.setName((String) req.get("account"));
            else user.setName((String) req.get("name"));
            user.setRole(1);
            user.setCreateTime(new Date().getTime());
            user.setUpdateTime(new Date().getTime());
            String passwordHash = bCrypt.encode((String) req.get("password"));
            user.setPassword(passwordHash);
            user = userService.saveUser(user);
            user.setPassword(null);
            return ResultUtil.success(user);
        }
    }

    @PutMapping("/update")
    @Transactional
    public Map update(@RequestBody JSONObject req){
        Map cookie = CookieUtil.getCookie("bear",Map.class);
        Integer userId = new Integer((String) cookie.get("userId"));
        Integer reqId = req.getInteger("id");
        String reqPasswrod = req.getString("password");
        String reqName = req.getString("name");
        Integer reqRole = req.getInteger("role");
        String reqHeadurl = req.getString("headurl");
        if(reqId == null) reqId = userId;
        DUser reqUser = userService.getUserById(reqId);

        if( reqUser == null) {
            throw new DatashopException("要修改的用户不存在",404);
        }

        reqUser.setId(reqId);
        if(reqPasswrod != null) reqUser.setPassword(bCrypt.encode(reqPasswrod));
        if(reqName != null) reqUser.setName(reqName);
        if(reqRole != null) reqUser.setRole(reqRole);
        if(reqHeadurl != null) reqUser.setHeaderurl(reqHeadurl);
        reqUser.setUpdateTime(new Date().getTime());


        if(userId != reqId){
            DUser user = userService.getUserById(userId);
            if(user != null){
                if(user.getRole() < Auth.ADMIN.getRole()) {
                    throw new DatashopException("只有管理员才能修改非本人的用户信息！",401);
                } else {
                    return ResultUtil.handleResult(userService.updateById(reqUser),"用户信息更改失败！",500);
                }
            } else {
                throw new DatashopException("登录信息失效！",401);
            }
        } else {
            return ResultUtil.handleResult(userService.updateById(reqUser),"用户信息更改失败！",500);
        }
    }

//    登录
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
            cookie.put("userId",String.valueOf(user.getId()));
            cookie.put("account",user.getAccount());
            cookie.put("maxAge",String.valueOf(60*60*1000));
            cookie.put("beginTime",String.valueOf(new Date().getTime()));
            CookieUtil.addCookie("bear",cookie);
            return ResultUtil.success(user);
        }
    }

//    注销帐号
    @GetMapping("/logout")
    public Map logout(){
        return ResultUtil.success("帐号注销成功！");
    }
//    获取当前用户信息
    @GetMapping("/getUser")
    public Map getCurrentUser(){
        Map<String,Object> cookie = CookieUtil.getCookie("bear",Map.class);
        Integer userId = new Integer((String) cookie.get("userId"));
        DUser user = userService.getUserById(userId);
        if(user != null) {
            user.setPassword(null);
            return ResultUtil.success(user);
        } else {
            throw new DatashopException("请先登录！",404);
        }
    }

//    通过名称查找用户信息
    @GetMapping("/queryByName/{name}")
    public Map queryByName(@PathVariable String name){
        return ResultUtil.handleResult(userService.getUserListByName(name),"没有找到用户信息",404);
    }

//    查询所有用户信息
    @GetMapping("/queryAll")
    public Map queryAll(){
        return ResultUtil.handleResult(userService.selectAll(),"没有找到用户信息",404);
    }

}
