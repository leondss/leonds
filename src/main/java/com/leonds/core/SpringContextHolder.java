package com.leonds.core;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Leon
 */
@Component
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext context;


    public void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        checkApplicationContext();
        return context;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        checkApplicationContext();
        return (T) context.getBean(name);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) {
        checkApplicationContext();
        @SuppressWarnings("rawtypes")
        Map beanMaps = context.getBeansOfType(clazz);
        if (!beanMaps.isEmpty()) {
            return (T) beanMaps.values().iterator().next();
        } else {
            throw new ServiceException("Bean is not found: " + clazz.getName());
        }
    }

    private static void checkApplicationContext() {
        if (context == null) {
            throw new IllegalStateException();
        }
    }

}
