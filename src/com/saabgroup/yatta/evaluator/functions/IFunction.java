package com.saabgroup.yatta.evaluator.functions;

import java.util.List;

public interface IFunction {
    Object apply(List args) throws Exception;
}
