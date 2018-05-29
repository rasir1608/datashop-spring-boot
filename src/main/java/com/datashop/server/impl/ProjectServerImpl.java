package com.datashop.server.impl;

import com.datashop.domain.DProject;
import com.datashop.exception.DatashopException;
import com.datashop.mapper.DProjectMapper;
import com.datashop.server.inter.ProjectServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public DProject create(DProject dProject) {
        try{
            projectMapper.insert(dProject);
            return projectMapper.findByName(dProject.getName());
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

    @Override
    public Boolean deleteById(Integer id) {
        try{
            projectMapper.deleteById(id);
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
}
