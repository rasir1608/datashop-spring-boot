package com.datashop.server.impl;

import com.datashop.domain.DPowerMapping;
import com.datashop.exception.DatashopException;
import com.datashop.mapper.DPowerMappingMapper;
import com.datashop.server.inter.PowerMappingServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PowerMappingImpl implements PowerMappingServer {

    @Autowired
    private DPowerMappingMapper powerMapping;

    @Transactional
    @Override
    public DPowerMapping create(DPowerMapping dPowerMapping) {
        try {
            powerMapping.insert(dPowerMapping);
            return powerMapping.findByUserAndProject(dPowerMapping.getUserId(),dPowerMapping.getProjectId());
        } catch (Exception e) {

            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Override
    public List<DPowerMapping> queryUsersByProject(Integer projectId, Integer power) {
        try {
            return powerMapping.selectUsersByProject(projectId,power);
        } catch (Exception e) {
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Override
    public List<DPowerMapping> queryProjectsByUser(Integer userId,Integer power) {
        try {
            return powerMapping.selectProjectsByUser(userId,power);
        } catch (Exception e) {
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Override
    public Boolean deleteMappingByProject(Integer projectId) {
        try {
            powerMapping.deleteMappingByProject(projectId);
            return true;
        } catch (Exception e) {
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Override
    public Boolean deleteMappingByUser(Integer userId) {
        try {
            powerMapping.deleteMappingByUser(userId);
            return true;
        } catch (Exception e) {
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Override
    public Boolean deleteByUserAndProject(Integer userId, Integer projectId) {
        try {
            powerMapping.deleteByUserAndProject(userId,projectId);
            return true;
        } catch (Exception e) {
            throw new DatashopException(e.getMessage(),500);
        }
    }
    @Override
    public DPowerMapping selectByUserAndProject(Integer userId,Integer projectId){
        try {
            return powerMapping.findByUserAndProject(userId,projectId);
        } catch (Exception e) {
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Override
    public DPowerMapping findById(Integer id) {
        try {
            return powerMapping.findById(id);
        } catch (Exception e) {
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Override
    public DPowerMapping updateById(DPowerMapping dPowerMapping) {
        try {
            powerMapping.updateById(dPowerMapping);
            return powerMapping.findById(dPowerMapping.getId());
        } catch (Exception e) {
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Override
    public Boolean deleteById(Integer id) {
        try {
            powerMapping.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Override
    public List<Map> queryAllMyApplingProjectList(Integer userId) {
        try {
            return powerMapping.queryAllMyApplingProjectList(userId);
        } catch (Exception e) {
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Override
    public List<Map> queryAllApplyMineMappingList(Integer userId) {
        try {
            return powerMapping.queryAllApplyMineMappingList(userId);
        } catch (Exception e) {
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Override
    public List<Map> getProjectUserList(Integer projectId) {
        try {
            return powerMapping.getProjectUserList(projectId);
        } catch (Exception e) {
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Override
    public Map getMyProjects(Integer userId,String name ,Integer limit,Integer offset) {
        try {
            if(name != null){
                name = "%" + name +"%";
            }
            List projectList = powerMapping.getMyProjects(userId,name,limit,offset);
            Integer total = powerMapping.getMyProjectsTotal(userId,name,limit,offset);
            Map map = new HashMap();
            map.put("list",projectList);
            map.put("total",total);
            return map;
        } catch (Exception e) {
            throw new DatashopException(e.getMessage(),500);
        }
    }


}
