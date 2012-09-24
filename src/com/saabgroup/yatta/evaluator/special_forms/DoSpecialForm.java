package com.saabgroup.yatta.evaluator.special_forms;

import com.saabgroup.yatta.evaluator.Evaluator;
import com.saabgroup.yatta.evaluator.IEnvironment;

import java.util.ArrayList;

public class DoSpecialForm implements ISpecialForm {
    private final Evaluator evaluator;

    public DoSpecialForm(Evaluator evaluator) {
        this.evaluator = evaluator;
    }

    public Object apply(ArrayList args, IEnvironment env) throws Exception {
        return evaluator.evaluate(args, env);
    }
}
