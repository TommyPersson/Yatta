package com.saabgroup.yatta.evaluator.functions;

import java.math.BigDecimal;
import java.util.ArrayList;

public class MultiplyFunction implements IFunction {

    public Object apply(ArrayList args) throws Exception {
        BigDecimal sum = new BigDecimal(1);

        for (Object arg : args) {
            sum = sum.multiply((BigDecimal)arg);
        }

        return sum;
    }
}
