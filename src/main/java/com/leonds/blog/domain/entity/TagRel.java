package com.leonds.blog.domain.entity;

import com.leonds.core.orm.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Leon
 */
@Entity
@Table(name = "tag_rel")
public class TagRel extends BaseEntity {

    private String tagId;
    private String postsId;

    @Column(name = "tag_id", length = 50)
    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    @Column(name = "posts_id", length = 50)
    public String getPostsId() {
        return postsId;
    }

    public void setPostsId(String postsId) {
        this.postsId = postsId;
    }
}
