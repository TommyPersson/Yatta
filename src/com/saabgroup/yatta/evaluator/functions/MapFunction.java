package com.saabgroup.yatta.evaluator.functions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MapFunction implements IFunction {
    public Object apply(List args) throws Exception {
        args = new ArrayList(args);
        IFunction func = (IFunction)args.get(0);

        args.remove(0);

        ArrayList<List> argLists = new ArrayList<List>();
        int minListSize = Integer.MAX_VALUE;

        for (Object arg : args) {
            List list = (List)arg;
            minListSize = list.size() < minListSize ? list.size() : minListSize;
            argLists.add(list);
        }

        ArrayList results = new ArrayList();
        for (int i = 0; i < minListSize; i++) {
            ArrayList toApply = new ArrayList();
            for (List argList : argLists) {
                toApply.add(argList.get(i));
            }

            results.add(func.apply(toApply));
        }

        return Collections.unmodifiableList(results);
    }

}
