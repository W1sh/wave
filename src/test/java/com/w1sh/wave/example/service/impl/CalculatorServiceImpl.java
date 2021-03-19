package com.w1sh.wave.example.service.impl;

import com.w1sh.wave.core.annotation.Component;
import com.w1sh.wave.core.annotation.Inject;
import com.w1sh.wave.example.service.CalculatorService;

@Component(name = "calculatorService")
public class CalculatorServiceImpl implements CalculatorService {
    @Inject
    public CalculatorServiceImpl() { }
}
