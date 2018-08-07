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
@Table(name = "sys_resource")
public class SysResource extends BaseEntity {

    @Length(max = 50)
    @NotBlank(message = "{sysResource.name.notBlank}")
    private String name;

    @Length(max = 255)
    private String url;

    @Length(max = 100)
    private String path;

    @Length(max = 50)
    private String component;

    @Length(max = 50)
    private String permission;

    private Integer position;

    @Length(max = 50)
    @NotBlank(message = "{sysResource.type.notBlank}")
    private String type;

    @Length(max = 50)
    private String pid;

    @Length(max = 100)
    private String icon;

    @Column(name = "name", length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(name = "path", length = 100)
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Column(name = "component", length = 50)
    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    @Column(name = "permission", length = 50)
    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Column(name = "position", length = 11)
    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Column(name = "type", length = 50)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "pid", length = 50)
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    @Column(name = "icon", length = 100)
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
