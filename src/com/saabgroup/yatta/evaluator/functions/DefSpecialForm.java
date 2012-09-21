package com.saabgroup.yatta.evaluator.functions;

import com.saabgroup.yatta.Symbol;
import com.saabgroup.yatta.evaluator.IEnvironment;
import com.saabgroup.yatta.evaluator.IEvaluator;

import java.util.ArrayList;

public class DefSpecialForm implements IFunction {
    private IEvaluator evaluator;

    public DefSpecialForm(IEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    public Object apply(ArrayList args, IEnvironment env) throws Exception {
        Object value = evaluator.evaluate(args.get(1), env);

        evaluator.setRootBinding(((Symbol) args.get(0)).getName(), value);

        return value;
    }

    public boolean isSpecialForm() {
        return true;
    }
}
