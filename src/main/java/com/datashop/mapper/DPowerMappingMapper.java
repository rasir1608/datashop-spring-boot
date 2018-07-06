package com.datashop.mapper;

import com.datashop.domain.DPowerMapping;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DPowerMappingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DPowerMapping record);

    DPowerMapping selectByPrimaryKey(Integer id);

    List<DPowerMapping> selectAll();

    int updateByPrimaryKey(DPowerMapping record);

    DPowerMapping findByUserAndProject(@Param("userId") Integer userId, @Param("projectId") Integer projectId);

    List<DPowerMapping> selectUsersByProject(Integer projectId, Integer power);

    List<DPowerMapping> selectProjectsByUser(@Param("userId") Integer userId,@Param("projectId") Integer power);

    int deleteMappingByProject(Integer projectId);

    int deleteMappingByUser(@Param("userId") Integer userId);

    int deleteByUserAndProject(@Param("userId") Integer userId,@Param("projectId") Integer projectId);

    DPowerMapping findById(Integer id);

    int updateById(DPowerMapping dPowerMapping);

    int deleteById(Integer id);

    List<Map> queryAllMyApplingProjectList(@Param("userId") Integer userId);

    List<Map> queryAllApplyMineMappingList(@Param("userId") Integer userId);

    List<Map> getProjectUserList(@Param("projectId") Integer projectId);

    List<Map> getMyProjects(@Param("userId") Integer userId,@Param("name") String name,@Param("limit") Integer limit,@Param("offset") Integer offset);

    Integer getMyProjectsTotal(@Param("userId") Integer userId,@Param("name") String name,@Param("limit") Integer limit,@Param("offset") Integer offset);
}