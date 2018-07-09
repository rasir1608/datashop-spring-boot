package com.datashop.server.impl;

import com.datashop.domain.DEdite;
import com.datashop.domain.DPowerMapping;
import com.datashop.domain.DProject;
import com.datashop.exception.DatashopException;
import com.datashop.mapper.DEditeMapper;
import com.datashop.mapper.DPowerMappingMapper;
import com.datashop.mapper.DProjectMapper;
import com.datashop.server.inter.ProjectServer;
import com.datashop.utils.CookieUtil;
import com.datashop.utils.FileHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rasir on 2018/5/29.
 */

@Service
public class ProjectServerImpl implements ProjectServer {

    @Value("${edite.timeout}")
    private Integer editeTimeout;

    private Integer timeout;

    @PostConstruct
    public void init(){
        this.timeout = this.editeTimeout * 1000;
    }

    @Autowired
    private DProjectMapper projectMapper;

    @Autowired
    private DPowerMappingMapper powerMappingMapper;

    @Autowired
    private DEditeMapper editeMapper;

    @Transactional
    @Override
    public Map create(DProject dProject) {
        try{
            projectMapper.insert(dProject);
            DProject dp = projectMapper.findByName(dProject.getName());
            DPowerMapping dpm = new DPowerMapping();
            dpm.setProjectId(dp.getId());
            dpm.setUserId(dp.getCreator());
            dpm.setPower(5);
            dpm.setCreateTime(new Date().getTime());
            dpm.setUpdateTime(new Date().getTime());
            powerMappingMapper.insert(dpm);
            DEdite de = new DEdite();
            de.setEditor(dp.getCreator());
            de.setIslock(0);
            de.setKind(0);
            de.setTarget(dp.getId());
            de.setCreateTime(new Date().getTime());
            de.setUpdateTime(new Date().getTime());
            editeMapper.insert(de);
            return projectMapper.queryDetail(dp.getId(),dp.getCreator());
        } catch (Exception e) {
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Transactional
    @Override
    public Map update(DProject dProject) {
        try{
           projectMapper.updateById(dProject);
           DEdite de = editeMapper.getDetailByTarget(0,dProject.getId());
           de.setIslock(1);
           de.setUpdateTime(new Date().getTime());
           de.setEditor(dProject.getModifier());
           editeMapper.updateById(de);
           return projectMapper.queryDetail(dProject.getId(),dProject.getModifier());
        } catch (Exception e) {
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Transactional
    @Override
    public Boolean deleteById(Integer id) {
        try{
            DProject dp = projectMapper.findById(id);
//            从d_project表中删除
            projectMapper.deleteById(id);
//            从d_power_mapping表中删除所有的对应关系
            powerMappingMapper.deleteMappingByProject(id);
            Map de = editeMapper.getDetail(0,id);
//            从d_edite表中删除编辑状态
            editeMapper.deleteById((Integer) de.get("id"));
//            删除掉项目的UI、web、原型图片
            if(dp.getModel() != null) FileHandler.removeFile(dp.getModel());
            if(dp.getUi() != null) FileHandler.removeFile(dp.getUi());
            if(dp.getWeb() != null) FileHandler.removeFile(dp.getWeb());
            return true;
        } catch (Exception e) {
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Override
    public Map page(Map params) {
        try{
            List projectList = projectMapper.page(params);
            Integer total = projectMapper.total(params);
            Map<String,Object> map = new HashMap<>();
            map.put("list",projectList);
            map.put("total",total);
            return map;
        } catch (Exception e) {
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Override
    public DProject findByName(String name) {
        try{
            return projectMapper.findByName(name);
        } catch (Exception e) {
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Override
    public DProject findById(Integer id) {
        try{
            return projectMapper.findById(id);
        } catch (Exception e) {
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Transactional
    @Override
    public Map queryDetail(Integer projectId,Integer userId) {
        try{
            Map ret = projectMapper.queryDetail(projectId,userId);
            Integer islock = (Integer) ret.get("islock");
            if(islock != null && islock == 1) {
                Long editorTime = (Long) ret.get("lastEditeTime");
                if(new Date().getTime() - editorTime > timeout) {
                    DEdite de = editeMapper.getDetailByTarget(0,projectId);
                    de.setIslock(0);
                    editeMapper.updateById(de);
                }
            }
            return projectMapper.queryDetail(projectId,userId);
        } catch (Exception e) {
            throw new DatashopException(e.getMessage(),500);
        }
    }
}
