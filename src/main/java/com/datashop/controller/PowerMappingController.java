package com.datashop.controller;

import com.alibaba.fastjson.JSONObject;
import com.datashop.domain.DPowerMapping;
import com.datashop.exception.DatashopException;
import com.datashop.server.inter.PowerMappingServer;
import com.datashop.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/powerMapping")
public class PowerMappingController {

    @Autowired
    private PowerMappingServer powerServer;

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
        reqPm.setUpdateTime(String.valueOf(new Date().getTime()));
        if(id == null){
            return createMapping(reqPm);
        } else {
            return updateMapping(reqPm);
        }
    }

    private Map createMapping(DPowerMapping dPowerMapping){
        DPowerMapping pme = powerServer.selectByUserAndProject(dPowerMapping.getUserId(),dPowerMapping.getProjectId());
        if(pme == null) {
            dPowerMapping.setCreateTime(String.valueOf(new Date().getTime()));
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

}