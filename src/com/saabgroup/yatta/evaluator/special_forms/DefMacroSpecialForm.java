package com.saabgroup.yatta.evaluator.special_forms;

import com.saabgroup.yatta.Symbol;
import com.saabgroup.yatta.evaluator.Evaluator;
import com.saabgroup.yatta.evaluator.IEnvironment;
import com.saabgroup.yatta.evaluator.Macro;

import java.util.List;

public class DefMacroSpecialForm implements ISpecialForm {
    private final Evaluator evaluator;

    public DefMacroSpecialForm(Evaluator evaluator) {
        this.evaluator = evaluator;
    }

    public Object apply(List args, IEnvironment env) throws Exception {
        String name = ((Symbol)args.get(0)).getName();
        List paramList = (List)args.get(1);
        List bodyForms = args.subList(2, args.size());

        Macro macro = new Macro(paramList, bodyForms, evaluator, env);

        evaluator.setRootBinding(name, macro);

        return macro;
    }
}
