package com.leonds.blog.domain.entity;

import com.leonds.core.orm.BaseEntity;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * @author Leon
 */
@Entity
@Table(name = "sys_user")
public class SysUser extends BaseEntity {

    @Length(max = 50)
    @NotBlank(message = "{sysUser.username.notBlank}")
    private String username;

    @NotBlank(message = "{sysUser.password.notBlank}")
    private String password;

    private int status;

    @NotBlank(message = "{sysUser.realName.notBlank}")
    private String realName;

    @NotBlank(message = "{sysUser.mobile.notBlank}")
    private String mobile;

    @Column(name = "username", length = 50)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "status", length = 1)
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column(name = "real_name", length = 50)
    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    @Column(name = "mobile", length = 50)
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
