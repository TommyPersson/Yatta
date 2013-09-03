package com.saabgroup.yatta.tests;

import com.saabgroup.yatta.evaluator.Evaluator;
import com.saabgroup.yatta.evaluator.IEvaluator;
import com.saabgroup.yatta.Namespace;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static junit.framework.Assert.assertEquals;

public class EvaluatorNamespacesTests {
    @Rule
    public ExpectedException ex = ExpectedException.none();

    @Test
    public void shallHaveUserAsInitialNamespace() throws Exception {
        IEvaluator evaluator = new Evaluator();

        Namespace ns = (Namespace)evaluator.evaluate("yatta.core/*ns*");

        assertEquals("yatta.user", ns.getName());
    }

    @Test
    public void shallBeAbleToAccessSymbolsInCoreNamespaceImplicitly() throws Exception {
        IEvaluator evaluator = new Evaluator();

        Namespace ns = (Namespace)evaluator.evaluate("*ns*");

        assertEquals("yatta.user", ns.getName());
    }

    @Test
    public void shallBeAbleToSwitchNamespace() throws Exception {
        IEvaluator evaluator = new Evaluator();

        Namespace ns = (Namespace)evaluator.evaluate("(ns my-new-ns)" +
                                                     "*ns*");

        assertEquals("my-new-ns", ns.getName());
    }

    @Test
    public void shallBeUnableToAccessSymbolsInOtherNamespacesImplicitly() throws Exception {
        IEvaluator evaluator = new Evaluator();

        ex.expect(RuntimeException.class);
        ex.expectMessage("Could not find binding for symbol 'a'");

        Namespace ns = (Namespace)evaluator.evaluate("(def a 1)" +
                                                     "(ns t)" +
                                                     "a");
    }
}
