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
@Table(name = "sys_role")
@Data
@NoArgsConstructor
public class SysRole extends BaseEntity {

    @Column(name = "name", length = 50)
    @Length(max = 50)
    @NotBlank(message = "{sysRole.name.notBlank}")
    private String name;

}
