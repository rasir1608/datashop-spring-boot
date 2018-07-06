package com.datashop.server.impl;

import com.datashop.domain.DInterface;
import com.datashop.exception.DatashopException;
import com.datashop.mapper.DInterfaceMapper;
import com.datashop.server.inter.InterfaceServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rasir on 2018/6/19.
 */
@Service
public class InterfaceServerImpl implements InterfaceServer {

    @Autowired
    private DInterfaceMapper dInterfaceMapper;

    @Override
    public Boolean deleteById(Integer id) {
        try{
            dInterfaceMapper.deleteById(id);
            return true;
        } catch (Exception e){
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Override
    public DInterface insert(DInterface record) {
        try{
            dInterfaceMapper.insert(record);
            DInterface di = dInterfaceMapper.findByName(record.getName());
            return di;
        } catch (Exception e){
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Override
    public DInterface findById(Integer id) {
        try{
            return dInterfaceMapper.findById(id);
        } catch (Exception e){
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Override
    public DInterface findByName(String name) {
        try{
            return dInterfaceMapper.findByName(name);
        } catch (Exception e){
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Override
    public List<DInterface> selectAll() {
        try{
            return dInterfaceMapper.selectAll();
        } catch (Exception e){
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Override
    public Boolean updateById(DInterface record) {
        try{
            dInterfaceMapper.updateById(record);
            return true;
        } catch (Exception e){
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Override
    public Map page(Map map) {
        try{
            List<Map> inList = dInterfaceMapper.page(map);
            Integer total = dInterfaceMapper.total(map);
            Map<String,Object> result = new HashMap<>();
            result.put("list",inList);
            result.put("total",total);
            return result;
        } catch (Exception e){
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Override
    public int total(Map map) {
        try{
            Integer total = dInterfaceMapper.total(map);
            return total;
        } catch (Exception e){
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Override
    public Map getDetail(Integer interId, Integer userId) {
        try{
            return dInterfaceMapper.getDetail(interId,userId);
        } catch (Exception e){
            throw new DatashopException(e.getMessage(),500);
        }
    }
}
