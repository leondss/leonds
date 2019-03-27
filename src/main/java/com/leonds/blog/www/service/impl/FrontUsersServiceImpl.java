package com.leonds.blog.www.service.impl;

import com.leonds.blog.domain.entity.Users;
import com.leonds.blog.www.service.FrontUsersService;
import com.leonds.core.orm.Filters;
import com.leonds.core.orm.PersistenceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Leon
 */
@Service
public class FrontUsersServiceImpl implements FrontUsersService {

    @Autowired
    private PersistenceManager pm;

    @Override
    public Users save(Users users) {
        Users user = pm.findOne(Users.class, Filters.and(Filters.eq("nick_name", users.getNickName()),
                Filters.eq("email", users.getEmail())));
        if (user != null) {
            return user;
        }
        return pm.save(users);
    }
}
