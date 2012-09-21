package com.saabgroup.yatta.evaluator.functions;

import com.saabgroup.yatta.evaluator.IEnvironment;
import com.saabgroup.yatta.evaluator.ValueUtils;

import java.util.ArrayList;

public class NotFunction implements IFunction {
    public Object apply(ArrayList args, IEnvironment env) throws Exception {
        return !ValueUtils.isTruthy(args.get(0));
    }

    public boolean isSpecialForm() {
        return false;
    }
}
