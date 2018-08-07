package com.leonds.blog.domain.entity;

import com.leonds.core.orm.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Leon
 */
@Entity
@Table(name = "sys_role_resource")
public class SysRoleResource extends BaseEntity {

    private String roleId;

    private String resourceId;

    @Column(name = "role_id", length = 50)
    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @Column(name = "resource_id", length = 50)
    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
}
