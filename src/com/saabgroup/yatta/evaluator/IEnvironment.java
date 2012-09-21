package com.saabgroup.yatta.evaluator;

import com.saabgroup.yatta.Symbol;

import java.util.Collection;
import java.util.Map;

public interface IEnvironment {
    Object lookUp(Symbol symbol) throws Exception;

    IEnvironment createChildEnvironment(IEnvironment env);

    IEnvironment createChildEnvironment(String symbolName, Object value);

    Collection<Map.Entry<String, Object>> getAllBindings();
}
