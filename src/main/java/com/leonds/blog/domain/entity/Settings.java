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
@Table(name = "settings")
public class Settings extends BaseEntity {

    @Length(max = 100)
    @NotBlank(message = "{settings.name.notBlank}")
    private String name;

    @Length(max = 100)
    @NotBlank(message = "{settings.code.notBlank}")
    private String code;

    @Length(max = 500)
    @NotBlank(message = "{settings.value.notBlank}")
    private String value;

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
