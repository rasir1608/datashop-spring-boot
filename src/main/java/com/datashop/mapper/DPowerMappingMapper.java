package com.datashop.mapper;

import com.datashop.domain.DPowerMapping;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DPowerMappingMapper {
    int deleteById(Integer id);

    int insert(DPowerMapping record);

    DPowerMapping findById(Integer id);

    List<DPowerMapping> selectAll();

    int updateById(DPowerMapping record);

    List<DPowerMapping> selectUsersByProject(@Param("projectId") Integer projectId,@Param("power") Integer power);

    List<DPowerMapping> selectProjectsByUser(@Param("userId") Integer userId, @Param("power") Integer power);

    Boolean deleteByUserAndProject(@Param("userId") Integer userId,@Param("projectId") Integer projectId);

    Boolean deleteMappingByProject(@Param("userId") Integer userId);

    Boolean deleteMappingByUser(@Param("projectId") Integer projectId);

    DPowerMapping findByUserAndProject(@Param("userId") Integer userId,@Param("projectId") Integer projectId);

    List<Map> queryAllMyApplingProjectList(@Param("userId") Integer userId);

    List<Map> queryAllApplyMineMappingList(@Param("userId") Integer userId);

    List<Map> getProjectUserList(@Param("projectId") Integer projectId);

    List<Map> getMyProjects(@Param("userId") Integer userId);

}