package com.datashop.controller;

import com.alibaba.fastjson.JSONObject;
import com.datashop.domain.DEdite;
import com.datashop.server.inter.EditeServer;
import com.datashop.utils.CookieUtil;
import com.datashop.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/dedite")
public class EditeController {

    @Value("${edite.timeout}")
    private Integer editeTimeout;

    private Integer timeout;

    @Autowired
    private EditeServer editeServer;

    @PostConstruct
    public void init(){
        this.timeout = this.editeTimeout * 1000;
    }

    @PostMapping("/create")
    public Map createEdite(@RequestBody JSONObject req){
        Integer userId = CookieUtil.getUserId("bear");
        Integer target = req.getInteger("target");
        Integer kind = req.getInteger("kind");
        DEdite de = new DEdite();
        de.setEditor(userId);
        de.setKind(kind);
        de.setTarget(target);
        de.setIslock(1);
        de.setCreateTime(new Date().getTime());
        de.setUpdateTime(new Date().getTime());
        return ResultUtil.success(editeServer.insert(de));
    }

    @GetMapping("/unlock/{editeId}")
    public Map unlock(@PathVariable Integer editeId){
        DEdite edite = editeServer.findById(editeId);
        edite.setIslock(0);
        edite.setUpdateTime(new Date().getTime());
        return ResultUtil.success(editeServer.updateById(edite));
    }

    @GetMapping("/lock/{editeId}")
    public Map lock(@PathVariable Integer editeId){
        Integer userId = CookieUtil.getUserId("bear");
        DEdite edite = editeServer.findById(editeId);
        edite.setIslock(1);
        edite.setEditor(userId);
        edite.setUpdateTime(new Date().getTime());
        return ResultUtil.success(editeServer.updateById(edite));
    }

    @PostMapping("/detail")
    public Map getDetail(@RequestBody JSONObject req){
        Integer userId = CookieUtil.getUserId("bear");
        Integer target = req.getInteger("target");
        Integer kind = req.getInteger("kind");
        Map edite = editeServer.getDetail(kind,target);
        DEdite de = new DEdite();
        if(edite == null) {
            de.setEditor(userId);
            de.setKind(kind);
            de.setTarget(target);
            de.setIslock(0);
            de.setCreateTime(new Date().getTime());
            de.setUpdateTime(new Date().getTime());
            editeServer.insert(de);
        } else {
            Long updateTime = (Long) edite.get("updateTime");
            if(new Date().getTime() - updateTime > timeout){
                de = turn2DEdite(edite);
                de.setIslock(1);
                de.setUpdateTime(new Date().getTime());
                editeServer.updateById(de);
            }
        }
        return ResultUtil.success(editeServer.getDetail(kind,target));
    }

    public DEdite turn2DEdite(Map edite){
        DEdite de = new DEdite();
        de.setId((Integer) edite.get("id"));
        de.setEditor((Integer) edite.get("editor"));
        de.setKind((Integer) edite.get("kind"));
        de.setTarget((Integer) edite.get("target"));
        de.setIslock((Integer) edite.get("islock"));
        de.setCreateTime((Long) edite.get("createTime"));
        de.setUpdateTime((Long) edite.get("updateTime"));
        return de;
    }
}
