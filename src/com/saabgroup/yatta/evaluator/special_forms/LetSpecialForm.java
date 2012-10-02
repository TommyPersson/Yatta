package com.saabgroup.yatta.evaluator.special_forms;

import com.saabgroup.yatta.Symbol;
import com.saabgroup.yatta.evaluator.Evaluator;
import com.saabgroup.yatta.evaluator.IEnvironment;

import java.util.ArrayList;
import java.util.List;

public class LetSpecialForm implements ISpecialForm {
    private final Evaluator evaluator;

    public LetSpecialForm(Evaluator evaluator) {
        this.evaluator = evaluator;
    }

    public Object apply(List args, IEnvironment env) throws Exception {
        // (let (sym1 val1
        //       sym2 val2)
        //   body)

        List bindingList = (List)args.get(0);
        ArrayList body = new ArrayList(args);
        body.remove(0);

        for (int i = 0; i < bindingList.size(); i = i + 2) {
            String symbolName = ((Symbol)bindingList.get(i)).getName();
            Object symbolValue = evaluator.evaluate(bindingList.get(i+1), env);

            env = env.createChildEnvironment(symbolName, symbolValue);
        }

        return evaluator.evaluate(body, env);
    }
}
