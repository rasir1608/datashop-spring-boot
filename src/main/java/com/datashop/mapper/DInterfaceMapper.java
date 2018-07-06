package com.datashop.mapper;

import com.datashop.domain.DInterface;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DInterfaceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DInterface record);

    DInterface selectByPrimaryKey(Integer id);

    List<DInterface> selectAll();

    int updateByPrimaryKey(DInterface record);

    int updateById(DInterface record);

    DInterface findByName(String name);

    DInterface findById(Integer id);

    int deleteById(Integer id);

    List<Map> page(Map map);

    Integer total(Map map);

    Map getDetail(@Param("interId") Integer interId,@Param("userId") Integer userId);
}