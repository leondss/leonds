package com.leonds.blog.console.service.impl;

import com.leonds.blog.console.service.PostsService;
import com.leonds.blog.console.service.SequenceService;
import com.leonds.blog.domain.dto.PostsDto;
import com.leonds.blog.domain.entity.CategoryRel;
import com.leonds.blog.domain.entity.Posts;
import com.leonds.blog.domain.entity.TagRel;
import com.leonds.blog.domain.enums.PostsStatus;
import com.leonds.blog.domain.enums.Sequence;
import com.leonds.core.ServiceException;
import com.leonds.core.orm.*;
import com.leonds.core.utils.CheckUtils;
import com.leonds.core.utils.MessageUtils;
import org.apache.commons.lang3.StringUtils;
import org.pegdown.PegDownProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Leon
 */
@Service
public class PostsServiceImpl implements PostsService {

    @Autowired
    private PersistenceManager pm;

    @Autowired
    private SequenceService sequenceService;

    @Override
    public Posts save(PostsDto dto) {
        Posts posts = dto.getPosts();
        CheckUtils.checkNotNull(posts, MessageUtils.get("posts.base.exists"));
        CheckUtils.checkObject(posts);

        if (StringUtils.isBlank(posts.getId())) {
            posts.setSn(sequenceService.getSequence(Sequence.SEQ_POSTS.name()));
            if (posts.getStatus() == PostsStatus.PUBLISH.getCode()) {
                posts.setPublishTime(new Date());
            }
        }
        if (StringUtils.isNotBlank(posts.getFilePath())) {
            try {

                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(posts.getFilePath()), StandardCharsets.UTF_8));
                String line;
                StringBuilder mdContent = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    mdContent.append(line).append("\r\n");
                }
                PegDownProcessor pdp = new PegDownProcessor(Integer.MAX_VALUE);
                String htmlContent = pdp.markdownToHtml(mdContent.toString());
                posts.setContentMd(mdContent.toString());
                posts.setContentHtml(htmlContent);
            } catch (IOException e) {
                throw new ServiceException("解析markdown出错");
            }
        }

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
            pm.remove(CategoryRel.class, Filters.and(Filters.eq("posts_id", id)));
            pm.remove(TagRel.class, Filters.and(Filters.eq("posts_id", id)));
            pm.remove(Posts.class, id);
        }
    }

    @Override
    public Posts get(String id) {
        return pm.get(Posts.class, id);
    }

    @Override
    public PostsDto getPostsDto(String id) {
        PostsDto dto = new PostsDto();
        dto.setPosts(get(id));

        List<CategoryRel> categories = pm.find(CategoryRel.class, Filters.and(Filters.eq("posts_id", id)));
        dto.setCategory(categories.stream().map(CategoryRel::getCategoryId).collect(Collectors.toList()));

        List<TagRel> tags = pm.find(TagRel.class, Filters.and(Filters.eq("posts_id", id)));
        dto.setTag(tags.stream().map(TagRel::getTagId).collect(Collectors.toList()));
        return dto;
    }

    @Override
    public void updatePostsStatus(String id, PostsStatus postsStatus) {
        Posts posts = get(id);
        if (posts != null) {
            posts.setStatus(postsStatus.getCode());
            if (postsStatus.getCode() == PostsStatus.PUBLISH.getCode()) {
                posts.setPublishTime(new Date());
            }
            pm.save(posts);
        }
    }
}
