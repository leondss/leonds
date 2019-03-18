package com.leonds.blog.console.service.impl;

import com.leonds.blog.console.service.PostsGitService;
import com.leonds.blog.domain.dto.PostsFiles;
import com.leonds.core.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Leon
 */
@Service
public class PostsGitServiceImpl implements PostsGitService {
    @Value("${app.git.remote}")
    private String remote;
    @Value("${app.git.local}")
    private String local;

    @Override
    public void clonePosts() {
        try {
            File file = new File(local);
            File gitFile = new File(file, ".git");
            if (gitFile.exists()) {
                Git.open(file).pull().call();
            } else {
                Git.cloneRepository().setURI(remote).setDirectory(file).call();
            }
        } catch (GitAPIException e) {
            throw new ServiceException("Clone出错：", e);
        } catch (IOException e) {
            throw new ServiceException("Pull出错：", e);
        }
    }

    @Override
    public List<PostsFiles> getPosts(String dir) {
        String root = StringUtils.isNotBlank(dir) ? dir : local;
        File file = new File(root);
        File[] files = file.listFiles(pathname -> !pathname.getName().equals(".git"));
        List<PostsFiles> rows = new ArrayList<>();
        if (files != null && files.length > 0) {
            for (File item : files) {
                PostsFiles postsFiles = new PostsFiles();
                postsFiles.setName(item.getName());
                postsFiles.setPath(item.getAbsolutePath());
                postsFiles.setDir(item.isDirectory());
                postsFiles.setTime(item.lastModified());
                rows.add(postsFiles);
            }
        }
        rows.sort((o1, o2) -> (int) (o2.getTime() - o1.getTime()));
        return rows;
    }
}
