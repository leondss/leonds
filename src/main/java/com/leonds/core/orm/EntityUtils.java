package com.leonds.core.orm;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Leon
 */

public class EntityUtils {
    private static Map<String, List<Attribute>> attributes = new HashMap<>();

    private EntityUtils() {
    }

    public static String getTableName(Class clazz) {
        Table table = (Table) clazz.getAnnotation(Table.class);
        return table != null ? table.name() : clazz.getSimpleName();
    }

    public static String getIdName(Class clazz) {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Id.class)) {
                Column column = method.getAnnotation(Column.class);
                return column != null ? column.name() : method.getName().substring(3).toLowerCase();
            }
        }
        throw new RuntimeException("Not found ID column.");
    }

    public static <T> String getIdValue(T entity) {
        Method[] methods = entity.getClass().getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Id.class)) {
                try {
                    Object result = method.invoke(entity);
                    return result == null ? null : result.toString();
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        throw new RuntimeException("Can't get id value");
    }

    public static List<Attribute> getAttributes(Class clazz) {
        if (attributes.containsKey(clazz.getName())) {
            return attributes.get(clazz.getName());
        }
        List<Attribute> result = new ArrayList<>();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Column.class) && method.getName().startsWith("get")) {
                Column column = method.getAnnotation(Column.class);
                if (column != null) {
                    Attribute attribute = new Attribute();
                    attribute.setName(getAttributeName(method.getName()));
                    attribute.setField(column.name());
                    attribute.setType(method.getReturnType());
                    attribute.setIsPk(method.isAnnotationPresent(Id.class));
                    result.add(attribute);
                } else {
                    throw new RuntimeException("Not found the @Column annotation in method :" + method.getName());
                }
            }
        }
        attributes.put(clazz.getName(), result);
        return result;
    }

    private static String getAttributeName(String methodName) {
        String name = methodName.substring(3);
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    public static String getEntityMethodName(String action, String name) {
        return action + name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public static void main(String[] args) {
        String getUserName = getEntityMethodName("get", "name");
        System.out.println("getUserName = " + getUserName);
    }
}