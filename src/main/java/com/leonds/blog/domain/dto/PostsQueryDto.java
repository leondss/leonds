package com.leonds.blog.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Leon
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PostsQueryDto {
    private String cate;
    private String tag;
    private int page;
    private int size;
}
