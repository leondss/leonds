package com.leonds.domain.dto;

import com.leonds.domain.entity.SysRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Leon
 */
@Data
@NoArgsConstructor
public class SysRoleResourceDto {
    private SysRole sysRole;
    private List<String> resourceIds;
    private String roleId;
}
