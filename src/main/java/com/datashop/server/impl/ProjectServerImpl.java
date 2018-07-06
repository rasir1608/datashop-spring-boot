package com.datashop.server.impl;

import com.datashop.domain.DPowerMapping;
import com.datashop.domain.DProject;
import com.datashop.exception.DatashopException;
import com.datashop.mapper.DPowerMappingMapper;
import com.datashop.mapper.DProjectMapper;
import com.datashop.server.inter.ProjectServer;
import com.datashop.utils.CookieUtil;
import com.datashop.utils.FileHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rasir on 2018/5/29.
 */

@Service
public class ProjectServerImpl implements ProjectServer {

    @Autowired
    private DProjectMapper projectMapper;

    @Autowired
    private DPowerMappingMapper powerMappingMapper;

    @Transactional
    @Override
    public DProject create(DProject dProject, DPowerMapping dpm) {
        try{
            projectMapper.insert(dProject);
            DProject dp = projectMapper.findByName(dProject.getName());
            dpm.setProjectId(dp.getId());
            powerMappingMapper.insert(dpm);
            return dp;
        } catch (Exception e) {
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Override
    public DProject update(DProject dProject) {
        try{
           projectMapper.updateById(dProject);
           return projectMapper.findById(dProject.getId());
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

    @Override
    public Map queryDetail(Integer projectId,Integer userId) {
        try{
            return projectMapper.queryDetail(projectId,userId);
        } catch (Exception e) {
            throw new DatashopException(e.getMessage(),500);
        }
    }
}
