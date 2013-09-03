package com.saabgroup.yatta.evaluator;

import com.saabgroup.yatta.Namespace;
import com.saabgroup.yatta.Symbol;

import java.util.Collection;
import java.util.Map;

public interface IEnvironment {
    boolean hasDefinedValue(Symbol sym, Namespace currentNamespace);

    Object lookUp(Symbol first, Namespace currentNamespace);

    IEnvironment createChildEnvironment(IEnvironment env);

    IEnvironment createChildEnvironment(String symbolName, Object value);

    Collection<Map.Entry<String, Object>> getAllBindings();
}
