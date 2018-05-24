package com.datashop.mapper;

import com.datashop.domain.DInterface;
import java.util.List;

public interface DInterfaceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DInterface record);

    DInterface selectByPrimaryKey(Integer id);

    List<DInterface> selectAll();

    int updateByPrimaryKey(DInterface record);
}