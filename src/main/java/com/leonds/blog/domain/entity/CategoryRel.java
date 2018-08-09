package com.leonds.blog.domain.entity;

import com.leonds.core.orm.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Leon
 */
@Entity
@Table(name = "category_rel")
public class CategoryRel extends BaseEntity {

    private String categoryId;
    private String postsId;

    @Column(name = "category_id", length = 50)
    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @Column(name = "posts_id", length = 50)
    public String getPostsId() {
        return postsId;
    }

    public void setPostsId(String postsId) {
        this.postsId = postsId;
    }
}
