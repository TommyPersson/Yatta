package com.saabgroup.yatta.evaluator.functions;

import com.saabgroup.yatta.evaluator.IEnvironment;
import com.saabgroup.yatta.evaluator.IEvaluator;

import java.util.ArrayList;

public class LambdaSpecialForm implements IFunction {
    private final IEvaluator evaluator;

    public LambdaSpecialForm(IEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    public Object apply(ArrayList args, IEnvironment env) throws Exception {
        ArrayList paramList = (ArrayList)args.get(0);
        Object body = args.get(1);

        return new UserFunction(paramList, body, evaluator, env);
    }

    public boolean isSpecialForm() {
        return true;
    }
}
