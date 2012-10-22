package com.saabgroup.yatta;

import java.util.*;

public class MapLiteral {
    private HashMap<Object, Object> map = new HashMap<Object, Object>();

    private MapLiteral() {
    }

    public static MapLiteral create(List<Object> initializer) {
        MapLiteral lit = new MapLiteral();

        for (int i = 0; i < initializer.size(); i = i+2) {
            Object key = initializer.get(i);
            Object val = initializer.get(i+1);

            lit.map.put(key, val);
        }

        return lit;
    }

    public Set<Map.Entry<Object, Object>> getEntries() {
        return Collections.unmodifiableSet(map.entrySet());
    }
}
