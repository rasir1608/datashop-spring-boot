package com.datashop.mapper;

import com.datashop.domain.DEdite;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DEditeMapper {

    int deleteById(Integer id);

    int insert(DEdite record);

    int updateById(DEdite record);

    Map getDetail(@Param("kind") Integer kind,@Param("target") Integer target);

}
