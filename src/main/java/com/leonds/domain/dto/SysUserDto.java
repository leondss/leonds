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
@Builder
@AllArgsConstructor
public class SysUserDto {
    private String id;
    private String username;
    private String password;
    private String status;
    private List<String> roleIds;
}
