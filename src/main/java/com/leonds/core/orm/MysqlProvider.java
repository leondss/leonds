package com.leonds.core.orm;


import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * @author Leon
 */
public class MysqlProvider {

    public String getById(Map<String, Object> params) {
        Class entityClass = (Class) params.get("entityClass");
        final String tableName = EntityUtils.getTableName(entityClass);
        final String idName = EntityUtils.getIdName(entityClass);
        final String queryFields = getQueryField(entityClass);

        return new SQL() {{
            SELECT(queryFields);
            FROM(tableName);
            WHERE(idName + " = #{id}");
        }}.toString();
    }

    public <T extends BaseEntity> String update(T entity) {
        Class clazz = entity.getClass();
        final String tableName = EntityUtils.getTableName(clazz);
        final List<Attribute> attributes = EntityUtils.getAttributes(clazz);
        return new SQL() {{
            UPDATE(tableName);
            for (Attribute attribute : attributes) {
                if (!attribute.getIsPk()) {
                    SET(attribute.getField() + "=#{" + attribute.getName() + "}");
                } else {
                    WHERE(attribute.getField() + "=#{" + attribute.getName() + "}");
                }
            }
        }}.toString();
    }

    public <T extends BaseEntity> String insert(T entity) {
        Class clazz = entity.getClass();
        final String tableName = EntityUtils.getTableName(clazz);
        List<Attribute> attributes = EntityUtils.getAttributes(clazz);
        final StringBuilder columns = new StringBuilder();
        final StringBuilder values = new StringBuilder();
        for (Attribute attribute : attributes) {
            columns.append(",").append(attribute.getField());
            values.append(",").append("#{").append(attribute.getName()).append("}");
        }
        return new SQL() {{
            INSERT_INTO(tableName);
            VALUES(columns.substring(1), values.substring(1));
        }}.toString();
    }

    public String remove(Map<String, Object> params) {
        Class entityClass = (Class) params.get("entityClass");
        final String tableName = EntityUtils.getTableName(entityClass);
        final String idName = EntityUtils.getIdName(entityClass);
        return new SQL() {{
            DELETE_FROM(tableName);
            WHERE(idName + " = #{id}");
        }}.toString();
    }

    public String removeByCondition(Map<String, Object> params) {
        Class entityClass = (Class) params.get("entityClass");

        final String tableName = EntityUtils.getTableName(entityClass);
        final String whereSql = getWhereSql(params);


        return new SQL() {{
            DELETE_FROM(tableName);
            WHERE(" 1=1 " + whereSql);
        }}.toString();
    }


    @SuppressWarnings("unchecked")
    public String find(Map<String, Object> params) {
        Class entityClass = (Class) params.get("entityClass");

        final String tableName = EntityUtils.getTableName(entityClass);
        final String queryFields = getQueryField(entityClass);
        final String whereSql = getWhereSql(params);

        return new SQL() {{
            SELECT(queryFields);
            FROM(tableName);
            WHERE(" 1=1 " + whereSql);
        }}.toString();
    }

    @SuppressWarnings("unchecked")
    public String count(Map<String, Object> params) {
        Class entityClass = (Class) params.get("entityClass");

        final String tableName = EntityUtils.getTableName(entityClass);
        final String queryFields = "count(1)";
        final String whereSql = getWhereSql(params);

        return new SQL() {{
            SELECT(queryFields);
            FROM(tableName);
            WHERE(" 1=1 " + whereSql);
        }}.toString();
    }

    @SuppressWarnings("unchecked")
    public String findPage(Map<String, Object> params) {
        Class entityClass = (Class) params.get("entityClass");

        final String tableName = EntityUtils.getTableName(entityClass);
        final String queryFields = getQueryField(entityClass);
        final String whereSql = getWhereSql(params);
        String limitSql = "";
        String orderBySql = "";
        if (params.get("pageRequest") != null) {
            PageRequest pageRequest = (PageRequest) params.get("pageRequest");
            limitSql += " limit " + pageRequest.getOffset() + "," + pageRequest.getSize();
            if (StringUtils.isNotBlank(pageRequest.getOrderBy())) {
                orderBySql = " order by " + pageRequest.getOrderBy();
            }
        }
        return new SQL() {{
            SELECT(queryFields);
            FROM(tableName);
            WHERE(" 1=1 " + whereSql);
        }}.toString() + orderBySql + limitSql;
    }

    private String getQueryField(Class clazz) {
        final List<Attribute> attributes = EntityUtils.getAttributes(clazz);
        final StringBuilder queryFields = new StringBuilder();
        for (Attribute attribute : attributes) {
            queryFields.append(",");
            String field = attribute.getField();
            if (SqlReservedWords.containsWord(field)) {
                field = "`" + field + "`";
            }
            queryFields.append(field);
        }
        return queryFields.substring(1);
    }

    private String getWhereSql(Map<String, Object> params) {
        Object obj = params.get("condition");
        if (obj != null) {
            Condition condition = (Condition) obj;
            String whereClause = condition.getWhereClause();
            if (StringUtils.isNotBlank(whereClause)) {
                return " AND " + whereClause;
            }
        }
        return "";
    }
}
