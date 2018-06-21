package com.datashop.mapper;

import com.datashop.domain.DProject;
import java.util.List;
import java.util.Map;

public interface DProjectMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DProject record);

    DProject selectByPrimaryKey(Integer id);

    List<DProject> selectAll();

    int updateByPrimaryKey(DProject record);

    DProject findByName(String name);

    int updateById(DProject dProject);

    DProject findById(Integer id);

    int deleteById(Integer id);

    List page(Map params);

    Integer total(Map params);

    Map queryDetail(Integer projectId);
}