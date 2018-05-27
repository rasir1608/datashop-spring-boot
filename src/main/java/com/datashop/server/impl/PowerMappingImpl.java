package com.datashop.server.impl;

import com.datashop.domain.DPowerMapping;
import com.datashop.domain.DProject;
import com.datashop.domain.DUser;
import com.datashop.exception.DatashopException;
import com.datashop.mapper.DPowerMappingMapper;
import com.datashop.server.inter.PowerMappingServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PowerMappingImpl implements PowerMappingServer {

    @Autowired
    private DPowerMappingMapper powerMapping;

    @Override
    public DPowerMapping create(DPowerMapping dPowerMapping) {
        try {
            powerMapping.insert(dPowerMapping);
            return powerMapping.selectByUserAndProject(dPowerMapping.getUserId(),dPowerMapping.getProjectId());
        } catch (Exception e) {

            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Override
    public List<DUser> queryUsersByProject(Integer projectId) {
        try {
            return powerMapping.selectUsersByProject(projectId);
        } catch (Exception e) {
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Override
    public List<DProject> queryProjectsByUser(Integer userId) {
        try {
            return powerMapping.selectProjectsByUser(userId);
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
            return powerMapping.selectByUserAndProject(userId,projectId);
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


}
