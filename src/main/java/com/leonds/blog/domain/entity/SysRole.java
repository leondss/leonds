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
@Table(name = "sys_role")
public class SysRole extends BaseEntity {

    @Length(max = 50)
    @NotBlank(message = "{sysRole.name.notBlank}")
    private String name;

    @Length(max = 100)
    private String description;

    @Column(name = "name", length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description", length = 100)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
