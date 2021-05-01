package com.w1sh.wave.core;

import com.w1sh.wave.condition.FilteringConditionalProcessor;
import com.w1sh.wave.condition.GenericFilteringConditionalProcessor;
import com.w1sh.wave.core.annotation.Component;
import com.w1sh.wave.core.annotation.Configuration;
import com.w1sh.wave.core.annotation.Profile;
import com.w1sh.wave.core.annotation.Provides;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.reflections.ReflectionUtils.getAllMethods;
import static org.reflections.ReflectionUtils.withAnnotation;

public class GenericComponentScanner implements ComponentScanner {

    private static final Logger logger = LoggerFactory.getLogger(GenericComponentScanner.class);

    private final FilteringConditionalProcessor conditionProcessor;
    private final Reflections reflections;
    private final String packagePrefix;
    private final Set<Class<?>> typesToIgnore = new HashSet<>();

    private AbstractApplicationEnvironment environment;

    public GenericComponentScanner(FilteringConditionalProcessor conditionProcessor, String packagePrefix) {
        this.reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packagePrefix))
                .setScanners(new TypeAnnotationsScanner(), new SubTypesScanner(), new MethodAnnotationsScanner())
                .useParallelExecutor());
        this.packagePrefix = packagePrefix;
        this.conditionProcessor = conditionProcessor != null ? conditionProcessor : new GenericFilteringConditionalProcessor(reflections);
    }

    public GenericComponentScanner(String packagePrefix) {
        this(null, packagePrefix);
    }

    @Override
    public Set<Class<?>> scanClasses() {
        logger.debug("Scanning in defined package \"{}\" for annotated classes", packagePrefix);
        final Set<Class<?>> componentClasses = reflections.getTypesAnnotatedWith(Component.class);
        componentClasses.removeAll(typesToIgnore);
        return conditionProcessor.processConditionals(componentClasses.stream()
                .filter(this::isProfileActive)
                .collect(Collectors.toSet()));
    }

    @Override
    public Set<Method> scanMethods() {
        logger.debug("Scanning in defined package \"{}\" for annotated methods", packagePrefix);
        final Set<Class<?>> configurationClasses = reflections.getTypesAnnotatedWith(Configuration.class);
        return configurationClasses.stream()
                .map(clazz -> getAllMethods(clazz, withAnnotation(Provides.class)))
                .flatMap(Collection::stream)
                .filter(this::isProfileActive)
                .collect(Collectors.toSet());
    }

    @Override
    public void ignoreType(Class<?> clazz) {
        typesToIgnore.add(clazz);
    }

    private boolean isProfileActive(AnnotatedElement annotatedElement){
        return annotatedElement.isAnnotationPresent(Profile.class) &&
                environment.getActiveProfiles().contains(annotatedElement.getAnnotation(Profile.class).value());
    }

    @Override
    public AbstractApplicationEnvironment getEnvironment() {
        return environment;
    }

    @Override
    public void setEnvironment(AbstractApplicationEnvironment environment) {
        this.environment = environment;
    }
}
