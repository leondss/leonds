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
@Table(name = "category")
public class Category extends BaseEntity {

    @Length(max = 50)
    @NotBlank(message = "{category.name.notBlank}")
    private String name;

    private String sn;

    @Length(max = 50)
    private String pid;

    private int position;

    @Column(name = "name", length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "sn", length = 50)
    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    @Column(name = "pid", length = 50)
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    @Column(name = "position")
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
