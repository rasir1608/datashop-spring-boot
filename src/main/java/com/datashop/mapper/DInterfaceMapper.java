package com.datashop.mapper;

import com.datashop.domain.DInterface;

import java.util.List;
import java.util.Map;

public interface DInterfaceMapper {
    int deleteById(Integer id);

    int insert(DInterface record);

    DInterface findById(Integer id);

    List<DInterface> selectAll();

    int updateById(DInterface record);

    List<DInterface> page(Map map);

    int total(Map map);
}