package com.saabgroup.yatta;

import java.util.HashMap;

public class Keyword {
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
