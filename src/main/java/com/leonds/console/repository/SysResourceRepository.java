package com.leonds.console.repository;

import com.leonds.domain.entity.SysResource;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Leon
 */
public interface SysResourceRepository extends PagingAndSortingRepository<SysResource, String> {
    SysResource findByName(String name);

    SysResource findByLink(String link);

    SysResource findByIdIsNotAndName(String id, String name);
}
