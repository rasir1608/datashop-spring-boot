package com.datashop.server.impl;

import com.datashop.domain.DUser;
import com.datashop.exception.DatashopException;
import com.datashop.mapper.DUserMapper;
import com.datashop.server.inter.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by rasir on 2018/5/24.
 */
@Service(value = "userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private DUserMapper userMapper;

    @Override
    public int deleteById(Integer id) {
        return 0;
    }

    @Override
    public DUser getUserById(Integer id) {
        return userMapper.selectUserById(id);
    }

    @Override
    public List<DUser> selectAll() {
        return null;
    }

    @Override
    public DUser updateById(DUser record) {
        return null;
    }

    @Override
    public DUser getUser(DUser user) {
        return null;
    }

    @Override
    public DUser saveUser(DUser user) {
        try {
            userMapper.insert(user);
            user.setName(null);
            return userMapper.getUser(user);
        } catch (Exception e){
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Override
    public DUser login(DUser user) {
        return null;
    }

    @Override
    public List<DUser> getUserListByName(String name) {
        return null;
    }

    @Override
    public DUser getUserByAccount(String account) {
        DUser temp = new DUser();
        temp.setAccount(account);
        return userMapper.getUser(temp);
    }
}
