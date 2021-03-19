package com.w1sh.wave.example.service.impl;

import com.w1sh.wave.core.annotation.Inject;
import com.w1sh.wave.example.service.CalculatorService;
import com.w1sh.wave.example.service.MerchantService;

public class MerchantServiceImpl implements MerchantService {

    private CalculatorService calculatorService;

    @Inject
    public MerchantServiceImpl(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    public CalculatorService getCalculatorService() {
        return calculatorService;
    }
}
