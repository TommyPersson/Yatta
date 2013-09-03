package com.saabgroup.yatta.evaluator;

public class NsValue implements IValue {
    public Object getValue(IEvaluator evaluator, IEnvironment env) {
        return evaluator.getCurrentNamespace();
    }
}
