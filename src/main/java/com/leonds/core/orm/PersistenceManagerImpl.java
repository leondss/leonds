package com.leonds.core.orm;

import com.leonds.core.orm.mapper.BaseMapper;
import com.leonds.core.security.User;
import com.leonds.core.security.UserManager;
import com.leonds.core.utils.CommonUtils;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.DirectFieldAccessor;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author Leon
 */
public class PersistenceManagerImpl extends SqlSessionDaoSupport implements PersistenceManager {

    @Resource
    private BaseMapper baseMapper;

    @Override
    public <T extends BaseEntity> T get(Class<T> entityClass, String id) {
        Map<String, Object> source = baseMapper.get(entityClass, id);
        return convert(source, entityClass);
    }

    @Override
    public <T extends BaseEntity> T save(T entity) {
        String id = EntityUtils.getIdValue(entity);
        if (id != null && !"".equals(id.trim())) {
            return update(entity);
        }
        return insert(entity);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BaseEntity> T insert(T entity) {
        String id = UUID.randomUUID().toString();
        entity.setId(id);
        entity.setCreationTime(new Date());
        User user = UserManager.getUser();
        entity.setOperator(user.getRealName());
        entity.setOperatorId(user.getId());
        if (baseMapper.insert(entity) > 0) {
            return get((Class<T>) entity.getClass(), id);
        }
        throw new PersistentException("insert error.");
    }

    @Override
    public <T extends BaseEntity> T update(T entity) {
        entity.setModifiedTime(new Date());
        if (baseMapper.update(entity) > 0) {
            return entity;
        }
        throw new PersistentException("update error.");
    }

    @Override
    public <T extends BaseEntity> void remove(Class<T> entityClass, String id) {
        baseMapper.remove(entityClass, id);
    }

    @Override
    public <T extends BaseEntity> void remove(Class<T> entityClass, Collection<String> ids) {
        for (String id : ids) {
            remove(entityClass, id);
        }
    }

    @Override
    public <T extends BaseEntity> void remove(Class<T> entityClass, Condition condition) {
        baseMapper.removeByCondition(entityClass, condition);
    }

    @Override
    public <T extends BaseEntity> List<T> findAll(Class<T> entityClass) {
        return find(entityClass, null);
    }

    @Override
    public <T extends BaseEntity> List<T> find(Class<T> entityClass, Condition condition) {
        List<Map<String, Object>> sources = baseMapper.find(entityClass, condition);
        List<T> targets = new ArrayList<>();
        for (Map<String, Object> source : sources) {
            targets.add(convert(source, entityClass));
        }
        return targets;
    }

    @Override
    public <T extends BaseEntity> T findOne(Class<T> entityClass, Condition condition) {
        List<T> result = find(entityClass, condition);
        return result != null && result.size() > 0 ? result.get(0) : null;
    }

    @Override
    public List<Map<String, Object>> find(String statement, SqlParams sqlParams) {
        return getSqlSession().selectList(statement, sqlParams.params());
    }

    @Override
    public <T> List<T> find(String statement, SqlParams sqlParams, Class<T> clazz) {
        List<Map<String, Object>> sources = find(statement, sqlParams);
        List<T> rows = new ArrayList<>();
        for (Map<String, Object> source : sources) {
            rows.add(CommonUtils.mapToBean(clazz, source));
        }
        return rows;
    }

    @Override
    public Map<String, Object> findOne(String statement, SqlParams sqlParams) {
        List<Map<String, Object>> result = find(statement, sqlParams);
        return result != null && result.size() > 0 ? result.get(0) : null;
    }

    @Override
    public Page<Map<String, Object>> findPage(String statement, SqlParams sqlParams) {
        int total = 0;
        sqlParams.count();
        List<Map<String, Object>> counts = getSqlSession().selectList(statement, sqlParams.params());
        if (counts != null) {
            Map<String, Object> countMap = counts.get(0);
            if (countMap != null && countMap.size() > 0) {
                if (countMap.get("count") != null) {
                    String countStr = countMap.get("count").toString();
                    total = Integer.parseInt(countStr);
                }
            }
        }
        List<Map<String, Object>> rows = new ArrayList<>();
        if (total > 0) {
            sqlParams.unCount();
            rows = find(statement, sqlParams);
        }
        return new QueryPageImpl<>(sqlParams.getPageNo(), sqlParams.getPageSize(), total, rows);
    }

    @Override
    public <T extends BaseEntity> Page<T> findPage(Class<T> entityClass, PageRequest pageRequest, Condition condition) {
        int count = baseMapper.count(entityClass, condition);
        List<Map<String, Object>> sources = baseMapper.findPage(entityClass, condition, pageRequest);
        List<T> rows = new ArrayList<>();
        for (Map<String, Object> source : sources) {
            rows.add(convert(source, entityClass));
        }
        return new QueryPageImpl<>(pageRequest.getPage(), pageRequest.getSize(), count, rows);
    }

    @Override
    public <T> Page<T> findPage(String statement, SqlParams sqlParams, Class<T> clazz) {
        Page<Map<String, Object>> page = findPage(statement, sqlParams);
        List<T> rows = new ArrayList<>();
        for (Map<String, Object> map : page.getRows()) {
            T t = CommonUtils.mapToBean(clazz, map);
            rows.add(t);
        }
        return new QueryPageImpl<>(page.getPage(), page.getSize(), page.getTotal(), rows);
    }

    private <T extends BaseEntity> T convert(Map<String, Object> source, Class<T> entityClass) {
        T target;
        try {
            target = entityClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new PersistentException(e);
        }
        DirectFieldAccessor directFieldAccessor = new DirectFieldAccessor(target);
        directFieldAccessor.registerCustomEditor(Date.class, new CustomDateEditor());
        directFieldAccessor.setPropertyValues(source);
        return target;
    }

}
