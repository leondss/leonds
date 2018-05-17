package com.leonds.domain.entity;

import com.leonds.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Leon
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sys_role_resource")
@Data
@NoArgsConstructor
public class SysRoleResource extends BaseEntity {

    @Column(name = "role_id", length = 50)
    private String roleId;

    @Column(name = "resource_id", length = 50)
    private String resourceId;

}
