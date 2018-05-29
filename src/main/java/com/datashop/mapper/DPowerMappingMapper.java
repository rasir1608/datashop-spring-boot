package com.datashop.mapper;

import com.datashop.domain.DPowerMapping;
import com.datashop.domain.DProject;
import com.datashop.domain.DUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DPowerMappingMapper {
    int deleteById(Integer id);

    int insert(DPowerMapping record);

    DPowerMapping findById(Integer id);

    List<DPowerMapping> selectAll();

    int updateById(DPowerMapping record);

    List<Map> selectUsersByProject(@Param("projectId") Integer projectId,@Param("power") Integer power);

    List<Map> selectProjectsByUser(@Param("userId") Integer userId, @Param("power") Integer power);

    Boolean deleteByUserAndProject(@Param("userId") Integer userId,@Param("projectId") Integer projectId);

    Boolean deleteMappingByProject(@Param("userId") Integer userId);

    Boolean deleteMappingByUser(@Param("projectId") Integer projectId);

    DPowerMapping findByUserAndProject(@Param("userId") Integer userId,@Param("projectId") Integer projectId);
}