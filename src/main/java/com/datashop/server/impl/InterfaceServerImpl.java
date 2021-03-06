package com.datashop.server.impl;

import com.datashop.domain.DEdite;
import com.datashop.domain.DInterface;
import com.datashop.exception.DatashopException;
import com.datashop.mapper.DEditeMapper;
import com.datashop.mapper.DInterfaceMapper;
import com.datashop.server.inter.InterfaceServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rasir on 2018/6/19.
 */
@Service
public class InterfaceServerImpl implements InterfaceServer {

    @Value("${edite.timeout}")
    private Integer editeTimeout;

    private Integer timeout;

    @PostConstruct
    public void init(){
        this.timeout = this.editeTimeout * 1000;
    }


    @Autowired
    private DInterfaceMapper dInterfaceMapper;

    @Autowired
    private DEditeMapper editeMapper;

    @Transactional
    @Override
    public Boolean deleteById(Integer id) {
        try{
            dInterfaceMapper.deleteById(id);
            DEdite de = editeMapper.getDetailByTarget(1,id);
            editeMapper.deleteById(de.getId());
            return true;
        } catch (Exception e){
            e.printStackTrace();
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Transactional
    @Override
    public Map insert(DInterface record) {
        try{
            dInterfaceMapper.insert(record);
            DInterface di = dInterfaceMapper.findByName(record.getName());
            DEdite de = new DEdite();
            de.setEditor(di.getCreator());
            de.setIslock(0);
            de.setKind(1);
            de.setTarget(di.getId());
            de.setCreateTime(new Date().getTime());
            de.setUpdateTime(new Date().getTime());
            editeMapper.insert(de);
            return dInterfaceMapper.getDetail(di.getId(),di.getCreator());
        } catch (Exception e){
            e.printStackTrace();
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Override
    public DInterface findById(Integer id) {
        try{
            return dInterfaceMapper.findById(id);
        } catch (Exception e){
            e.printStackTrace();
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Override
    public DInterface findByName(String name) {
        try{
            return dInterfaceMapper.findByName(name);
        } catch (Exception e){
            e.printStackTrace();
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Override
    public List<DInterface> selectAll() {
        try{
            return dInterfaceMapper.selectAll();
        } catch (Exception e){
            e.printStackTrace();
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Transactional
    @Override
    public Boolean updateById(DInterface record) {
        try{
            dInterfaceMapper.updateById(record);
            DEdite de = editeMapper.getDetailByTarget(1,record.getId());
            de.setIslock(1);
            de.setUpdateTime(new Date().getTime());
            de.setEditor(record.getModifier());
            editeMapper.updateById(de);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Transactional
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
            e.printStackTrace();
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Override
    public int total(Map map) {
        try{
            Integer total = dInterfaceMapper.total(map);
            return total;
        } catch (Exception e){
            e.printStackTrace();
            throw new DatashopException(e.getMessage(),500);
        }
    }

    @Transactional
    @Override
    public Map getDetail(Integer interId, Integer userId) {
        try{
            Map ret = dInterfaceMapper.getDetail(interId,userId);
            Integer islock = (Integer) ret.get("islock");
            if(islock != null && islock == 1) {
                Long editorTime = (Long) ret.get("lastEditeTime");
                if(new Date().getTime() - editorTime > timeout) {
                    DEdite de = editeMapper.getDetailByTarget(1,interId);
                    de.setIslock(0);
                    editeMapper.updateById(de);
                }
            }

            return dInterfaceMapper.getDetail(interId,userId);
        } catch (Exception e){
            e.printStackTrace();
            throw new DatashopException(e.getMessage(),500);
        }
    }
}
