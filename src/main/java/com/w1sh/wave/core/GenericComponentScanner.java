package com.w1sh.wave.core;

import com.w1sh.wave.core.annotation.Component;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class GenericComponentScanner implements ComponentScanner {

    private static final Logger logger = LoggerFactory.getLogger(GenericComponentScanner.class);

    private final Reflections reflections;
    private final String packagePrefix;

    public GenericComponentScanner(String packagePrefix) {
        this.reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packagePrefix))
                .setScanners(new TypeAnnotationsScanner(), new SubTypesScanner(), new MethodAnnotationsScanner())
                .useParallelExecutor());
        this.packagePrefix = packagePrefix;
    }

    @Override
    public Set<Class<?>> scan() {
        logger.debug("Scanning in defined package \"{}\" for annotated classes", packagePrefix);
        return reflections.getTypesAnnotatedWith(Component.class);
    }
}
