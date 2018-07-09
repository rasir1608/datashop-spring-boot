package com.datashop.controller;

import com.alibaba.fastjson.JSONObject;
import com.datashop.domain.DInterface;
import com.datashop.exception.DatashopException;
import com.datashop.server.inter.InterfaceServer;
import com.datashop.utils.CookieUtil;
import com.datashop.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rasir on 2018/6/11.
 */
@RestController
@RequestMapping("/dinterface")
public class InterfaceController {

    @Autowired
    private InterfaceServer interfaceServer;

    /**
     * 获取所有接口的列表
     * @param req
     * @return
     */
    @PostMapping("/page")
    public Map pageInterface(@RequestBody JSONObject req){
        String name = req.getString("name");
        if(name != null){
            name = "%"+name+"%";
            req.put("name",name);
        }
        Integer page = req.getInteger("currentPage");
        Integer size = req.getInteger("size");
        req.put("offset",(page-1)*size);
        Integer userId = CookieUtil.getUserId("bear");
        req.put("userId",userId);
        return ResultUtil.handleResult(interfaceServer.page(req),"获取接口列表失败",200);
    }

    /**
     * 删除接口
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Map deleteById(@PathVariable Integer id){
        return ResultUtil.handleResult(interfaceServer.deleteById(id),"接口删除失败",200);
    }

    /**
     * 保存接口
     * @param req
     * @return
     */
    @PostMapping("/save")
    public Map saveInteface(@RequestBody JSONObject req){
        Integer id = req.getInteger("id");
        DInterface newDi = new DInterface();
        newDi.setId(id);
        newDi.setName(req.getString("name"));
        newDi.setPath(req.getString("path"));
        newDi.setContentType(req.getString("contentType"));
        newDi.setMethod(req.getString("method"));
        newDi.setProject(req.getInteger("project"));
        newDi.setRemarks(req.getString("remarks"));
        newDi.setRequest(req.getString("request"));
        newDi.setResponse(req.getString("response"));
        newDi.setCreator(req.getInteger("creator"));
        newDi.setModifier(req.getInteger("modifier"));
        newDi.setUpdateTime(new Date().getTime());
        newDi.setCreateTime(req.getLong("createTime"));
        if(id == null) {
            return create(newDi);
        } else {
            return update(newDi);
        }
    }

    public Map create(DInterface dInterface){
        DInterface di = interfaceServer.findByName(dInterface.getName());
        if(di != null) {
            throw new DatashopException("接口名称重复，请重新填写",200);
        } else {
            Integer userId = CookieUtil.getUserId("bear");
            dInterface.setCreateTime(new Date().getTime());
            dInterface.setUpdateTime(new Date().getTime());
            dInterface.setCreator(userId);
            dInterface.setModifier(userId);
            return ResultUtil.handleResult(interfaceServer.getDetail(dInterface.getId(),userId),"接口保存失败",200);
        }
    }

    public Map update(DInterface dInterface){
        Integer userId = CookieUtil.getUserId("bear");
        dInterface.setModifier(userId);
        dInterface.setUpdateTime(new Date().getTime());
        interfaceServer.updateById(dInterface);
        return ResultUtil.handleResult(interfaceServer.getDetail(dInterface.getId(),userId),"获取接口详情失败",200);
    }

    /**
     * 获取接口详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Map getDetail(@PathVariable Integer id){
        Integer userId = CookieUtil.getUserId("bear");
       return ResultUtil.handleResult(interfaceServer.getDetail(id,userId),"获取接口详情失败",200);
    }

}
