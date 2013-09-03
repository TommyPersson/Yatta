package com.saabgroup.yatta.evaluator.special_forms;

import com.saabgroup.yatta.Namespace;
import com.saabgroup.yatta.evaluator.IEnvironment;
import com.saabgroup.yatta.evaluator.IEvaluator;

import java.util.List;

public class NsSpecialForm implements ISpecialForm {
    private final IEvaluator evaluator;

    public NsSpecialForm(IEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    public Object apply(List args, IEnvironment env) throws Exception {
        String name = args.get(0).toString();
        evaluator.setCurrentNamespace(new Namespace(name));

        return null;
    }
}
