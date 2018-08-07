
package com.leonds.core.orm;

import com.leonds.core.security.User;
import com.leonds.core.security.UserManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Leon
 */
public class SqlParams {

    private Map<String, Object> params = new HashMap<>();

    public static SqlParams instance() {
        return new SqlParams();
    }

    public SqlParams append(String key, Object value) {
        params.put(key, value);
        return this;
    }

    public SqlParams page(PageRequest pageRequest) {
        params.put("page", pageRequest);
        params.put("isPage", true);
        return this;
    }

    public SqlParams user() {
        User user = UserManager.getUser();
        params.put("userId", user.getId());
        return this;
    }

    public SqlParams count() {
        params.put("isCount", "Y");
        params.put("isPage", null);
        return this;
    }

    public SqlParams unCount() {
        params.put("isCount", null);
        params.put("isPage", "Y");
        return this;
    }

    public SqlParams orderByClause(String orderByClause) {
        params.put("orderByClause", orderByClause);
        return this;
    }

    public int getPageNo() {
        return params.get("pageNo") != null ? Integer.valueOf(params.get("pageNo").toString()) : 0;
    }

    public int getPageSize() {
        return params.get("pageSize") != null ? Integer.valueOf(params.get("pageSize").toString()) : 15;
    }

    public Map<String, Object> params() {
        return params;
    }
}
