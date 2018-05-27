package com.datashop.server.inter;

import com.datashop.domain.DUser;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by rasir on 2018/5/24.
 */

public interface UserServer {

    Boolean deleteById(Integer id);

    DUser getUserById(Integer id);

    List<DUser> selectAll();

    DUser updateById(DUser record);


    DUser getUser(DUser user);


    DUser saveUser(DUser user);

    DUser getUserByAccount(String account);

    List<DUser> getUserListByName(String name);
}
