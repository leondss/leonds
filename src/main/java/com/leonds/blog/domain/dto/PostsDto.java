package com.leonds.blog.domain.dto;

import com.leonds.blog.domain.entity.Posts;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Leon
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PostsDto {
    private List<String> category;
    private List<String> tag;
    private Posts posts;
}
