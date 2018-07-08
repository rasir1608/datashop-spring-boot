package com.datashop.controller;

import com.alibaba.fastjson.JSONObject;
import com.datashop.bean.Auth;
import com.datashop.bean.VerificationCode;
import com.datashop.domain.DUser;
import com.datashop.exception.DatashopException;
import com.datashop.server.impl.UserServiceImpl;
import com.datashop.utils.CookieUtil;
import com.datashop.utils.FileHandler;
import com.datashop.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by rasir on 2018/5/24.
 */
@RestController
@RequestMapping("/duser")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    private BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder(11);

    /**
     * 通过id查找用户信息
     * @param id
     * @return
     */
    @GetMapping("/getUserById/{id}")
    public Map getUserById(@PathVariable int id){
        DUser user = userService.getUserById(id);
        if(user != null) {
            user.setPassword(null);
            return ResultUtil.success(user);
        } else {
            throw new DatashopException("您输入ID有误",200);
        }
    }

    /**
     * 注册帐号
     * @param req
     * @return
     */
    @PostMapping("/create")
    @Transactional
    public Map create(@RequestBody JSONObject req){
        String account = (String) req.get("account");
        DUser target = userService.getUserByAccount(account);
        if(target != null) {
            throw new DatashopException("帐号已被注册，请重新注册",200);
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

    @PostMapping("/update")
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
            throw new DatashopException("要修改的用户不存在",200);
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
                    throw new DatashopException("只有管理员才能修改非本人的用户信息！",200);
                } else {
                    return ResultUtil.handleResult(userService.updateById(reqUser),"用户信息更改失败！",200);
                }
            } else {
                throw new DatashopException("登录信息失效！",200);
            }
        } else {
            return ResultUtil.handleResult(userService.updateById(reqUser),"用户信息更改失败！",200);
        }
    }

    /**
     * 用户登录
     * @param req
     * @return
     */
    @PostMapping("/login")
    public Map login(@RequestBody JSONObject req){
        if(req.get("account") == null || req.get("password") == null) {
            throw new DatashopException("帐号密码不能为空！",200);
        }
        DUser user = userService.getUserByAccount((String) req.get("account"));
        if(user == null) {
            throw new DatashopException("帐号不存在，请重新输入！",200);
        } else if(!bCrypt.matches((String) req.get("password"),user.getPassword())){
            throw new DatashopException("密码错误，请重新输入！",200);
        } else {
            user.setPassword(null);
            Map<String,Object> cookie = new HashMap<>();
            cookie.put("userId",String.valueOf(user.getId()));
            cookie.put("account",user.getAccount());
            CookieUtil.addCookie("bear",cookie);
            return ResultUtil.success(user);
        }
    }

    /**
     * 注销登录
     * @return
     */
    @GetMapping("/logout")
    public Map logout(){
        CookieUtil.removeCookie("bear");
        return ResultUtil.success("帐号注销成功！");
    }

    /**
     * 获取当前用户信息
     * @return
     */
    @GetMapping("/getUser")
    public Map getCurrentUser(){
        Map<String,Object> cookie = CookieUtil.getCookie("bear",Map.class);
        Integer userId = new Integer((String) cookie.get("userId"));
        DUser user = userService.getUserById(userId);
        if(user != null) {
            user.setPassword(null);
            return ResultUtil.success(user);
        } else {
            throw new DatashopException("请先登录！",200);
        }
    }
    /**
     * 通过名称查找用户信息
     * @param name
     * @return
     */
    @GetMapping("/getUserListByName/{name}")
    public Map queryByName(@PathVariable String name){
        if(name != null) name = "%"+name+"%";
        return ResultUtil.handleResult(userService.getUserListByName(name),"没有找到用户信息",200);
    }

    /**
     * 查询所有用户信息
     * @return
     */
    @GetMapping("/queryAll")
    public Map queryAll(){
        return ResultUtil.handleResult(userService.selectAll(),"没有找到用户信息",200);
    }

    /**
     * 修改昵称
     * @param newName
     * @return
     */
    @GetMapping("/updateName/{newName}")
    public Map changeName(@PathVariable String newName){
        DUser temp = new DUser();
        temp.setName(newName);
        DUser exUser = userService.getUser(temp);
        if(exUser != null) {
            throw new DatashopException("昵称已被占用，请重新输入！",200);
        } else {
            Map<String,Object> cookie = CookieUtil.getCookie("bear",Map.class);
            Integer userId = new Integer((String) cookie.get("userId"));
            DUser user = userService.getUserById(userId);
            if(user == null) {
                throw new DatashopException("服务异常，请重新尝试",500);
            } else {
                user.setName(newName);
                return ResultUtil.handleResult(userService.updateById(user),"昵称修改失败！",200);
            }
        }
    }

    /**
     * 修改密码
     * @param req
     * @return
     */
    @PostMapping("/updatePasswrod")
    public Map updatePassword(@RequestBody JSONObject req){
        String oldP = req.getString("oldPassword");
        String newP = req.getString("newPassword");
        Map<String,Object> cookie = CookieUtil.getCookie("bear",Map.class);
        Integer userId = new Integer((String) cookie.get("userId"));
        DUser user = userService.getUserById(userId);
        if(bCrypt.matches(oldP,user.getPassword())){
            user.setPassword(bCrypt.encode(newP));
            return ResultUtil.handleResult(userService.updateById(user),"密码修改失败！",200);
        } else {
            throw new DatashopException("原密码输入有误，请重新输入",200);
        }
    }

    /**
     * 上传用户头像
     * @param multipartFile
     * @param fileName
     * @return
     */
    @PostMapping("/upload")
    public Map uploadUserHeader(@RequestParam("file") MultipartFile multipartFile, @RequestParam("fileName")String fileName){
        if(multipartFile.isEmpty()){
            throw new DatashopException("未获取到图片",200);
        } else if(multipartFile.getSize() > 2097152){
            throw new DatashopException("请选择小于2MB的图片",200);
        }
        try{
            String contentType = multipartFile.getContentType();
            if(Pattern.matches("^image/\\w+",contentType)){

                String[] fileNameParts = multipartFile.getOriginalFilename().split("\\.");
                String fileSuffix = fileNameParts[(fileNameParts.length-1)];
                List<String> suffixArray = Arrays.asList("png","jpg","gif");
                if(!suffixArray.contains(fileSuffix)){
                    throw new DatashopException("请上传png、jpg、gif类型的文件！",200);
                } else{
                    DUser user = getUserInfo();
                    String dirName ="/image";
                    String newFileName = user.getAccount() + "--" + "header"+new Date().getTime()+"."+fileSuffix;
                    String filePath = FileHandler.saveFile(multipartFile.getInputStream(),newFileName,dirName);
                    String oldHeader = user.getHeaderurl();
                    if(oldHeader != null){
                        FileHandler.removeFile(oldHeader);
                    }
                    user.setHeaderurl(filePath);
                    user.setUpdateTime(new Date().getTime());
                    userService.updateById(user);
                    return ResultUtil.success(filePath);
                }
            } else {
                throw new DatashopException("请不要上传图片以外的文件",200);
            }
        } catch(Exception e){
            e.printStackTrace();
            throw new DatashopException(e.getMessage(),200);
        }
    }

    /**
     * 获取验证码
     * @param request
     * @param response
     */
    @GetMapping("/getVerificationCode")
    public  void getVerificationCode(HttpServletRequest request, HttpServletResponse response){
        System.out.println(294);
        try{
                VerificationCode verificationCode = new VerificationCode();
                //获取验证码图片
                BufferedImage image = verificationCode.getImage();
                //获取验证码内容
                String text = verificationCode.getText();
                // randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
                StringBuffer randomCode = new StringBuffer();
                randomCode.append(text);
                // 将验证码保存到Session中。
                HttpSession session = request.getSession();
                session.setAttribute("signcode", randomCode.toString());
                System.out.println("session-signcode==>"+randomCode.toString());
                // 禁止图像缓存。
                response.setHeader("Pragma", "no-cache");
                response.setHeader("Cache-Control", "no-cache");
                response.setDateHeader("Expires", 0);
                response.setContentType("image/jpeg");
                // 将图像输出到Servlet输出流中。
                ServletOutputStream sos = response.getOutputStream();
                ImageIO.write(image, "jpeg", sos);
                sos.flush();
                sos.close();
            } catch (Exception e){
                throw new DatashopException(e.getMessage(),500);
            }
    }

    /**
     * 校验验证码
     * @param request
     * @param signcode
     * @return
     */
    @GetMapping("/checkVerificationCode/{signcode}")
    public Map check(HttpServletRequest request,@PathVariable String signcode) {
        HttpSession session = request.getSession();
        String signcodeSession = (String) session.getAttribute("signcode");
        System.out.println("signcode==>"+signcode);
        System.out.println("signcodeSession==>"+signcodeSession);
        if(signcode == null){
            throw new DatashopException("请输入验证码",200);
        }else if( signcodeSession == null){
            throw new DatashopException("请重新刷新验证码",200);
        } else if (signcode.equalsIgnoreCase(signcodeSession)) { //验证的时候不区分大小写
            return ResultUtil.success("验证码正确");
        } else {
            return ResultUtil.error("验证码错误",200);
        }
    }
    private DUser getUserInfo(){
        Map cookie = CookieUtil.getCookie("bear",Map.class);
        Integer userId = new Integer((String) cookie.get("userId"));
        DUser user = userService.getUserById(userId);
        if(user != null) {
            return user;
        } else {
            throw new DatashopException("登录信息有误",200);
        }
    }

}
