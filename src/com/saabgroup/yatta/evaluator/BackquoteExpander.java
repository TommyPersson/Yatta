package com.saabgroup.yatta.evaluator;

import com.saabgroup.yatta.*;

import java.util.ArrayList;
import java.util.List;

public class BackquoteExpander {
    private final Evaluator evaluator;

    public BackquoteExpander(Evaluator evaluator) {
        this.evaluator = evaluator;
    }

    public Object expand(Backquote backquote, IEnvironment env) throws Exception {
        return innerExpand(backquote.getQuotedValue(), env);
    }

    private Object innerExpand(Object form, IEnvironment env) throws Exception {
        if (form instanceof Quoted) {
            return innerExpandQuoted((Quoted)form, env);
        } else if (form instanceof Backquote) {
            return expand((Backquote)form, env);
        } else if (form instanceof Tilde) {
            return innerExpandTilde((Tilde)form, env);
        } else if (form instanceof Splice) {
            throw new Exception("~@ may only appear in a list!");
        } else if (form instanceof List) {
            return innerExpandList((List)form, env);
        } else {
            return form;
        }
    }

    private Object innerExpandList(List list, IEnvironment env) throws Exception {
        List res = new ArrayList();

        for (Object form : list) {
            if (form instanceof Splice) {
                List toSplice = (List)evaluator.evaluate(((Splice)form).getQuotedValue(), env);

                for (Object f : toSplice) {
                    res.add(f);
                }
            }
            else {
                res.add(innerExpand(form, env));
            }
        }

        return res;
    }

    private Object innerExpandTilde(Tilde tilde, IEnvironment env) throws Exception {
        return evaluator.evaluate(tilde.getQuotedValue(), env);
    }

    private Object innerExpandQuoted(Quoted quoted, IEnvironment env) throws Exception {
        Object expanded = innerExpand(quoted.getQuotedValue(), env);

        return new Quoted(expanded);
    }
}
