package com.w1sh.wave.example.service.impl;

import com.w1sh.wave.condition.ConditionalOnComponent;
import com.w1sh.wave.core.annotation.Component;
import com.w1sh.wave.example.service.CalculatorService;

@Component(name = "calculatorService")
@ConditionalOnComponent(CalculatorServiceImpl.class)
public class DuplicateCalculatorServiceImpl implements CalculatorService {
}
