package com.saabgroup.yatta.evaluator.special_forms;

import com.saabgroup.yatta.Namespace;
import com.saabgroup.yatta.Symbol;
import com.saabgroup.yatta.evaluator.IEnvironment;
import com.saabgroup.yatta.evaluator.IEvaluator;

import java.util.ArrayList;
import java.util.List;

public class NsSpecialForm implements ISpecialForm {
    private final IEvaluator evaluator;

    public NsSpecialForm(IEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    public Object apply(List args, IEnvironment env) throws Exception {
        String name = args.get(0).toString();
        Namespace namespace = new Namespace(name);

        if (args.size() > 1 && args.get(1).toString().equals(":aliases")) {
            ArrayList<ArrayList> aliasesList = (ArrayList<ArrayList>)args.get(2);

            for (ArrayList aliasDefList : aliasesList) {
                String fullNsName = ((Symbol)aliasDefList.get(0)).getName();
                String nsAlias = ((Symbol)aliasDefList.get(2)).getName();

                namespace.addNamespaceAlias(nsAlias, new Namespace(fullNsName));
            }
        }

        evaluator.setCurrentNamespace(namespace);

        return namespace;
    }
}
