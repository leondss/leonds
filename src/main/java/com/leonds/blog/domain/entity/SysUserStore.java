package com.leonds.blog.domain.entity;

import com.leonds.core.orm.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Leon
 */
@Entity
@Table(name = "sys_user_store")
public class SysUserStore extends BaseEntity {

    private String userId;

    private String storeId;

    @Column(name = "user_id", length = 50)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name = "store_id", length = 50)
    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}
