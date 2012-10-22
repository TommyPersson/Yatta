package com.saabgroup.yatta;

import com.saabgroup.yatta.evaluator.functions.IFunction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Keyword implements IFunction {
    private static HashMap<String, Keyword> internedKeywords = new HashMap<String, Keyword>();

    private final String name;

    private Keyword(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("A symbol must have a name");
        }

        this.name = name;
    }

    public static Keyword create(String name) {
        if (!internedKeywords.containsKey(name)) {
            Keyword sym = new Keyword(name);
            internedKeywords.put(name, sym);
        }

        return internedKeywords.get(name);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    public Object apply(List args) throws Exception {
        Map<Object, Object> map = (Map<Object, Object>) args.get(0);

        return map.get(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Keyword keyword = (Keyword) o;

        return name.equals(keyword.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
