package com.saabgroup.yatta.evaluator.functions;

import com.saabgroup.yatta.evaluator.IEnvironment;
import com.saabgroup.yatta.evaluator.IEvaluator;
import com.saabgroup.yatta.evaluator.ValueUtils;

import java.util.ArrayList;

public class AndSpecialForm implements IFunction {
    private final IEvaluator evaluator;

    public AndSpecialForm(IEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    public Object apply(ArrayList args, IEnvironment env) throws Exception {
        for (Object arg : args) {
            Object evaluatedArg = evaluator.evaluate(arg, env);
            if (!ValueUtils.isTruthy(evaluatedArg)) {
                return false;
            }
        }

        return true;
    }

    public boolean isSpecialForm() {
        return true;
    }
}
