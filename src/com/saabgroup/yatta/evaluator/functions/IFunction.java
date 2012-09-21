package com.saabgroup.yatta.evaluator.functions;

import com.saabgroup.yatta.evaluator.IEnvironment;

import java.util.ArrayList;

public interface IFunction {
    Object apply(ArrayList args, IEnvironment env) throws Exception;

    boolean isSpecialForm();
}
