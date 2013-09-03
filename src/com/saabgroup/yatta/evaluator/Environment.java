package com.saabgroup.yatta.evaluator;

import com.saabgroup.yatta.Namespace;
import com.saabgroup.yatta.Symbol;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Environment implements IEnvironment {
    private final HashMap<String, Object> envTable;
    private final Environment parent;

    public Environment() {
        this(new HashMap<String, Object>(), null);
    }

    public Environment(Map<String, Object> initialEnv) {
        this(initialEnv, null);
    }

    public Environment(Map<String, Object> initialEnv, Environment parent) {
        this.envTable = new HashMap<String, Object>(initialEnv);
        this.parent = parent;
    }

    public Environment createChildEnvironment(IEnvironment env) {
        HashMap<String, Object> extended = new HashMap<String, Object>(envTable);

        for (Map.Entry<String, Object> entry : env.getAllBindings()) {
            extended.put(entry.getKey(), entry.getValue());
        }

        return new Environment(extended, this);
    }

    public IEnvironment createChildEnvironment(String symbolName, Object value) {
        HashMap<String, Object> extended = new HashMap<String, Object>(envTable);
        extended.put(symbolName, value);

        return new Environment(extended, this);
    }

    public Collection<Map.Entry<String, Object>> getAllBindings() {
        return envTable.entrySet();
    }

    public boolean hasDefinedValue(Symbol symbol, Namespace namespace) {
        if (hasDefinedValue(symbol)) {
            return true;
        }

        if (symbol.hasNamespace()) {
            String nsName = symbol.getNamespace();
            Namespace ns = namespace.getNamespaceByAlias(nsName);

            return hasDefinedValue(Symbol.create(ns.getName(), symbol.getLocalName()));
        }

        Symbol namespacedSymbol = Symbol.create(namespace.getName(), symbol.getName());
        if (hasDefinedValue(namespacedSymbol)) {
            return true;
        }

        for (Namespace ns : namespace.getImplicitReferences()) {
            Symbol coreSymbol = Symbol.create(ns.getName(), symbol.getName());
            if (hasDefinedValue(coreSymbol)) {
                return true;
            }
        }

        return false;
    }

    public Object lookUp(Symbol symbol, Namespace namespace) {
        if (symbol.hasNamespace()) {
            if (hasDefinedValue(symbol)) {
                return lookUp(symbol);
            } else {
                String nsName = symbol.getNamespace();
                Namespace ns = namespace.getNamespaceByAlias(nsName);

                return lookUp(Symbol.create(ns.getName(), symbol.getLocalName()));
            }
        }

        Symbol namespacedSymbol = Symbol.create(namespace.getName(), symbol.getName());
        if (hasDefinedValue(namespacedSymbol)) {
            return lookUp(namespacedSymbol);
        }

        for (Namespace ns : namespace.getImplicitReferences()) {
            Symbol coreSymbol = Symbol.create(ns.getName(), symbol.getName());
            if (hasDefinedValue(coreSymbol)) {
                return lookUp(coreSymbol);
            }
        }

        return lookUp(symbol);
    }

    public void put(String name, Object value) {
        envTable.put(name, value);
    }

    private Object lookUp(Symbol symbol) {
        String symbolName = symbol.getName();
        if (envTable.containsKey(symbolName)) {
            return envTable.get(symbolName);
        }

        if (parent != null) {
            return parent.lookUp(symbol);
        }

        throw new RuntimeException(String.format("Could not find binding for symbol '%s'", symbolName));
    }

    private boolean hasDefinedValue(Symbol symbol) {
        String symbolName = symbol.getName();
        if (envTable.containsKey(symbolName)) {
            return true;
        }

        if (parent != null) {
            return parent.hasDefinedValue(symbol);
        }

        return false;
    }
}
