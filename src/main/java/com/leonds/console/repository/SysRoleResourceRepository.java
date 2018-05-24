package com.leonds.console.repository;

import com.leonds.domain.entity.SysRoleResource;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author Leon
 */
public interface SysRoleResourceRepository extends PagingAndSortingRepository<SysRoleResource, String> {
    void deleteByRoleId(String roleId);

    List<SysRoleResource> findByRoleId(String roleId);
}
