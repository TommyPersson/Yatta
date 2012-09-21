package com.saabgroup.yatta.evaluator.functions;

import com.saabgroup.yatta.evaluator.Evaluator;
import com.saabgroup.yatta.evaluator.IEnvironment;

import java.util.ArrayList;

public class DoSpecialForm implements IFunction {
    private final Evaluator evaluator;

    public DoSpecialForm(Evaluator evaluator) {
        this.evaluator = evaluator;
    }

    public Object apply(ArrayList args, IEnvironment env) throws Exception {
        return evaluator.evaluate(args, env);
    }

    public boolean isSpecialForm() {
        return true;
    }
}
