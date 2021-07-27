package com.w1sh.wave.core;

import java.lang.reflect.Executable;

public interface InjectionPointFactory {

    InjectionPoint create(Executable executable);
}
