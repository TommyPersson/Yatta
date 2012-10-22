package com.saabgroup.yatta;

import java.util.HashMap;

public class ExternalAccessor {
    private static HashMap<String, ExternalAccessor> internedAccessors = new HashMap<String, ExternalAccessor>();

    private final String path;

    private ExternalAccessor(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("A symbol must have a path");
        }

        this.path = name;
    }

    public static ExternalAccessor create(String path) {
        if (!internedAccessors.containsKey(path)) {
            ExternalAccessor acc = new ExternalAccessor(path);
            internedAccessors.put(path, acc);
        }

        return internedAccessors.get(path);
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return getPath();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExternalAccessor symbol = (ExternalAccessor) o;

        return path.equals(symbol.path);
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }
}
