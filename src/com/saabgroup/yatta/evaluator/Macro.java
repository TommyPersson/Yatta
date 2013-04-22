package com.saabgroup.yatta.evaluator;

import com.saabgroup.yatta.Symbol;

import java.util.ArrayList;
import java.util.List;

public class Macro {
    private final List paramList;
    private final Object body;
    private final IEvaluator evaluator;
    private final IEnvironment lexicalEnvironment;

    public Macro(List paramList, Object body, IEvaluator evaluator, IEnvironment lexicalEnvironment) {
        this.paramList = new ArrayList(paramList);
        this.body = body;
        this.evaluator = evaluator;
        this.lexicalEnvironment = lexicalEnvironment;
    }

    public Object expand(List args) throws Exception {
        IEnvironment macroEnv = lexicalEnvironment;

        if (args.size() != paramList.size()) {
      //      throw new Exception("Arg count mismatch");
        }

        for (int i = 0; i < args.size(); i++) {
            String paramName = ((Symbol)paramList.get(i)).getName();

            if (paramName == "&") {
                paramName = ((Symbol)paramList.get(i+1)).getName();

                ArrayList rest = new ArrayList();

                for (int j = i; j < args.size(); j++) {
                    rest.add(args.get(j));
                }

                macroEnv = macroEnv.createChildEnvironment(paramName, rest);
                break;
            }

            macroEnv = macroEnv.createChildEnvironment(paramName, args.get(i));
        }

        return evaluator.evaluate(body, macroEnv);
    }
}
