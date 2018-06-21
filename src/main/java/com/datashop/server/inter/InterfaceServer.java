package com.datashop.server.inter;

import com.datashop.domain.DInterface;

import java.util.List;
import java.util.Map;

/**
 * Created by rasir on 2018/6/19.
 */
public interface InterfaceServer {

    Boolean deleteById(Integer id);

    DInterface insert(DInterface record);

    DInterface findById(Integer id);

    DInterface findByName(String name);

    List<DInterface> selectAll();

    Boolean updateById(DInterface record);

    Map page(Map map);

    int total(Map map);
}
