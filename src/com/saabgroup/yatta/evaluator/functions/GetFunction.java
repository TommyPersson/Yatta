package com.saabgroup.yatta.evaluator.functions;

import java.util.List;
import java.util.Map;

public class GetFunction implements IFunction {
    public Object apply(List args) throws Exception {
        Map<Object, Object> map = (Map<Object, Object>) args.get(0);
        Object keyVal = args.get(1);

        return map.get(keyVal);
    }
}
