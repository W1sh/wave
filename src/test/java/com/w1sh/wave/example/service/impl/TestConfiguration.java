package com.w1sh.wave.example.service.impl;

import com.w1sh.wave.condition.ConditionalOnProperty;
import com.w1sh.wave.condition.PropertyType;
import com.w1sh.wave.core.annotation.Configuration;

@Configuration
@ConditionalOnProperty(key = "test", value = "true", type = PropertyType.SYSTEM)
public class TestConfiguration {
}
