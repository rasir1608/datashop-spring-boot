package com.datashop.controller;

import com.alibaba.fastjson.JSONObject;
import com.datashop.domain.DPowerMapping;
import com.datashop.exception.DatashopException;
import com.datashop.server.inter.PowerMappingServer;
import com.datashop.utils.CookieUtil;
import com.datashop.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.plugin2.main.server.ResultHandler;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/dpower")
public class PowerMappingController {

    @Autowired
    private PowerMappingServer powerServer;


    /**
     * 申请加入项目
     * @param req
     * @return
     */
    @PostMapping("/save")
    public Map save(@RequestBody JSONObject req){
        Integer id = req.getInteger("id");
        Integer userId = req.getInteger("userId");
        Integer projectId = req.getInteger("projectId");
        Integer power = req.getInteger("power");
        DPowerMapping reqPm = new DPowerMapping();
        reqPm.setId(id);
        reqPm.setUserId(userId);
        reqPm.setProjectId(projectId);
        reqPm.setPower(power);
        reqPm.setUpdateTime(new Date().getTime());
        if(id == null){
            return createMapping(reqPm);
        } else {
            return updateMapping(reqPm);
        }
    }

    private Map createMapping(DPowerMapping dPowerMapping){
        DPowerMapping pme = powerServer.selectByUserAndProject(dPowerMapping.getUserId(),dPowerMapping.getProjectId());
        if(pme == null) {
            dPowerMapping.setCreateTime(new Date().getTime());
            return ResultUtil.handleResult(powerServer.create(dPowerMapping),"该账号与项目关系保存失败",500);
        } else {
            throw new DatashopException("该账号与项目已经存在关系",401);
        }
    }

    private Map updateMapping(DPowerMapping dPowerMapping){
        DPowerMapping pme = powerServer.findById(dPowerMapping.getId());
        if(pme != null) {
            pme.setUpdateTime(dPowerMapping.getUpdateTime());
            pme.setPower(dPowerMapping.getPower());
            return ResultUtil.handleResult(powerServer.updateById(pme),"该账号与项目关系更新失败",500);
        } else {
            throw new DatashopException("该账号与项目不存在映射",401);
        }
    }

    @PostMapping("/update")
    public Map update(@RequestBody JSONObject req){
        Integer power = req.getInteger("power");
        Integer id = req.getInteger("id");
        DPowerMapping pm = powerServer.findById(id);
        if(pm != null) {
            pm.setPower(power);
            pm.setUpdateTime(new Date().getTime());
            return ResultUtil.handleResult(powerServer.updateById(pm),"该账号与项目映射保存失败",500);
        } else {
            throw new DatashopException("该账号与项目不存在映射",404);
        }
    }

    /**
     * 删除申请项目的流程
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public Map deleteById(@PathVariable Integer id){
        return ResultUtil.handleResult(powerServer.deleteById(id),"删除失败",500);
    }

    @GetMapping("/queryByUser/{userId}")
    public Map queryByUser(@PathVariable Integer userId){
        return ResultUtil.handleResult(powerServer.queryProjectsByUser(userId,null),"查询错误",500);
    }
    @GetMapping("/queryByProject/{projectId}")
    public Map queryByProject(@PathVariable Integer projectId){
        return ResultUtil.handleResult(powerServer.queryUsersByProject(projectId,null),"查询错误",500);
    }

    @GetMapping("/find/{id}")
    public Map findById(@PathVariable Integer id){
        return ResultUtil.handleResult(powerServer.findById(id),"获取映射失败！",500);
    }

    /**
     * 我申请中的项目
     * @return
     */
    @GetMapping("/myApplying")
    public Map getMyApplyingProject(){
        Map<String,Object> cookie = CookieUtil.getCookie("bear",Map.class);
        Integer userId = new Integer((String) cookie.get("userId"));
        return ResultUtil.handleResult(powerServer.queryAllMyApplingProjectList(userId),"获取项目列表失败",500);
    }

