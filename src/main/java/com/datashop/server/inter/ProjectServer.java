package com.datashop.server.inter;

import com.datashop.domain.DPowerMapping;
import com.datashop.domain.DProject;

import java.util.List;
import java.util.Map;

public interface ProjectServer {

    DProject create(DProject dProject,DPowerMapping dpm);

    DProject update(DProject dProject);

    Boolean deleteById(Integer id);

    Map page(Map params);

    DProject findByName(String name);

    DProject findById(Integer id);

    Map queryDetail(Integer projectId,Integer userId);
}
