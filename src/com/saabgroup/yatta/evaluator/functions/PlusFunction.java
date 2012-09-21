package com.saabgroup.yatta.evaluator.functions;

import com.saabgroup.yatta.evaluator.IEnvironment;

import java.math.BigDecimal;
import java.util.ArrayList;

public class PlusFunction implements IFunction {
    public Object apply(ArrayList args, IEnvironment env) {
        BigDecimal[] decimals = (BigDecimal[])args.toArray(new BigDecimal[0]);

        BigDecimal sum = new BigDecimal(0);

        for (BigDecimal decimal : decimals) {
            sum = sum.add(decimal);
        }

        return sum;
    }

    public boolean isSpecialForm() {
        return false;
    }
}
