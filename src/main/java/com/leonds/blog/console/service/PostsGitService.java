package com.leonds.blog.console.service;

import com.leonds.blog.domain.dto.PostsFiles;

import java.util.List;

/**
 * @author Leon
 */
public interface PostsGitService {
    void clonePosts();

    List<PostsFiles> getPosts(String dir);
}
