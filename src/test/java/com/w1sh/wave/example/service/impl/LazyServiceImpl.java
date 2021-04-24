package com.w1sh.wave.example.service.impl;

import com.w1sh.wave.core.Lazy;
import com.w1sh.wave.core.Provider;
import com.w1sh.wave.core.annotation.Component;
import com.w1sh.wave.core.annotation.Inject;
import com.w1sh.wave.example.service.CalculatorService;
import com.w1sh.wave.example.service.MerchantService;

@Component
public class LazyServiceImpl implements MerchantService {

    private final Lazy<CalculatorService> calculatorService;
    private final Provider<BetterCalculatorServiceImpl> betterCalculatorService;

    @Inject
    public LazyServiceImpl(Lazy<CalculatorService> calculatorService, Provider<BetterCalculatorServiceImpl> betterCalculatorService) {
        this.calculatorService = calculatorService;
        this.betterCalculatorService = betterCalculatorService;
    }

    public Lazy<CalculatorService> getCalculatorService() {
        return calculatorService;
    }

    public Provider<BetterCalculatorServiceImpl> getBetterCalculatorService() {
        return betterCalculatorService;
    }
}
