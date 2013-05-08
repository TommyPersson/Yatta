package com.saabgroup.yatta.evaluator.special_forms;

import com.saabgroup.yatta.evaluator.IEnvironment;
import com.saabgroup.yatta.evaluator.IEvaluator;

import java.util.List;

public class DoSpecialForm implements ISpecialForm {
    private final IEvaluator evaluator;

    public DoSpecialForm(IEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    public Object apply(List args, IEnvironment env) throws Exception {
        Object last = null;

        for (Object form : args) {
            last = evaluator.evaluate(form, env);
        }

        return last;
    }
}
