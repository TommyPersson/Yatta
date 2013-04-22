package com.saabgroup.yatta.tests;

import com.saabgroup.yatta.Symbol;
import com.saabgroup.yatta.evaluator.Evaluator;
import com.saabgroup.yatta.evaluator.IEvaluator;
import com.saabgroup.yatta.evaluator.Macro;
import com.saabgroup.yatta.reader.IReader;
import com.saabgroup.yatta.reader.Reader;
import com.saabgroup.yatta.repl.Printer;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class MacroTest {
    @Test
    public void shallEvaluateNumbers() throws Exception {
        IEvaluator evaluator = new Evaluator();
        IReader reader = new Reader();

        List params = new ArrayList();
        params.add(Symbol.create("a"));
        params.add(Symbol.create("b"));

        Object body = reader.read("(list '+ a b)").iterator().next();

        Macro m = new Macro(params, body, evaluator, evaluator.getEnvironment());

        List args = new ArrayList();
        args.add(new BigDecimal(1));
        args.add(new BigDecimal(2));

        Object expanded = m.expand(args);

        assertEquals(new BigDecimal(3), evaluator.evaluate(expanded, evaluator.getEnvironment()));
    }

    @Test
    public void shallEvaluateNumbers2() throws Exception {
        IEvaluator evaluator = new Evaluator();
        IReader reader = new Reader();

        List params = new ArrayList();
        params.add(Symbol.create("name"));
        params.add(Symbol.create("params"));
        params.add(Symbol.create("&"));
        params.add(Symbol.create("body"));

        Object body = reader.read("(list 'def name (list 'lambda params (cons 'do body)))").iterator().next();

        Macro defn = new Macro(params, body, evaluator, evaluator.getEnvironment());

        Object name = Symbol.create("my-fn");
        List paramList = new ArrayList();
        paramList.add(Symbol.create("x"));

        List defnbody = new ArrayList();
        List form1 = new ArrayList();
        form1.add(Symbol.create("+"));
        form1.add(Symbol.create("x"));
        form1.add(Symbol.create("1"));

        List form2 = new ArrayList();
        form2.add(Symbol.create("+"));
        form2.add(Symbol.create("x"));
        form2.add(Symbol.create("2"));

        defnbody.add(form1);

        List args = new ArrayList();
        args.add(name);
        args.add(paramList);
        args.add(form1);
        args.add(form2);

        Object expanded = defn.expand(args);

        String test = new Printer().print(expanded);

        System.out.print(test);
    }
}
