package com.example;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.beans.Introspector;

/**
 * Created by tanvir on 1/2/17.
 */
public class PojoInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>, BeanDefinitionRegistryPostProcessor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String PACKAGE_NAME = "pojo";

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment springEnvironment = applicationContext.getEnvironment();
        applicationContext.addBeanFactoryPostProcessor(this);
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        Reflections ref = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
                .setUrls(ClasspathHelper.forPackage(PACKAGE_NAME))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(PACKAGE_NAME))));
        ref.getSubTypesOf(Object.class).stream()
                .forEach(clazz -> {
                    logger.info("Defining pojo bean: {} -> {}", Introspector.decapitalize(clazz.getSimpleName()), clazz.getCanonicalName());
                    registry.registerBeanDefinition(Introspector.decapitalize(clazz.getSimpleName()),
                            BeanDefinitionBuilder.genericBeanDefinition(clazz).getBeanDefinition());
                });
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // we actually only add new beans, but do not post process the existing definitions
    }
}

