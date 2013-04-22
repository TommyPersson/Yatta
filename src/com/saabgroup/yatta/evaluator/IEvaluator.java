package com.saabgroup.yatta.evaluator;

import com.saabgroup.yatta.IExternalAccessorFunction;

import java.util.Collection;

public interface IEvaluator {
    Object evaluate(String input) throws Exception;

    Object evaluate(String input, IEnvironment env) throws Exception;

    Object evaluate(Object object, IEnvironment env) throws Exception;

    Object evaluate(Collection<Object> object, IEnvironment env) throws Exception;

    void setRootBinding(String name, Object value);

    IEnvironment getEnvironment();

    void setExternalAccessor(IExternalAccessorFunction accessorFunction);
}
