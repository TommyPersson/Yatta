package com.saabgroup.yatta.evaluator.functions;

import com.saabgroup.yatta.evaluator.IEnvironment;

import java.util.ArrayList;

public class LessThanFunction implements IFunction {
    public Object apply(ArrayList args) {
        args = new ArrayList(args);
        Comparable previousArg = (Comparable)args.remove(0);

        for (Object arg : args) {
            int res = previousArg.compareTo(arg);
            if (res != -1) {
                return false;
            }

            previousArg = (Comparable)arg;
        }

        return true;
    }

}
