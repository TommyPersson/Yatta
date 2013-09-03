package com.saabgroup.yatta.evaluator;

import com.saabgroup.yatta.IExternalAccessorFunction;
import com.saabgroup.yatta.Namespace;

public interface IEvaluator {
    Object evaluate(String input) throws Exception;

    Object evaluate(String input, IEnvironment env) throws Exception;

    Object evaluate(Object object, IEnvironment env) throws Exception;

    void setRootBinding(String name, Object value);

    IEnvironment getEnvironment();

    Object getCurrentNamespace();

    void setCurrentNamespace(Namespace namespace);

    void setExternalAccessor(IExternalAccessorFunction accessorFunction);
}
