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

/**
 * @author Leon
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sys_resource")
@Data
@NoArgsConstructor
public class SysResource extends BaseEntity {

    @Column(name = "name", length = 50)
    @Length(max = 50)
    @NotBlank(message = "{sysResource.name.notBlank}")
    private String name;

    @Column(name = "link", length = 200)
    @Length(max = 200)
    private String link;

    @Column(name = "permission", length = 50)
    @Length(max = 50)
    private String permission;

    @Column(name = "position", length = 11)
    private Integer position;

    @Column(name = "type", length = 50)
    @Length(max = 50)
    @NotBlank(message = "{sysResource.type.notBlank}")
    private String type;

    @Column(name = "pid", length = 50)
    @Length(max = 50)
    private String pid;
}
