package com.leonds.console.service;

import com.leonds.domain.entity.SysUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * @author Leon
 */
public interface SysUserService {
    SysUser save(SysUser sysUser, List<String> roleIds);

    SysUser getById(String id);

    Page<SysUser> getPage(PageRequest pageRequest, String text, String status);

    void remove(List<String> ids);

    void enable(List<String> ids);

    void disable(List<String> ids);

    void resetPassword(SysUser sysUser);
}
