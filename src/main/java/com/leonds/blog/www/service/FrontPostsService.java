package com.leonds.blog.www.service;

import com.leonds.blog.domain.dto.PostsQueryDto;
import com.leonds.core.orm.Page;

import java.util.Map;

/**
 * @author Leon
 */
public interface FrontPostsService {
    Page<Map<String, Object>> getPostsPage(PostsQueryDto postsQueryDto);

    Map<String, Object> getDetail(String id);

    void addViewCount(String id, String ip);

}
