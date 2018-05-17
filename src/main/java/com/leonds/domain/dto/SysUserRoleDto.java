package com.leonds.domain.dto;

import com.leonds.domain.entity.SysUser;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Leon
 */
@Data
@NoArgsConstructor
public class SysUserRoleDto {
    private SysUser sysUser;
    private List<String> roleIds;
    private String userId;
}
