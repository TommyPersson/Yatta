package com.saabgroup.yatta.tests;

import com.saabgroup.yatta.IExternalAccessorFunction;
import com.saabgroup.yatta.Keyword;
import com.saabgroup.yatta.Symbol;
import com.saabgroup.yatta.evaluator.Environment;
import com.saabgroup.yatta.evaluator.Evaluator;
import com.saabgroup.yatta.evaluator.IEvaluator;
import com.saabgroup.yatta.evaluator.functions.IFunction;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.*;

public class EvaluatorTests {
    @Test
    public void shallEvaluateNumbers() throws Exception {
        IEvaluator evaluator = new Evaluator();

        Object res = evaluator.evaluate("1");

        assertEquals(new BigDecimal("1"), res);
    }

    @Test
    public void shallReturnLastEvaluatedForm() throws Exception {
        IEvaluator evaluator = new Evaluator();

        Object res = evaluator.evaluate("1 2");

        assertEquals(new BigDecimal("2"), res);
    }

    @Test
    public void shallLookUpSymbolValues() throws Exception {
        IEvaluator evaluator = new Evaluator();

        HashMap<String, Object> initialEnv = new HashMap<String, Object>();
        initialEnv.put("a", new BigDecimal("3"));

        Environment baseEnv = new Environment(initialEnv);

        Object res = evaluator.evaluate("a", baseEnv);

        assertEquals(new BigDecimal("3"), res);
    }

    @Test
    public void canAddNumbers() throws Exception {
        IEvaluator evaluator = new Evaluator();

        BigDecimal res = (BigDecimal)evaluator.evaluate("(+ 1 2 3 -3)");

        assertEquals(3, res.intValue());
    }

    @Test
    public void canCompareNumbers() throws Exception {
        IEvaluator evaluator = new Evaluator();

        assertTrue((Boolean)evaluator.evaluate("(< 1 2 3)"));
        assertFalse((Boolean) evaluator.evaluate("(< 1 3 2)"));
    }

    @Test
    public void canCheckEquality() throws Exception {
        IEvaluator evaluator = new Evaluator();

        assertTrue((Boolean)evaluator.evaluate("(= 1 1 (+ 2 0 -1))"));
        assertFalse((Boolean) evaluator.evaluate("(= 1 2 1)"));
    }

    @Test
    public void canDefineValues() throws Exception {
        IEvaluator evaluator = new Evaluator();

        assertEquals(2, ((BigDecimal) evaluator.evaluate("(def a 2)" +
                "a")).intValue());
        assertEquals(3, ((BigDecimal) evaluator.evaluate("(+ a 1)")).intValue());
    }

    @Test
    public void canDefineFunctions() throws Exception {
        IEvaluator evaluator = new Evaluator();

        assertTrue(evaluator.evaluate("(def my+ (lambda (x y) (+ x y)))") instanceof IFunction);
        assertEquals(3, ((BigDecimal) evaluator.evaluate("(my+ 2 1)")).intValue());
    }

    @Test
    public void canAndValues() throws Exception {
        IEvaluator evaluator = new Evaluator();

        assertTrue((Boolean) evaluator.evaluate("(and true true (= 1 1))"));
        assertFalse((Boolean) evaluator.evaluate("(and true false (= 1 1))"));
    }

    @Test
    public void canOrValues() throws Exception {
        IEvaluator evaluator = new Evaluator();

        assertTrue((Boolean)evaluator.evaluate("(or true true (= 1 1))"));
        assertTrue((Boolean)evaluator.evaluate("(or true false (= 1 1))"));
        assertFalse((Boolean) evaluator.evaluate("(or false (= 1 2) (= true false))"));
    }

    @Test
    public void canInvertValues() throws Exception {
        IEvaluator evaluator = new Evaluator();

        assertFalse((Boolean)evaluator.evaluate("(not true)"));
        assertFalse((Boolean) evaluator.evaluate("(not 2)"));
        assertTrue((Boolean)evaluator.evaluate("(not false)"));
        assertTrue((Boolean) evaluator.evaluate("(not nil)"));
    }

    @Test
    public void canDoIfStatements() throws Exception {
        IEvaluator evaluator = new Evaluator();

        assertTrue((Boolean)evaluator.evaluate("(if true true false)"));
        assertFalse((Boolean) evaluator.evaluate("(if false true false)"));
    }

    @Test
    public void canMapValuesOverAList() throws Exception {
        IEvaluator evaluator = new Evaluator();

        List res = (List)evaluator.evaluate("(map (lambda (x) (+ x 1)) " +
                                            "     '(1 2 3))");

        assertEquals(3, res.size());
        assertEquals(2, ((BigDecimal)res.get(0)).intValue());
        assertEquals(3, ((BigDecimal) res.get(1)).intValue());
        assertEquals(4, ((BigDecimal) res.get(2)).intValue());
    }

