package com.leonds.core.orm;

import org.apache.ibatis.session.SqlSession;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Leon
 */
public interface PersistenceManager {

    SqlSession getSqlSession();

    <T extends BaseEntity> T get(Class<T> entityClass, String id);

    <T extends BaseEntity> T save(T entity);

    <T extends BaseEntity> T insert(T entity);

    <T extends BaseEntity> T update(T entity);

    <T extends BaseEntity> void remove(Class<T> entityClass, String id);

    <T extends BaseEntity> void remove(Class<T> entityClass, Collection<String> ids);

    <T extends BaseEntity> void remove(Class<T> entityClass, Condition condition);

    <T extends BaseEntity> List<T> findAll(Class<T> entityClass);

    <T extends BaseEntity> List<T> find(Class<T> entityClass, Condition condition);

    <T extends BaseEntity> T findOne(Class<T> entityClass, Condition condition);

    List<Map<String, Object>> find(String statement, SqlParams sqlParams);

    <T> List<T> find(String statement, SqlParams sqlParams, Class<T> clazz);

    Map<String, Object> findOne(String statement, SqlParams sqlParams);

    Page<Map<String, Object>> findPage(String statement, SqlParams sqlParams);

    <T extends BaseEntity> Page<T> findPage(Class<T> entityClass, PageRequest pageRequest, Condition condition);

    <T> Page<T> findPage(String statement, SqlParams sqlParams, Class<T> clazz);
}
