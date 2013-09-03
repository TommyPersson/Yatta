package com.saabgroup.yatta.evaluator;

import com.saabgroup.yatta.*;

import java.util.ArrayList;
import java.util.List;

public class MacroExpander {
    private final IEvaluator evaluator;

    private boolean hasExpandedInLastRound = false;

    public MacroExpander(IEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    public Object expand(Object form, IEnvironment env) throws Exception {
        Object expanded = form;

        do {
            hasExpandedInLastRound = false;

            expanded = expand1(expanded, env);
        } while (hasExpandedInLastRound);

        return expanded;
    }

    public Object expand1(Object form, IEnvironment env) throws Exception {
        hasExpandedInLastRound = false;

        return innerExpand(form, env);
    }

    private Object innerExpand(Object form, IEnvironment env) throws Exception {
        if (form instanceof Quoted) {
            return innerExpandQuoted((Quoted)form, env);
        } else if (form instanceof List) {
            return innerExpandList((List)form, env);
        } else {
            return form;
        }
    }

    private Object innerExpandQuoted(Quoted quoted, IEnvironment env) throws Exception {
        return quoted;
    }

    private Object innerExpandList(List list, IEnvironment env) throws Exception {
        if (list.size() == 0) {
            return list;
        }

        Object first = list.get(0);

        if (!hasExpandedInLastRound && first instanceof Symbol) {
            Symbol sym = (Symbol)first;
            if (env.hasDefinedValue(sym, evaluator.getCurrentNamespace())) {
                Object value = env.lookUp((Symbol)first, evaluator.getCurrentNamespace());
                if (value instanceof Macro) {
                    hasExpandedInLastRound = true;

                    Object expand = ((Macro)value).expand(list.subList(1, list.size()));
                    return expand;
                }
            }
        }

        List res = new ArrayList();

        for (Object form : list) {
            res.add(innerExpand(form, env));
        }

        return res;
    }
}
