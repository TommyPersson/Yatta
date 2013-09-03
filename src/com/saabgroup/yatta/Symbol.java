package com.saabgroup.yatta;

import java.util.HashMap;

public class Symbol {
    private static HashMap<String, Symbol> internedSymbols = new HashMap<String, Symbol>();

    private final String name;

    private Symbol(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("A symbol must have a name");
        }

        this.name = name;
    }

    public static Symbol create(String namespace, String name) {
        return create(namespace + "/" + name);
    }

    public static Symbol create(String name) {
        if (!internedSymbols.containsKey(name)) {
            Symbol sym = new Symbol(name);
            internedSymbols.put(name, sym);
        }

        return internedSymbols.get(name);
    }

    public String getName() {
        return name;
    }

    public Boolean hasNamespace() {
        return name.contains("/");
    }

    public String getLocalName() {
        if (!hasNamespace()) {
            return name;
        }

        return name.split("/")[1];
    }

    public String getNamespace() {
        if (!hasNamespace()) {
            return "";
        }

        return name.split("/")[0];
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Symbol symbol = (Symbol) o;

        return name.equals(symbol.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
