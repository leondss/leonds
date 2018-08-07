package com.leonds.core.orm;

/**
 * @author Leon
 */
public class Attribute {
    private String name;
    private String field;
    private Class<?> type;
    private boolean isPk;
    private boolean isNull;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public boolean getIsPk() {
        return isPk;
    }

    public void setIsPk(boolean pk) {
        isPk = pk;
    }

    public boolean getIsNull() {
        return isNull;
    }

    public void setIsNull(boolean aNull) {
        isNull = aNull;
    }
}
