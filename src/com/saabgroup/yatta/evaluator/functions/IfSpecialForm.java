package com.saabgroup.yatta.evaluator.functions;

import com.saabgroup.yatta.evaluator.Evaluator;
import com.saabgroup.yatta.evaluator.IEnvironment;
import com.saabgroup.yatta.evaluator.ValueUtils;

import java.util.ArrayList;

public class IfSpecialForm implements IFunction {
    private final Evaluator evaluator;

    public IfSpecialForm(Evaluator evaluator) {
        this.evaluator = evaluator;
    }

    public Object apply(ArrayList args, IEnvironment env) throws Exception {
        // (if test succ fail)
        boolean success = ValueUtils.isTruthy(evaluator.evaluate(args.get(0), env));

        Object toEval = success ? args.get(1) : args.get(2);

        return evaluator.evaluate(toEval, env);
    }

    public boolean isSpecialForm() {
        return false;
    }
}
