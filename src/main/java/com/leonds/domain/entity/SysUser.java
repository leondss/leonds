package com.leonds.domain.entity;

import com.leonds.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Leon
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sys_user")
@Data
@NoArgsConstructor
public class SysUser extends BaseEntity {

    @Column(name = "username", length = 50)
    @Length(max = 50)
    @NotBlank(message = "{sysUser.username.notBlank}")
    private String username;

    @Column(name = "password")
    @NotBlank(message = "{sysUser.password.notBlank}")
    private String password;

    @Column(name = "status", length = 10)
    private String status;

}
