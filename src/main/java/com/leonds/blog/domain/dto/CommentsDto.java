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
public class CommentsDto {
    private String subjectId;
    private String subjectType;
    private String nickName;
    private String email;
    private String site;
    private String content;
    private String pid;
    private String ip;
}
