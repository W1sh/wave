package com.w1sh.wave.example.service.impl;

import com.w1sh.wave.annotation.Component;
import com.w1sh.wave.annotation.Inject;
import com.w1sh.wave.example.service.CalculatorService;

@Component(name = "calculatorService")
public class CalculatorServiceImpl implements CalculatorService {
    @Inject
    public CalculatorServiceImpl() { }
}
