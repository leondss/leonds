package com.leonds.blog.console.service;


import com.leonds.blog.domain.dto.PostsDto;
import com.leonds.blog.domain.entity.Posts;
import com.leonds.blog.domain.enums.PostsStatus;
import com.leonds.core.orm.Page;
import com.leonds.core.orm.PageRequest;

/**
 * @author Leon
 */
public interface PostsService {

    Posts save(PostsDto dto);

    Page<Posts> getPage(PageRequest pageRequest, String text);

    void remove(String id);

    Posts get(String id);

    PostsDto getPostsDto(String id);

    void updatePostsStatus(String id, PostsStatus postsStatus);
}
