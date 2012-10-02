package com.saabgroup.yatta.evaluator.functions;

import java.util.ArrayList;
import java.util.List;

public class LessThanFunction implements IFunction {
    public Object apply(List args) {
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
