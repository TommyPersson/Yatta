package com.saabgroup.yatta.evaluator;

import com.saabgroup.yatta.Symbol;
import com.saabgroup.yatta.evaluator.special_forms.DoSpecialForm;

import java.util.ArrayList;
import java.util.List;

public class Macro {
    private final List paramList;
    private final List bodyForms;
    private final IEvaluator evaluator;
    private final IEnvironment lexicalEnvironment;

    public Macro(List paramList, List bodyForms, IEvaluator evaluator, IEnvironment lexicalEnvironment) {
        this.paramList = new ArrayList(paramList);
        this.bodyForms = bodyForms;
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

            if (paramName.equals("&")) {
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

        return new DoSpecialForm(evaluator).apply(bodyForms, macroEnv);
    }
}
