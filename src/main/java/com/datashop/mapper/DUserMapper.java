package com.datashop.mapper;

import com.datashop.domain.DUser;

import java.util.List;

public interface DUserMapper {
    int deleteByUserId(Integer id);

    int insert(DUser record);

    DUser selectUserById(Integer id);

    List<DUser> selectAll();

    int updateUserById(DUser record);

    DUser getUser(DUser user);
}