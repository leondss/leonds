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
@Table(name = "sys_user_role")
@Data
@NoArgsConstructor
public class SysUserRole extends BaseEntity {

    @Column(name = "user_id", length = 50)
    private String userId;

    @Column(name = "role_id", length = 50)
    private String roleId;
}
