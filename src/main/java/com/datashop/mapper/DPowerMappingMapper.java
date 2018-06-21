package com.datashop.mapper;

import com.datashop.domain.DPowerMapping;
import java.util.List;
import java.util.Map;

public interface DPowerMappingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DPowerMapping record);

    DPowerMapping selectByPrimaryKey(Integer id);

    List<DPowerMapping> selectAll();

    int updateByPrimaryKey(DPowerMapping record);

    DPowerMapping findByUserAndProject(Integer userId, Integer projectId);

    List<DPowerMapping> selectUsersByProject(Integer projectId, Integer power);

    List<DPowerMapping> selectProjectsByUser(Integer userId, Integer power);

    int deleteMappingByProject(Integer projectId);

    int deleteMappingByUser(Integer userId);

    int deleteByUserAndProject(Integer userId, Integer projectId);

    DPowerMapping findById(Integer id);

    int updateById(DPowerMapping dPowerMapping);

    int deleteById(Integer id);

    List<Map> queryAllMyApplingProjectList(Integer userId);

    List<Map> queryAllApplyMineMappingList(Integer userId);

    List<Map> getProjectUserList(Integer projectId);

    List<Map> getMyProjects(Integer userId);
}