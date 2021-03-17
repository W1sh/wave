package com.w1sh.wave.example.service.impl;

import com.w1sh.wave.annotation.Component;
import com.w1sh.wave.example.service.CalculatorService;

@Component(name = "calculatorService", lazy = true)
public class DuplicateCalculatorServiceImpl implements CalculatorService {
}
