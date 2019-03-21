package com.leonds.blog.www.service.impl;

import com.leonds.blog.domain.dto.PostsQueryDto;
import com.leonds.blog.domain.entity.Category;
import com.leonds.blog.domain.entity.Posts;
import com.leonds.blog.domain.entity.PostsOptLog;
import com.leonds.blog.www.service.FrontPostsService;
import com.leonds.core.orm.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Leon
 */
@Service
public class FrontPostsServiceImpl implements FrontPostsService {
    @Autowired
    private PersistenceManager pm;

    @Override
    public Page<Map<String, Object>> getPostsPage(PostsQueryDto postsQueryDto) {
        SqlParams params = SqlParams.instance();
        params.page(PageRequest.of(postsQueryDto.getPage(), postsQueryDto.getSize()));
        params.append("cate", postsQueryDto.getCate());
        params.append("tag", postsQueryDto.getTag());
        return pm.findPage("getPostsPage", params);
    }

    @Override
    public Map<String, Object> getDetail(String id) {
        SqlParams params = SqlParams.instance();
        params.append("id", id);
        return pm.findOne("getPostsDetail", params);
    }

    @Override
    public void addViewCount(String id, String ip) {
        Posts posts = pm.get(Posts.class, id);
        if (posts != null) {
            List<PostsOptLog> viewLogs = pm.find(PostsOptLog.class, Filters.and(Filters.eq("posts_id", id), Filters.eq("ip", ip)));
            if (viewLogs.isEmpty()) {
                posts.setViewCount(posts.getViewCount() + 1);
                pm.save(posts);

                PostsOptLog postsOptLog = new PostsOptLog();
                postsOptLog.setPostsId(id);
                postsOptLog.setIp(ip);
                postsOptLog.setType(1);
                pm.save(postsOptLog);
            }
        }
    }

    @Override
    public Map<String, Object> getIndexCount() {
        return pm.findOne("getIndexCount", SqlParams.instance());
    }

    @Override
    public List<Map<String, Object>> getCategoryGroup() {
        return pm.find("getCategoryCount", SqlParams.instance());
    }

    @Override
    public int getCategoryCount() {
        return pm.count(Category.class, null);
    }
}
