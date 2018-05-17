package com.leonds.console.repository;

import com.leonds.domain.entity.SysUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Leon
 */
public interface SysUserRepository extends PagingAndSortingRepository<SysUser, String> {
    Page<SysUser> findAll(Specification<SysUser> spec, Pageable pageable);

    SysUser findByUsername(String username);

    SysUser findByIdIsNotAndUsername(String id, String username);
}
