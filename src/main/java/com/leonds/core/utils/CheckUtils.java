package com.leonds.core.utils;

import com.leonds.core.ServiceException;
import com.leonds.core.SpringContextHolder;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author Leon
 */
public class CheckUtils {
    private CheckUtils() {

    }

    public static <T> void checkNotNull(T input, String message) {
        if (input == null) {
            throw new ServiceException(message);
        }
    }

    public static void checkState(boolean input, String message) {
        if (!input) {
            throw new ServiceException(message);
        }
    }

    public static void checkNotBlank(String input, String message) {
        if (StringUtils.isBlank(input)) {
            throw new ServiceException(message);
        }
    }

    public static <T> void checkObject(T instance) {
        if (instance == null) {
            throw new ServiceException("Object not null.");
        }
        Validator validator = SpringContextHolder.getBean(Validator.class);
        Set<ConstraintViolation<T>> validate = validator.validate(instance);
        if (!validate.isEmpty()) {
            throw new ServiceException(validate.iterator().next().getMessage());
        }
    }
}
