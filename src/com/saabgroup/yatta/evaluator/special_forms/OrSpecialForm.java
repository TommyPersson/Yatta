package com.saabgroup.yatta.evaluator.special_forms;

import com.saabgroup.yatta.evaluator.IEnvironment;
import com.saabgroup.yatta.evaluator.IEvaluator;
import com.saabgroup.yatta.evaluator.ValueUtils;

import java.util.List;

public class OrSpecialForm implements ISpecialForm {
    private final IEvaluator evaluator;

    public OrSpecialForm(IEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    public Object apply(List args, IEnvironment env) throws Exception {
        for (Object arg : args) {
            Object evaluatedArg = evaluator.evaluate(arg, env);
            if (ValueUtils.isTruthy(evaluatedArg)) {
                return evaluatedArg;
            }
        }

        return false;
    }

    public boolean isSpecialForm() {
        return true;
    }
}
