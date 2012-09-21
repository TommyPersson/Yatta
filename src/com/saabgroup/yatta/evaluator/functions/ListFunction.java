package com.saabgroup.yatta.evaluator.functions;

import com.saabgroup.yatta.evaluator.IEnvironment;

import java.util.ArrayList;

public class ListFunction implements IFunction {
    public Object apply(ArrayList args, IEnvironment env) throws Exception {
        return new ArrayList(args);
    }

    public boolean isSpecialForm() {
        return false;
    }
}
