package com.w1sh.wave.example.service.impl;

import com.w1sh.wave.core.annotation.Component;
import com.w1sh.wave.core.annotation.Inject;
import com.w1sh.wave.core.annotation.Priority;
import com.w1sh.wave.example.service.CalculatorService;
import com.w1sh.wave.example.service.MerchantService;

import javax.annotation.PostConstruct;

@Component
@Priority(1)
public class MerchantServiceImpl implements MerchantService {

    private final CalculatorService calculatorService;

    @Inject
    public MerchantServiceImpl(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    public CalculatorService getCalculatorService() {
        return calculatorService;
    }

    @PostConstruct
    public void init() {}
}
