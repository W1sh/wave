package com.w1sh.wave.example;

import com.w1sh.wave.annotation.Inject;
import com.w1sh.wave.example.service.MerchantService;

public class ConstructorInjectionExample {

    private final MerchantService merchantService;

    @Inject
    public ConstructorInjectionExample(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    public MerchantService getMerchantService() {
        return merchantService;
    }
}
