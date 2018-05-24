package com.datashop.mapper;

import com.datashop.domain.DProject;
import java.util.List;

public interface DProjectMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DProject record);

    DProject selectByPrimaryKey(Integer id);

    List<DProject> selectAll();

    int updateByPrimaryKey(DProject record);
}