    /**
     * 申请我的项目的信息
     * @return
     */
    @GetMapping("/applyMine")
    public Map getApplyingMineMapping(){
        Map<String,Object> cookie = CookieUtil.getCookie("bear",Map.class);
        Integer userId = new Integer((String) cookie.get("userId"));
        return ResultUtil.handleResult(powerServer.queryAllApplyMineMappingList(userId),"获取项目列表失败",500);
    }

    @GetMapping("/delete/{powerId}")
    public Map deleteMapping(@PathVariable Integer powerId){
        return ResultUtil.handleResult(powerServer.deleteById(powerId),"删除失败！",500);
    }

    /**
     * 驳回申请项目的请求
     * @param powerId
     * @return
     */
    @GetMapping("/dismiss/{powerId}")
    public Map dismissApplying(@PathVariable Integer powerId){
        DPowerMapping dpm = powerServer.findById(powerId);
        if(dpm != null) {
            dpm.setPower(2);
            dpm.setUpdateTime(new Date().getTime());
            return ResultUtil.handleResult(powerServer.updateById(dpm),"申请驳回失败！",500);
        } else {
            throw new DatashopException("申请驳回失败！",404);
        }
    }

    /**
     * 本人申请项目
     * @param projectId
     * @return
     */
    @GetMapping("/create/{projectId}")
    public Map createMapping(@PathVariable Integer projectId){
        Map<String,Object> cookie = CookieUtil.getCookie("bear",Map.class);
        Integer userId = new Integer((String) cookie.get("userId"));
        DPowerMapping newDpm = new DPowerMapping();
        newDpm.setPower(0);
        newDpm.setUserId(userId);
        newDpm.setProjectId(projectId);
        newDpm.setCreateTime(new Date().getTime());
        newDpm.setUpdateTime(new Date().getTime());
        DPowerMapping dpm = powerServer.selectByUserAndProject(newDpm.getUserId(),newDpm.getProjectId());
        if(dpm != null){
            if(dpm.getPower() == 2) {
             powerServer.deleteById(dpm.getId());
             return ResultUtil.handleResult(powerServer.create(newDpm),"申请项目失败！",500);
            } else {
                throw new DatashopException("请不要重复申请同一个项目",401);
            }
        } else {
            return ResultUtil.handleResult(powerServer.create(newDpm),"申请项目失败！",500);
        }
    }

    /**
     * 往项目中添加成员
     * @param mapping
     * @return
     */
    @PostMapping("/addUser")
    public Map addUser(@RequestBody JSONObject mapping){
        DPowerMapping dpm = new DPowerMapping();
        dpm.setUserId((Integer) mapping.get("userId"));
        dpm.setProjectId((Integer) mapping.get("projectId"));
        dpm.setPower(1);
        dpm.setCreateTime(new Date().getTime());
        dpm.setUpdateTime(new Date().getTime());
        DPowerMapping d = powerServer.selectByUserAndProject(dpm.getUserId(),dpm.getProjectId());
        if(d == null) {
            return ResultUtil.handleResult(powerServer.create(dpm),"项目增加用户失败！",500);
        } else {
            if(d.getPower() == 0) {
                d.setPower(1);
                d.setUpdateTime(new Date().getTime());
                return ResultUtil.handleResult(powerServer.updateById(d),"项目增加用户失败！",500);
            } else if(d.getPower() == 1) {
                throw new DatashopException("用户已可使用项目，无需再次添加",401);
            } else {
                throw new DatashopException("用户已在申请列表中",401);
            }
        }
    }

    /**
     * 项目中所有成员的列表
     * @param projectId
     * @return
     */
    @GetMapping("/userList/{projectId}")
    public Map getProjectUserList(@PathVariable Integer projectId){
        return ResultUtil.handleResult(powerServer.getProjectUserList(projectId),"未获取到该项目下的用户列表",500);
    }

    /**
     * 查询我有权限的项目
     */
    @GetMapping("/myProjects")
    public Map getMyProjects(){
        Map<String,Object> cookie = CookieUtil.getCookie("bear",Map.class);
        Integer userId = new Integer((String) cookie.get("userId"));
        return ResultUtil.handleResult(powerServer.getMyProjects(userId),"未获取到项目列表",500);
    }
}
