package com.saabgroup.yatta;

import com.saabgroup.yatta.evaluator.Evaluator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class Namespace {
    private final String name;
    private final ArrayList<Namespace> implicitReferences = new ArrayList<Namespace>();
    private final HashMap<String, Namespace> aliasedNamespaces = new HashMap<String, Namespace>();

    public Namespace(String name) {
        this.name = name;

        if (!name.equals(Evaluator.YATTA_CORE_NS_NAME)) {
            addImplicitReference(new Namespace(Evaluator.YATTA_CORE_NS_NAME));
        }
    }

    public String getName() {
        return name;
    }

    public void addImplicitReference(Namespace namespace) {
        implicitReferences.add(namespace);
    }

    public Collection<Namespace> getImplicitReferences() {
        return Collections.unmodifiableCollection(implicitReferences);
    }

    public void addNamespaceAlias(String alias, Namespace namespace) {
        aliasedNamespaces.put(alias, namespace);
    }

    public Namespace getNamespaceByAlias(String alias) {
        return aliasedNamespaces.get(alias);
    }
}
