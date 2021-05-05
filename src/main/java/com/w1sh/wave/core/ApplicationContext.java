package com.w1sh.wave.core;

import com.w1sh.wave.condition.FilteringConditionalProcessor;
import com.w1sh.wave.condition.SimpleFilteringConditionalProcessor;
import com.w1sh.wave.core.annotation.Configuration;
import com.w1sh.wave.core.annotation.Provides;

import java.util.List;
import java.util.stream.Collectors;

public class ApplicationContext extends AbstractApplicationContext {

    public ApplicationContext(ComponentRegistry registry, ComponentScanner scanner) {
        super(registry, scanner);
    }

    public ApplicationContext(ComponentRegistry registry, ComponentScanner scanner, AbstractApplicationEnvironment environment) {
        super(registry, scanner, environment);
    }

    public static ApplicationContextBuilder builder(){
        return new ApplicationContextBuilder();
    }

    @Override
    public <T> T getComponent(Class<T> clazz) {
        return this.getRegistry().getComponent(clazz);
    }

    @Override
    public <T> T getComponent(Class<T> clazz, String name) {
        return this.getRegistry().getComponent(name, clazz);
    }

    @Override
    public boolean containsComponent(Class<?> clazz) {
        return this.getRegistry().getComponent(clazz) != null;
    }

    @Override
    public boolean containsComponent(String name) {
        return this.getRegistry().getComponent(name) != null;
    }

    @Override
    public Class<?> getType(String name) {
        final Object component = this.getRegistry().getComponent(name);
        if (component != null) {
            return component.getClass();
        }
        return null;
    }

    @Override
    public boolean isTypeMatch(String name, Class<?> clazz) {
        final Class<?> type = getType(name);
        return type != null && type.isAssignableFrom(clazz);
    }

    @Override
    public void initialize() {
        prepareContext();

        final List<Definition> definitions = this.getScanner().scan();
        // separate conditional definitions
        final List<Definition> conditionalDefinitions = definitions.stream()
                .filter(Definition::isConditional)
                .collect(Collectors.toList());

        definitions.removeAll(conditionalDefinitions);

        // register non conditional definitions
        this.getRegistry().initialize(definitions);

        // create current context and pass it onto conditional processor
        ContextMetadata context = new ContextMetadata(this, conditionalDefinitions, getEnvironment());
        FilteringConditionalProcessor conditionalProcessor = new SimpleFilteringConditionalProcessor(null);

        // filter conditional definitions based on components already initialized
        final List<Definition> passedConditionalDefinitions = conditionalProcessor.processConditionals(context);

        // register conditional definitions
        this.getRegistry().initialize(passedConditionalDefinitions);
    }

    @Override
    public void refresh() {
        this.getRegistry().clear();
        initialize();
    }

    @Override
    public void clear() {
        this.getRegistry().clear();
    }

    private void prepareContext() {
        this.getRegistry().register(ApplicationContext.class, this);

        this.getScanner().ignoreType(Configuration.class);
        this.getScanner().ignoreType(Provides.class);
    }
}
