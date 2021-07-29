package com.w1sh.wave.example.service.impl;

import com.w1sh.wave.condition.ConditionalOnProperty;
import com.w1sh.wave.condition.PropertyType;
import com.w1sh.wave.core.annotation.Configuration;
import com.w1sh.wave.core.annotation.Provides;
import com.w1sh.wave.example.service.MerchantService;

@Configuration
@ConditionalOnProperty(key = "test", value = "true", type = PropertyType.SYSTEM)
public class TestConfiguration {

    @Provides
    public MerchantService configurationDefinedMerchantService(){
        return new TestMerchantService();
    }

    private static class TestMerchantService implements MerchantService { }
}
