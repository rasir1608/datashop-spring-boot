package com.datashop.server.impl;

import com.datashop.domain.DUser;
import com.datashop.exception.DatashopException;
import com.datashop.mapper.DUserMapper;
import com.datashop.server.inter.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by rasir on 2018/5/24.
 */
@Service
public class UserServiceImpl implements UserServer {

    @Autowired
    private DUserMapper userMapper;

    @Override
    public Boolean deleteById(Integer id) {
        try {
             userMapper.deleteByUserId(id);
             return true;
        } catch (Exception e){
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Override
    public DUser getUserById(Integer id) {
        return userMapper.findById(id);
    }

    @Override
    public List<DUser> selectAll() {
        try {
            return userMapper.selectAll();
        } catch (Exception e){
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Override
    public DUser updateById(DUser record) {
        try{
            userMapper.updateUserById(record);
            DUser retUser = userMapper.findById(record.getId());
            retUser.setPassword(null);
            return retUser;
        } catch (Exception e) {
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Override
    public DUser getUser(DUser user) {
        DUser temp = new DUser();
        if(user.getId() != null) temp.setId(user.getId());
        else if(user.getAccount() != null) temp.setAccount(user.getAccount());
        else if(user.getName() != null) temp.setName(user.getName());
        else throw new DatashopException("需要用户ID或者name或者帐号进行查询",400);
        try {
            return userMapper.getUser(temp);
        } catch (Exception e){
            throw new DatashopException(e.getMessage(),500);
        }
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
    public List<DUser> getUserListByName(String name) {
        try {
            return userMapper.selectUserByName(name);
        } catch (Exception e){
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Override
    public DUser getUserByAccount(String account) {
        DUser temp = new DUser();
        temp.setAccount(account);
        return userMapper.getUser(temp);
    }

}
