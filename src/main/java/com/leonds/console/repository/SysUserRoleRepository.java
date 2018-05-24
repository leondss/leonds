package com.leonds.console.repository;

import com.leonds.domain.entity.SysUserRole;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author Leon
 */
public interface SysUserRoleRepository extends PagingAndSortingRepository<SysUserRole, String> {
    void deleteByUserId(String userId);

    List<SysUserRole> findByUserId(String userId);
}
