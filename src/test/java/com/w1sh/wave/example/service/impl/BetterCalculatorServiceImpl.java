package com.w1sh.wave.example.service.impl;

import com.w1sh.wave.condition.ConditionalOnMissingComponent;
import com.w1sh.wave.core.annotation.Component;
import com.w1sh.wave.example.service.CalculatorService;

@Component(name = "betterCalculatorService")
@ConditionalOnMissingComponent({CalculatorServiceImpl.class})
public class BetterCalculatorServiceImpl implements CalculatorService {

}
