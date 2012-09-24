package com.saabgroup.yatta.evaluator.functions;

import java.math.BigDecimal;
import java.util.ArrayList;

public class PlusFunction implements IFunction {
    public Object apply(ArrayList args) {
        BigDecimal[] decimals = (BigDecimal[])args.toArray(new BigDecimal[0]);

        BigDecimal sum = new BigDecimal(0);

        for (BigDecimal decimal : decimals) {
            sum = sum.add(decimal);
        }

        return sum;
    }
}
