package com.leonds.blog.www.controller;

import com.leonds.blog.domain.dto.CommentsDto;
import com.leonds.blog.domain.entity.Tag;
import com.leonds.blog.www.service.FrontCommentsService;
import com.leonds.blog.www.service.FrontPostsService;
import com.leonds.blog.www.service.FrontTagService;
import com.leonds.core.IpUtils;
import com.leonds.core.orm.Page;
import com.leonds.core.orm.PageRequest;
import com.leonds.core.resp.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author Leon
 */
@RestController
@RequestMapping("/fpi")
public class ApiController {
    @Autowired
    private FrontTagService frontTagService;
    @Autowired
    private FrontPostsService frontPostsService;
    @Autowired
    private FrontCommentsService frontCommentsService;

    @GetMapping("/tags")
    public List<Tag> getAll() {
        return frontTagService.getAll();
    }

    @GetMapping("/rpt")
    public Map<String, Object> getIndexCount() {
        return frontPostsService.getIndexCount();
    }

    @GetMapping("/links")
    public List<Map<String, Object>> getLinks() {
        return frontPostsService.getLinks();
    }

    @PostMapping("/comments")
    public Response comments(@RequestBody CommentsDto commentsDto, HttpServletRequest request) {
        commentsDto.setIp(IpUtils.getIp(request));
        frontCommentsService.save(commentsDto);
        return Response.ok().build();
    }

    @GetMapping("/comments/list")
    public Page<Map<String, Object>> commentsList(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "15") int size,
                                                  String subjectId) {
        return frontCommentsService.getComments(subjectId, PageRequest.of(page, size));
    }

    @GetMapping("/comments/count")
    public int getCommentsCount(String subjectId) {
        return frontCommentsService.getCommentsCount(subjectId);
    }


}
