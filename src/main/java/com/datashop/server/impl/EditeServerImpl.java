package com.datashop.server.impl;

import com.datashop.domain.DEdite;
import com.datashop.exception.DatashopException;
import com.datashop.mapper.DEditeMapper;
import com.datashop.server.inter.EditeServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class EditeServerImpl implements EditeServer {

    @Autowired
    private DEditeMapper editeMapper;

    @Transactional
    @Override
    public Map insert(DEdite dEdite) {
        try{
            editeMapper.insert(dEdite);
            return editeMapper.getDetail(dEdite.getKind(),dEdite.getTarget());
        } catch (Exception e){
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Transactional
    @Override
    public Map updateById(DEdite dEdite) {
        try{
            editeMapper.updateById(dEdite);
            return editeMapper.getDetail(dEdite.getKind(),dEdite.getTarget());
        } catch (Exception e){
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Override
    public Map getDetail(Integer kind, Integer target) {
        try{
            return editeMapper.getDetail(kind,target);
        } catch (Exception e){
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Transactional
    @Override
    public Boolean deleteById(Integer id) {
        try{
            editeMapper.deleteById(id);
            return true;
        } catch (Exception e){
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Override
    public DEdite findById(Integer id) {
        try{

            return editeMapper.findById(id);
        } catch (Exception e){
            throw new DatashopException(e.getMessage(),500);
        }
    }
}
