package com.saabgroup.yatta.evaluator.functions;

import com.saabgroup.yatta.evaluator.IEnvironment;

import java.util.ArrayList;

public class MapFunction implements IFunction {
    public Object apply(ArrayList args, IEnvironment env) throws Exception {
        args = new ArrayList(args);
        IFunction func = (IFunction)args.get(0);

        args.remove(0);

        ArrayList<ArrayList> argLists = new ArrayList<ArrayList>();
        int minListSize = Integer.MAX_VALUE;

        for (Object arg : args) {
            ArrayList list = (ArrayList)arg;
            minListSize = list.size() < minListSize ? list.size() : minListSize;
            argLists.add(list);
        }

        ArrayList results = new ArrayList();
        for (int i = 0; i < minListSize; i++) {
            ArrayList toApply = new ArrayList();
            for (ArrayList argList : argLists) {
                toApply.add(argList.get(i));
            }

            results.add(func.apply(toApply, env));
        }

        return results;
    }

    public boolean isSpecialForm() {
        return false;
    }
}
