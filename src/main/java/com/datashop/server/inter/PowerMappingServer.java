package com.datashop.server.inter;


import com.datashop.domain.DPowerMapping;
import com.datashop.domain.DProject;
import com.datashop.domain.DUser;

import java.util.List;
import java.util.Map;

/**
 * Created by rasir on 2018/5/24.
 */

public interface PowerMappingServer {

    DPowerMapping create(DPowerMapping dPowerMapping);

    List<Map> queryUsersByProject(Integer projectId,Integer power);

    List<Map> queryProjectsByUser(Integer userId,Integer power);

    Boolean deleteMappingByProject(Integer projectId);

    Boolean deleteMappingByUser(Integer userId);

    Boolean deleteByUserAndProject(Integer userId,Integer projectId);

    DPowerMapping selectByUserAndProject(Integer userId,Integer projectId);

    DPowerMapping findById(Integer id);

    DPowerMapping updateById(DPowerMapping dPowerMapping);

    Boolean deleteById(Integer id);
}
