package com.datashop.mapper;

import com.datashop.domain.DUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DUserMapper {
    int deleteByUserId(Integer id);

    int insert(DUser record);

    DUser findById(Integer userId);

    List<DUser> selectAll();

    int updateUserById(DUser record);

    DUser getUser(DUser user);

    List<DUser> selectUserByName(String name);

}