    @Test
    public void canMapValuesOverLists() throws Exception {
        IEvaluator evaluator = new Evaluator();

        List res = (List)evaluator.evaluate("(map (lambda (x y) (+ x y)) " +
                                            "     '(1 2 3)" +
                                            "     '(3 2 1))");

        assertEquals(3, res.size());
        assertEquals(4, ((BigDecimal)res.get(0)).intValue());
        assertEquals(4, ((BigDecimal) res.get(1)).intValue());
        assertEquals(4, ((BigDecimal) res.get(2)).intValue());
    }

    @Test
    public void canMapValuesOverListsOfDifferentSizes() throws Exception {
        IEvaluator evaluator = new Evaluator();

        List res = (List)evaluator.evaluate("(map (lambda (x y z) (+ x y z)) " +
                                            "     '(1 2 3)" +
                                            "     '(1 1 1 1)" +
                                            "     '(3 2 1))");

        assertEquals(3, res.size());
        assertEquals(5, ((BigDecimal)res.get(0)).intValue());
        assertEquals(5, ((BigDecimal)res.get(1)).intValue());
        assertEquals(5, ((BigDecimal)res.get(2)).intValue());
    }

    @Test
    public void canConstructLists() throws Exception {
        IEvaluator evaluator = new Evaluator();

        List res = (List)evaluator.evaluate("(list 1 2 3)");

        assertEquals(3, res.size());
        assertEquals(1, ((BigDecimal)res.get(0)).intValue());
        assertEquals(2, ((BigDecimal)res.get(1)).intValue());
        assertEquals(3, ((BigDecimal)res.get(2)).intValue());
    }

    @Test
    public void canBindLocalValuesLists() throws Exception {
        IEvaluator evaluator = new Evaluator();

        List res = (List)evaluator.evaluate("(let (x 1" +
                                            "      y 2)" +
                                            "  (list x y))");

        assertEquals(2, res.size());
        assertEquals(1, ((BigDecimal)res.get(0)).intValue());
        assertEquals(2, ((BigDecimal)res.get(1)).intValue());
    }

    @Test
    public void doExpressionsReturnTheLastValue() throws Exception {
        IEvaluator evaluator = new Evaluator();

        BigDecimal res = (BigDecimal)evaluator.evaluate("(do 1 2 3)");

        assertEquals(3, res.intValue());
    }

    @Test
    public void shallEvaluateCond() throws Exception {
        IEvaluator evaluator = new Evaluator();

        BigDecimal res = (BigDecimal)evaluator.evaluate("(cond false 1 false 2 true 3 'else 4)");

        assertEquals(3, res.intValue());
    }

    @Test
    public void shallEvaluateCondElse() throws Exception {
        IEvaluator evaluator = new Evaluator();

        BigDecimal res = (BigDecimal)evaluator.evaluate("(cond false 1 false 2 false 3 'else 4)");

        assertEquals(4, res.intValue());
    }

    @Test
    public void shallEvaluateMapLiterals() throws Exception {
        IEvaluator evaluator = new Evaluator();

        Map<Object, Object> res = (Map<Object, Object>)evaluator.evaluate("{'a 1 'b (+ 1 1)}");

        assertEquals(new BigDecimal(1), res.get(Symbol.create("a")));
        assertEquals(new BigDecimal(2), res.get(Symbol.create("b")));
    }

    @Test
    public void shallEvaluateKeywordsToThemselves() throws Exception {
        IEvaluator evaluator = new Evaluator();

        Keyword res = (Keyword)evaluator.evaluate(":keyword");

        assertEquals(Keyword.create("keyword"), res);
        assertSame(Keyword.create("keyword"), res);
    }

    @Test
    public void shallEvaluateKeywordsAsFunctionsOnMaps() throws Exception {
        IEvaluator evaluator = new Evaluator();

        String res = (String)evaluator.evaluate("(:key2 {:key1 \"val1\" :key2 \"val2\"})");

        assertEquals("val2", res);
    }

    @Test
    public void shallEvaluateExternalAccessors() throws Exception {
        IEvaluator evaluator = new Evaluator();

        evaluator.setExternalAccessor(new IExternalAccessorFunction() {
            public Object lookup(String path) {
                if (path.equals("my-accessor-path")) {
                    return "looked-up-value";
                }

                return null;
            }
        });

        String res = (String)evaluator.evaluate("<my-accessor-path>");

        assertEquals("looked-up-value", res);
    }

    @Test
    public void shallEvaluateGet() throws Exception {
        IEvaluator evaluator = new Evaluator();

        String res = (String)evaluator.evaluate("(get {\"key1\" \"val1\" \"key2\" \"val2\"} \"key2\")");

        assertEquals("val2", res);
    }
}
