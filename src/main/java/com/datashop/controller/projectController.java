package com.datashop.controller;

import com.alibaba.fastjson.JSONObject;
import com.datashop.domain.DProject;
import com.datashop.exception.DatashopException;
import com.datashop.server.inter.ProjectServer;
import com.datashop.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 * Created by rasir on 2018/5/29.
 */
@RestController
@RequestMapping("/project")
public class projectController {

    @Autowired
    private ProjectServer projectServer;

    public Map save(@RequestBody DProject dProject){
        if(dProject.getId() == null){
            return create(dProject);
        } else {
            return update(dProject);
        }
    }

    private Map create(DProject dProject){
        DProject exit = projectServer.findByName(dProject.getName());
        if(exit != null) {
            throw new DatashopException("已有同名项目存在，请重新命名后提交！",401);
        } else {
            dProject.setCreateTime(new Date().getTime());
            dProject.setUpdateTime(new Date().getTime());
            return ResultUtil.handleResult(projectServer.create(dProject),"项目信息保存失败！",500);
        }
    }
    private Map update(DProject dProject){
        DProject exit = projectServer.findById(dProject.getId());
        if(exit == null) {
            throw new DatashopException("要更新的项目不存在！",404);
        } else {
            dProject.setUpdateTime(new Date().getTime());
            return ResultUtil.handleResult(projectServer.create(dProject),"项目信息保存失败！",500);
        }
    }

    @PostMapping("/page")
    public Map page(@RequestBody JSONObject req){
        return ResultUtil.handleResult(projectServer.page(req),"项目的分页信息获取失败",500);
    }


}
