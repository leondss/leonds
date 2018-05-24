package com.leonds.domain.dto;

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
    private List<String> resourceIds;
}
