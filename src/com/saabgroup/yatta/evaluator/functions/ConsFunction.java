package com.saabgroup.yatta.evaluator.functions;

import java.util.ArrayList;
import java.util.List;

public class ConsFunction implements IFunction {
    public Object apply(List args) throws Exception {
        List res = new ArrayList();
        Object obj = args.get(0);
        List l = (List)args.get(1);

        res.add(obj);
        for (Object o : l) {
            res.add(o);
        }

        return res;
    }
}
