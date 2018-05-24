package com.datashop.mapper;

import com.datashop.domain.DPowerMapping;
import java.util.List;

public interface DPowerMappingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DPowerMapping record);

    DPowerMapping selectByPrimaryKey(Integer id);

    List<DPowerMapping> selectAll();

    int updateByPrimaryKey(DPowerMapping record);
}