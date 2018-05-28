package com.datashop.mapper;

import com.datashop.domain.DProject;

import java.util.List;
import java.util.Map;

public interface DProjectMapper {
    int deleteById(Integer id);

    int insert(DProject record);

    DProject selectById(Integer id);

    List<DProject> selectAll();

    int updateById(DProject record);

    List<DProject> page(Map map);

    int total(Map map);
}