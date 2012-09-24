package com.saabgroup.yatta.evaluator.special_forms;

import com.saabgroup.yatta.evaluator.IEnvironment;

import java.util.ArrayList;

public interface ISpecialForm {
    public Object apply(ArrayList args, IEnvironment env) throws Exception;
}
