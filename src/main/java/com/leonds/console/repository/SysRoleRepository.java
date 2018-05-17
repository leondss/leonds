package com.leonds.console.repository;

import com.leonds.domain.entity.SysRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Leon
 */
public interface SysRoleRepository extends PagingAndSortingRepository<SysRole, String> {
    Page<SysRole> findAll(Specification<SysRole> spec, Pageable pageable);

    SysRole findByName(String name);

    SysRole findByIdIsNotAndName(String id, String name);
}
