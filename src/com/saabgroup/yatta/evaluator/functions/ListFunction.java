package com.saabgroup.yatta.evaluator.functions;

import java.util.ArrayList;

public class ListFunction implements IFunction {
    public Object apply(ArrayList args) throws Exception {
        return new ArrayList(args);
    }

}
