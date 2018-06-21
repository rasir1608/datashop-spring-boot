package com.datashop.mapper;

import com.datashop.domain.DUser;
import java.util.List;

public interface DUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DUser record);

    DUser selectByPrimaryKey(Integer id);

    List<DUser> selectAll();

    int updateByPrimaryKey(DUser record);

    void deleteByUserId(Integer id);

    DUser findById(Integer id);

    int updateUserById(DUser record);

    DUser getUser(DUser temp);

    List<DUser> selectUserByName(String s);
}