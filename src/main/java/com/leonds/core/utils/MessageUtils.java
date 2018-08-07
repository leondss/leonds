package com.leonds.core.utils;

import com.leonds.core.SpringContextHolder;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @author Leon
 */
public class MessageUtils {
    public static String get(String key) {
        MessageSource messageSource = SpringContextHolder.getBean(MessageSource.class);
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }

    public static String get(String key, Object[] values) {
        MessageSource messageSource = SpringContextHolder.getBean(MessageSource.class);
        return messageSource.getMessage(key, values, LocaleContextHolder.getLocale());
    }

}
