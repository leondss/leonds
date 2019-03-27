package com.leonds.blog.www.service;

import com.leonds.blog.domain.dto.CommentsDto;
import com.leonds.blog.domain.entity.Comments;
import com.leonds.core.orm.Page;
import com.leonds.core.orm.PageRequest;

import java.util.Map;

/**
 * @author Leon
 */
public interface FrontCommentsService {
    Comments save(CommentsDto commentsDto);

    Page<Map<String, Object>> getComments(String subjectId, PageRequest pageRequest);
}
