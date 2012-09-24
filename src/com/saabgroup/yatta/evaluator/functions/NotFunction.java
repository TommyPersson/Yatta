package com.saabgroup.yatta.evaluator.functions;

import com.saabgroup.yatta.evaluator.ValueUtils;

import java.util.ArrayList;

public class NotFunction implements IFunction {
    public Object apply(ArrayList args) throws Exception {
        return !ValueUtils.isTruthy(args.get(0));
    }
}
