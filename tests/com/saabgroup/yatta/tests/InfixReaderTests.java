package com.saabgroup.yatta.tests;

import com.saabgroup.yatta.evaluator.Evaluator;
import com.saabgroup.yatta.evaluator.IEvaluator;
import com.saabgroup.yatta.reader.IReader;
import com.saabgroup.yatta.reader.InfixReader;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class InfixReaderTests {

    @Test
    public void shallRPNize() throws Exception {
        IReader parser = new InfixReader();

        Collection<Object> result = parser.read("(1 + 2) * 3");

        assertEquals(1, result.size());

        IEvaluator evaluator = new Evaluator(parser);

        assertEquals(9, ((BigDecimal)evaluator.evaluate("(1 + 2) * 3")).intValue());
    }

    @Test
    public void shallReadIfs() throws Exception {
        IReader parser = new InfixReader();

        String program = "if (1 == 2) then (1 + 2) * 3 else 1 + 2 * 3 end";
        List<Object> result = (List<Object>)parser.read(program);

        assertEquals(1, result.size());

        IEvaluator evaluator = new Evaluator(parser);

        assertEquals(7, ((BigDecimal)evaluator.evaluate(program)).intValue());
    }

    @Test
    public void shallReadIfs2() throws Exception {
        IReader parser = new InfixReader();

        String program = "if (1 == 1) then (1 + 2) * 3 else 1 + 2 * 3 end";
        List<Object> result = (List<Object>)parser.read(program);

        assertEquals(1, result.size());

        IEvaluator evaluator = new Evaluator(parser);

        assertEquals(9, ((BigDecimal)evaluator.evaluate(program)).intValue());
    }

    @Test
    public void shallReadNestedIfs() throws Exception {
        IReader parser = new InfixReader();

        String program = "if (1 == 1) " +
                         "then " +
                         "  (1 + 2) * if (1 == 1) then 3 else 2 end " +
                         "else " +
                         "  1 + 2 * 3 " +
                         "end";
        List<Object> result = (List<Object>)parser.read(program);

        assertEquals(1, result.size());

        IEvaluator evaluator = new Evaluator(parser);

        assertEquals(9, ((BigDecimal)evaluator.evaluate(program)).intValue());
    }

    @Test
    public void shallReadNestedIfs2() throws Exception {
        IReader parser = new InfixReader();

        String program = "if (1 == 1) " +
                         "then " +
                         "  (1 + 2) * (if (1 == 1) then 3 else 2 end) " +
                         "else " +
                         "  1 + 2 * 3 " +
                         "end";
        List<Object> result = (List<Object>)parser.read(program);

        assertEquals(1, result.size());

        IEvaluator evaluator = new Evaluator(parser);

        assertEquals(9, ((BigDecimal)evaluator.evaluate(program)).intValue());
    }

    @Test
    public void shallReadNestedIfs3() throws Exception {
        IReader parser = new InfixReader();

        String program = "if (1 == 2) " +
                         "then " +
                         "  (1 + 2) * (if (1 == 1) then 3 else 2 end) " +
                         "else " +
                         "  if (1 == 1) then 3 else 2 end " +
                         "end";
        List<Object> result = (List<Object>)parser.read(program);

        assertEquals(1, result.size());

        IEvaluator evaluator = new Evaluator(parser);

        assertEquals(3, ((BigDecimal)evaluator.evaluate(program)).intValue());
    }

    @Test
    public void shallReadNestedIfs4() throws Exception {
        IReader parser = new InfixReader();

        String program = "if (1 == 2) " +
                         "then " +
                         "  (1 + 2) * (if (1 == 1) then 3 else 2 end) " +
                         "else " +
                         "  (if (1 == 1) then 3 else 2 end) " +
                         "end";
        List<Object> result = (List<Object>)parser.read(program);

        assertEquals(1, result.size());

        IEvaluator evaluator = new Evaluator(parser);

        assertEquals(3, ((BigDecimal)evaluator.evaluate(program)).intValue());
    }

    @Test
    public void shallReadNestedIfs5() throws Exception {
        IReader parser = new InfixReader();

        String program = "if Tommy == \"1\" then Val1 else Val2 end";

        IEvaluator evaluator = new Evaluator(parser);
        evaluator.setRootBinding("Tommy", "1");
        evaluator.setRootBinding("Val1", new BigDecimal(123));

        assertEquals(123, ((BigDecimal) evaluator.evaluate(program)).intValue());
    }

    @Test
    public void shallReadIfsWithElsifs() throws Exception {
        IReader parser = new InfixReader();

        String program = "if Tommy == \"1\" then Val1 " +
                         "elsif Tommy == \"2\" then Val2 " +
                         "elsif Tommy == \"3\" then Val3 " +
                         "end";

        IEvaluator evaluator = new Evaluator(parser);
        evaluator.setRootBinding("Tommy", "3");
        evaluator.setRootBinding("Val3", new BigDecimal(123));

        assertEquals(123, ((BigDecimal) evaluator.evaluate(program)).intValue());
    }
}
