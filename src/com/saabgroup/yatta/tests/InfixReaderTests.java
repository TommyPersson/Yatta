package com.saabgroup.yatta.tests;

import com.saabgroup.yatta.evaluator.Evaluator;
import com.saabgroup.yatta.evaluator.IEvaluator;
import com.saabgroup.yatta.reader.IReader;
import com.saabgroup.yatta.reader.InfixReader;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collection;

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
}
