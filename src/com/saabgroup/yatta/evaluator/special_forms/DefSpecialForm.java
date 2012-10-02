package com.saabgroup.yatta.evaluator.special_forms;

import com.saabgroup.yatta.Symbol;
import com.saabgroup.yatta.evaluator.IEnvironment;
import com.saabgroup.yatta.evaluator.IEvaluator;

import java.util.List;

public class DefSpecialForm implements ISpecialForm {
    private IEvaluator evaluator;

    public DefSpecialForm(IEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    public Object apply(List args, IEnvironment env) throws Exception {
        Object value = evaluator.evaluate(args.get(1), env);

        evaluator.setRootBinding(((Symbol) args.get(0)).getName(), value);

        return value;
    }
}
