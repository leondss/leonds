package com.leonds.core.orm;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * @author Leon
 */
public final class Filters {
    private Filters() {
    }

    public static <T> Condition eq(String fieldName, T value) {
        return new OperatorFilter<>(fieldName, value, "=");
    }

    public static <T> Condition neq(String fieldName, T value) {
        return new OperatorFilter<>(fieldName, value, "<>");
    }

    public static Condition nill(String fieldName) {
        return new NullOperatorFilter(fieldName, "IS NULL");
    }

    public static Condition notNill(String fieldName) {
        return new NullOperatorFilter(fieldName, "IS NOT NULL");
    }

    public static Condition blank(String fieldName) {
        return new NullOperatorFilter(fieldName, " = ''");
    }

    public static Condition notBlank(String fieldName) {
        return new NullOperatorFilter(fieldName, " <> ''");
    }

    public static Condition like(String fieldName, String value) {
        return new OperatorFilter<>(fieldName, "%" + value + "%", "like");
    }

    public static <T> Condition le(String fieldName, T value) {
        return new OperatorFilter<>(fieldName, value, "<=");
    }

    public static <T> Condition lt(String fieldName, T value) {
        return new OperatorFilter<>(fieldName, value, "<");
    }

    public static <T> Condition ge(String fieldName, T value) {
        return new OperatorFilter<>(fieldName, value, ">=");
    }

    public static <T> Condition gt(String fieldName, T value) {
        return new OperatorFilter<>(fieldName, value, ">");
    }

    public static <T> Condition in(String fieldName, T... value) {
        return in(fieldName, Arrays.asList(value));
    }

    public static <T> Condition in(String fieldName, Iterable<T> value) {
        return new InOperatorFilter<>(fieldName, value, "IN");
    }

    public static <T> Condition nin(String fieldName, T... value) {
        return nin(fieldName, Arrays.asList(value));
    }

    public static <T> Condition nin(String fieldName, Iterable<T> value) {
        return new InOperatorFilter<>(fieldName, value, "NOT IN");
    }

    public static Condition and(Condition... value) {
        return and(Arrays.asList(value));
    }

    public static Condition and(Iterable<Condition> value) {
        return new AndOrFilter(value, "AND");
    }

    public static Condition or(Condition... value) {
        return or(Arrays.asList(value));
    }

    public static Condition or(Iterable<Condition> value) {
        return new AndOrFilter(value, "OR");
    }


    private static final class OperatorFilter<T> implements Condition {

        private final String fieldName;
        private final T value;
        private final String operator;

        private OperatorFilter(String fieldName, T value, String operator) {
            this.fieldName = fieldName;
            this.operator = operator;
            this.value = value;
        }

        @Override
        public String getWhereClause() {
            return fieldName + " " + operator + " " + convertValue(value);
        }
    }

    private static final class NullOperatorFilter implements Condition {

        private final String fieldName;
        private final String operator;

        private NullOperatorFilter(String fieldName, String operator) {
            this.fieldName = fieldName;
            this.operator = operator;
        }

        @Override
        public String getWhereClause() {
            return fieldName + " " + operator;
        }
    }

    private static final class InOperatorFilter<T> implements Condition {

        private final String fieldName;
        private final Iterable<T> value;
        private final String operator;

        public InOperatorFilter(String fieldName, Iterable<T> value, String operator) {
            this.fieldName = fieldName;
            this.value = value;
            this.operator = operator;
        }

        @Override
        public String getWhereClause() {
            StringBuilder result = new StringBuilder();
            for (T i : value) {
                result.append(",").append(convertValue(i));
            }
            return fieldName + " " + operator + " (" + result.substring(1) + ")";
        }
    }


    private static final class AndOrFilter implements Condition {

        private final Iterable<Condition> filters;
        private final String operator;

        private AndOrFilter(Iterable<Condition> filters, String operator) {
            this.filters = filters;
            this.operator = operator;
        }

        @Override
        public String getWhereClause() {
            StringBuilder result = new StringBuilder();
            for (Condition filter : filters) {
                result.append(operator).append(" ").append(filter.getWhereClause()).append(" ");
            }
            if (result.length() > 0) {
                return " (" + result.substring(operator.length()) + ") ";
            }
            return "";
        }
    }


    private static String convertValue(Object value) {
        if (value instanceof String) {
            return StringUtils.isNotBlank(value.toString()) ? "'" + value.toString() + "'" : "";
        }
        return value.toString();
    }

}
