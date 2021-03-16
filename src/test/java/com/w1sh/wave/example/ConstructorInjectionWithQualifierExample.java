package com.w1sh.wave.example;

import com.w1sh.wave.annotation.Inject;
import com.w1sh.wave.annotation.Qualifier;
import com.w1sh.wave.example.service.CalculatorService;
import com.w1sh.wave.example.service.MerchantService;

public class ConstructorInjectionWithQualifierExample {

    private final MerchantService merchantService;
    private final CalculatorService calculatorService;

    @Inject
    public ConstructorInjectionWithQualifierExample(MerchantService merchantService,
                                                    @Qualifier(name = "calculatorService") CalculatorService calculatorService) {
        this.merchantService = merchantService;
        this.calculatorService = calculatorService;
    }

    public MerchantService getMerchantService() {
        return merchantService;
    }

    public CalculatorService getCalculatorService() {
        return calculatorService;
    }
}
