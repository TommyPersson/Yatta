package com.saabgroup.yatta.evaluator.special_forms;

import com.saabgroup.yatta.evaluator.IEnvironment;
import com.saabgroup.yatta.evaluator.IEvaluator;
import com.saabgroup.yatta.evaluator.ValueUtils;

import java.util.List;

public class CondSpecialForm implements ISpecialForm {
    private IEvaluator evaluator;

    public CondSpecialForm(IEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    public Object apply(List args, IEnvironment env) throws Exception {
        for (int i = 0; i < args.size(); i = i + 2) {
            Object test = evaluator.evaluate(args.get(i), env);

            if (ValueUtils.isTruthy(test)) {
                return evaluator.evaluate(args.get(i+1), env);
            }
        }

        return null;
    }
}
