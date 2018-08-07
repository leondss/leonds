package com.leonds.core.orm.mapper;

import com.leonds.core.orm.BaseEntity;
import com.leonds.core.orm.Condition;
import com.leonds.core.orm.MysqlProvider;
import com.leonds.core.orm.PageRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @author Leon
 */
public interface BaseMapper {

    @SelectProvider(
            type = MysqlProvider.class,
            method = "getById"
    )
    <T extends BaseEntity> Map<String, Object> get(@Param("entityClass") Class<T> entityClass,
                                                   @Param("id") String id);

    @InsertProvider(
            type = MysqlProvider.class,
            method = "insert"
    )
    <T extends BaseEntity> int insert(T entity);

    @UpdateProvider(
            type = MysqlProvider.class,
            method = "update"
    )
    <T extends BaseEntity> int update(T entity);

    @DeleteProvider(
            type = MysqlProvider.class,
            method = "remove"
    )
    <T extends BaseEntity> int remove(@Param("entityClass") Class<T> entityClass,
                                      @Param("id") String id);

    @DeleteProvider(
            type = MysqlProvider.class,
            method = "removeByCondition"
    )
    <T extends BaseEntity> int removeByCondition(@Param("entityClass") Class<T> entityClass,
                                                 @Param("condition") Condition condition);

    @SelectProvider(
            type = MysqlProvider.class,
            method = "find"
    )
    <T extends BaseEntity> List<Map<String, Object>> find(@Param("entityClass") Class<T> entityClass,
                                                          @Param("condition") Condition condition);

    @SelectProvider(
            type = MysqlProvider.class,
            method = "count"
    )
    <T extends BaseEntity> int count(@Param("entityClass") Class<T> entityClass,
                                     @Param("condition") Condition condition);

    @SelectProvider(
            type = MysqlProvider.class,
            method = "findPage"
    )
    <T extends BaseEntity> List<Map<String, Object>> findPage(@Param("entityClass") Class<T> entityClass,
                                                              @Param("condition") Condition condition,
                                                              @Param("pageRequest") PageRequest pageRequest);

}
