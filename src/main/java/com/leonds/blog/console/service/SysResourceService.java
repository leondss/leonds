package com.leonds.blog.console.service;

import com.leonds.blog.domain.entity.SysResource;

import java.util.List;
import java.util.Map;

/**
 * @author Leon
 */
public interface SysResourceService {
    SysResource save(SysResource sysResource);

    SysResource getById(String id);

    void remove(List<String> ids);

    List<Map<String, Object>> getTreeByPid(String pid);

    SysResource getByName(String name);

    SysResource getByIdIsNotAndName(String id, String name);

    SysResource getByUrl(String url);
}
