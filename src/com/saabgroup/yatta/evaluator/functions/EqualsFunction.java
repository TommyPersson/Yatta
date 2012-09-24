package com.saabgroup.yatta.evaluator.functions;

import java.util.ArrayList;

public class EqualsFunction implements IFunction {
    public Object apply(ArrayList args) {
        args = new ArrayList(args);
        Object previousArg = args.remove(0);

        for (Object arg : args) {
            if (!previousArg.equals(arg)) {
                return false;
            }
        }

        return true;
    }
}
