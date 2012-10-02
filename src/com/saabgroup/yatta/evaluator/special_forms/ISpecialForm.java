package com.saabgroup.yatta.evaluator.special_forms;

import com.saabgroup.yatta.evaluator.IEnvironment;

import java.util.List;

public interface ISpecialForm {
    public Object apply(List args, IEnvironment env) throws Exception;
}
