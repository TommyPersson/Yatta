package com.saabgroup.yatta.evaluator.functions;

import com.saabgroup.yatta.Symbol;
import com.saabgroup.yatta.evaluator.IEnvironment;
import com.saabgroup.yatta.evaluator.IEvaluator;

import java.util.ArrayList;
import java.util.List;

public class UserFunction implements IFunction {
    private final ArrayList paramList;
    private final Object body;
    private final IEvaluator evaluator;
    private final IEnvironment lexicalEnv;

    public UserFunction(List paramList, Object body, IEvaluator evaluator, IEnvironment lexicalEnv) {
        this.paramList = new ArrayList(paramList);
        this.body = body;
        this.evaluator = evaluator;
        this.lexicalEnv = lexicalEnv;
    }

    public Object apply(List args) throws Exception {
        IEnvironment lambdaEnv = lexicalEnv;

        if (args.size() != paramList.size()) {
            throw new Exception("Arg count mismatch");
        }

        for (int i = 0; i < args.size(); i++) {
            String paramName = ((Symbol)paramList.get(i)).getName();
            lambdaEnv = lambdaEnv.createChildEnvironment(paramName, args.get(i));
        }

        return evaluator.evaluate(body, lambdaEnv);
    }
}
