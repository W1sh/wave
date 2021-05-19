package com.w1sh.wave.core;

import java.util.List;

public interface ComponentScanner extends Configurable {

    List<Definition> scan();

    void ignoreType(Class<?> clazz);
}
