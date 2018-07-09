package com.datashop.server.inter;

import com.datashop.domain.DEdite;

import java.util.Map;

public interface EditeServer {

    Map insert(DEdite dEdite);

    Map updateById(DEdite dEdite);

    Map getDetail(Integer kind,Integer target);

    Boolean deleteById(Integer id);

    DEdite findById(Integer id);

}
