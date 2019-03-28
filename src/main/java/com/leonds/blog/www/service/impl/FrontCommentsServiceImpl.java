package com.leonds.blog.www.service.impl;

import com.leonds.blog.domain.dto.CommentsDto;
import com.leonds.blog.domain.entity.Comments;
import com.leonds.blog.domain.entity.Posts;
import com.leonds.blog.domain.entity.Users;
import com.leonds.blog.domain.enums.CommentsStatus;
import com.leonds.blog.www.service.FrontCommentsService;
import com.leonds.blog.www.service.FrontUsersService;
import com.leonds.core.orm.*;
import com.leonds.core.utils.CheckUtils;
import com.leonds.core.utils.MessageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Leon
 */
@Service
public class FrontCommentsServiceImpl implements FrontCommentsService {
    @Autowired
    private PersistenceManager pm;

    @Autowired
    private FrontUsersService frontUsersService;

    @Override
    public void save(CommentsDto commentsDto) {
        CheckUtils.checkNotBlank(commentsDto.getContent(), MessageUtils.get("comments.content.notBlank"));
        CheckUtils.checkNotBlank(commentsDto.getNickName(), MessageUtils.get("comments.nickName.notBlank"));
        CheckUtils.checkNotBlank(commentsDto.getEmail(), MessageUtils.get("comments.email.notBlank"));

        Users users = new Users();
        users.setEmail(commentsDto.getEmail());
        users.setNickName(commentsDto.getNickName());
        users.setSite(commentsDto.getSite());
        users.setIp(commentsDto.getIp());

        Users userInfo = frontUsersService.save(users);

        Comments comments = new Comments();
        comments.setContent(commentsDto.getContent());
        comments.setIp(commentsDto.getIp());
        comments.setPid(commentsDto.getPid());
        comments.setSubjectId(commentsDto.getSubjectId());
        comments.setSubjectType(commentsDto.getSubjectType());
        comments.setUid(userInfo.getId());
        comments.setStatus(CommentsStatus.ADD.getCode());
        pm.save(comments);

        if ("posts".equals(commentsDto.getSubjectType())) {
            Posts posts = pm.get(Posts.class, commentsDto.getSubjectId());
            if (posts != null) {
                posts.setCommentsCount(posts.getCommentsStatus() + 1);
                pm.save(posts);
            }
        }
        // 发送邮件

    }

    @Override
    public Page<Map<String, Object>> getComments(String subjectId, PageRequest pageRequest) {
        SqlParams params = SqlParams.instance();
        params.page(pageRequest);
        params.append("subjectId", subjectId);
        return pm.findPage("getComments", params);
    }

    @Override
    public int getCommentsCount(String subjectId) {
        if (StringUtils.isNotBlank(subjectId)) {
            return pm.count(Comments.class, Filters.and(Filters.eq("subject_id", subjectId), Filters.neq("status", CommentsStatus.UN_PASS.getCode())));
        }
        return 0;
    }
}
