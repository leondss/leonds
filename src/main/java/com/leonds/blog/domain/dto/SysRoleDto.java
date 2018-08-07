package com.leonds.blog.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Leon
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysRoleDto {
    private String id;
    private String name;
    private String code;
    private String description;
    private List<String> resourceIds;
}
