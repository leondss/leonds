package com.leonds.config;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * @author Leon
 */
@Configuration
public class ValidatorConfig {
    @Bean
    public LocaleResolver localeResolver() {
        return new MyLocaleResolver();
    }

    @Bean
    public Validator validator() {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .messageInterpolator(new ResourceBundleMessageInterpolator(new PlatformResourceBundleLocator("i18n/messages")))
                .failFast(true)
                .buildValidatorFactory();
        return validatorFactory.getValidator();
    }
}
