package com.saabgroup.yatta.evaluator;

public interface IValue {
    Object getValue(IEvaluator evaluator, IEnvironment env);
}
