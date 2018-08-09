package com.leonds.blog.console.service;


import com.leonds.blog.domain.entity.Tag;

import java.util.List;

/**
 * @author Leon
 */
public interface TagService {

    Tag save(Tag tag);

    Tag getByName(String name);

    Tag getByIdIsNotAndName(String id, String name);

    Tag getById(String id);

    void remove(List<String> ids);

}
