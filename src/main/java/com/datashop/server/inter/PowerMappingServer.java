package com.datashop.server.inter;


import com.datashop.domain.DPowerMapping;

import java.util.List;

/**
 * Created by rasir on 2018/5/24.
 */

public interface PowerMappingServer {

    DPowerMapping create(DPowerMapping dPowerMapping);

    List<DPowerMapping> queryUsersByProject(Integer projectId,Integer power);

    List<DPowerMapping> queryProjectsByUser(Integer userId,Integer power);

    Boolean deleteMappingByProject(Integer projectId);

    Boolean deleteMappingByUser(Integer userId);

    Boolean deleteByUserAndProject(Integer userId,Integer projectId);

    DPowerMapping selectByUserAndProject(Integer userId,Integer projectId);

    DPowerMapping findById(Integer id);

    DPowerMapping updateById(DPowerMapping dPowerMapping);

    Boolean deleteById(Integer id);
}
