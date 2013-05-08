package com.saabgroup.yatta.evaluator.functions;

import com.saabgroup.yatta.evaluator.IEvaluator;
import com.saabgroup.yatta.evaluator.MacroExpander;

import java.util.List;

public class MacroExpand1Function implements IFunction {
    private final IEvaluator evaluator;
    private final MacroExpander macroExpander;

    public MacroExpand1Function(IEvaluator evaluator, MacroExpander macroExpander) {
        this.evaluator = evaluator;
        this.macroExpander = macroExpander;
    }

    public Object apply(List args) throws Exception {
        if (args.size() != 1) {
            throw new Exception("macro-expand-1: Must have only 1 argument!");
        }

        Object o = macroExpander.expand1(args.get(0), evaluator.getEnvironment());
        return o;
    }
}
