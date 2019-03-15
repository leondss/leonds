package com.leonds.blog.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Leon
 */
@Data
@NoArgsConstructor
public class PostsFiles {
    private String name;
    private String path;
    private boolean dir;
    private long time;
}
