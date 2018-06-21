package com.datashop.controller;

import com.alibaba.fastjson.JSONObject;
import com.datashop.domain.DInterface;
import com.datashop.exception.DatashopException;
import com.datashop.server.inter.InterfaceServer;
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
        return ResultUtil.handleResult(interfaceServer.page(req),"获取接口列表失败",500);
    }

    /**
     * 删除接口
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public Map deleteById(@PathVariable Integer id){
        return ResultUtil.handleResult(interfaceServer.deleteById(id),"接口删除失败",500);
    }

    /**
     * 保存接口
     * @param req
     * @return
     */
    @PostMapping("/save")
    public Map saveInteface(@RequestBody JSONObject req){
        Integer id = req.getInteger("id");
        String name = req.getString("name");
        DInterface newDi = new DInterface();
        newDi.setName(name);
        newDi.setPath(req.getString("path"));
        newDi.setContentType(req.getString("contentType"));
        newDi.setMethod(req.getString("method"));
        newDi.setProject(req.getInteger("project"));
        newDi.setRemark(req.getString("remark"));
        newDi.setRequest(req.getString("request"));
        newDi.setResponse(req.getString("response"));
        newDi.setCreator(req.getInteger("creator"));
        newDi.setModifier(req.getInteger("modifier"));
        newDi.setUpdateTime(new Date().getTime());
        if(id == null) {
            DInterface di = interfaceServer.findByName(name);
            if(di != null) {
                throw new DatashopException("接口名称重复，请重新填写",401);
            } else {
                newDi.setCreateTime(new Date().getTime());
                return create(newDi);
            }
        } else {
            newDi.setId(id);
            return update(newDi);
        }
    }

    public Map create(DInterface dInterface){
        return ResultUtil.handleResult(interfaceServer.insert(dInterface),"接口保存失败",500);
    }

    public Map update(DInterface dInterface){
        return ResultUtil.handleResult(interfaceServer.updateById(dInterface),"接口更新失败",500);
    }

    /**
     * 获取接口详情
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    public Map getDetail(@PathVariable Integer id){
       return ResultUtil.handleResult(interfaceServer.findById(id),"获取接口详情失败",500);
    }

}
