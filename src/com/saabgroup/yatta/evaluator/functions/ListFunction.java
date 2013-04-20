package com.saabgroup.yatta.evaluator.functions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListFunction implements IFunction {
    public Object apply(List args) throws Exception {
        return Collections.unmodifiableList(new ArrayList(args));
    }

}
