package com.leonds.blog.console.service.impl;

import com.leonds.blog.console.service.PostsService;
import com.leonds.blog.domain.dto.PostsDto;
import com.leonds.blog.domain.entity.CategoryRel;
import com.leonds.blog.domain.entity.Posts;
import com.leonds.blog.domain.entity.TagRel;
import com.leonds.blog.domain.enums.PostsStatus;
import com.leonds.core.orm.*;
import com.leonds.core.utils.CheckUtils;
import com.leonds.core.utils.MessageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Leon
 */
@Service
public class PostsServiceImpl implements PostsService {

    @Autowired
    private PersistenceManager pm;

    @Override
    public Posts save(PostsDto dto) {
        Posts posts = dto.getPosts();
        CheckUtils.checkNotNull(posts, MessageUtils.get("posts.base.exists"));
        CheckUtils.checkObject(posts);
        Posts postsNew = pm.save(posts);

        pm.remove(CategoryRel.class, Filters.and(Filters.eq("posts_id", postsNew.getId())));
        for (String s : dto.getCategory()) {
            CategoryRel categoryRel = new CategoryRel();
            categoryRel.setPostsId(postsNew.getId());
            categoryRel.setCategoryId(s);
            pm.save(categoryRel);
        }

        pm.remove(TagRel.class, Filters.and(Filters.eq("posts_id", postsNew.getId())));
        for (String s : dto.getTag()) {
            TagRel tagRel = new TagRel();
            tagRel.setPostsId(postsNew.getId());
            tagRel.setTagId(s);
            pm.save(tagRel);
        }

        return posts;
    }

    @Override
    public Page<Posts> getPage(PageRequest pageRequest, String text) {
        Condition condition = null;
        if (StringUtils.isNotBlank(text)) {
            condition = Filters.and(
                    Filters.or(
                            Filters.like("title", text)
                    )
            );
        }
        return pm.findPage(Posts.class, pageRequest, condition);
    }

    @Override
    public void remove(String id) {
        Posts posts = get(id);
        if (posts.getStatus() == PostsStatus.DRAFT.getCode()) {
            pm.remove(Posts.class, id);
        }
    }

    @Override
    public Posts get(String id) {
        return pm.get(Posts.class, id);
    }
}
