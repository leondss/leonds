package com.leonds.core;

import com.leonds.core.orm.OperatorService;
import com.leonds.core.security.User;
import com.leonds.core.security.UserManager;

/**
 * @author Leon
 */
public class OperatorServiceImpl implements OperatorService {
    @Override
    public String getOperatorId() {
        User user = UserManager.getAuthentication();
        if (user != null) {
            return user.getId();
        }
        return null;
    }

    @Override
    public String getOperatorName() {
        User user = UserManager.getAuthentication();
        if (user != null) {
            return user.getRealName();
        }
        return null;
    }
}
