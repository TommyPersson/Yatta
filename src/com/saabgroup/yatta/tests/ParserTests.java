package com.saabgroup.yatta.tests;

import com.saabgroup.yatta.Quoted;
import com.saabgroup.yatta.Symbol;
import com.saabgroup.yatta.parser.IParser;
import com.saabgroup.yatta.parser.Parser;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

public class ParserTests {
    @Test
    public void shallParseNumbers() throws Exception {
        IParser parser = new Parser();

        BigDecimal[] res = parser.parse("1 23 23.5 -1 -23 -23.5").toArray(new BigDecimal[0]);

        assertEquals(new BigDecimal("1"), res[0]);
        assertEquals(new BigDecimal("23"), res[1]);
        assertEquals(new BigDecimal("23.5"), res[2]);
        assertEquals(new BigDecimal("-1"), res[3]);
        assertEquals(new BigDecimal("-23"), res[4]);
        assertEquals(new BigDecimal("-23.5"), res[5]);
    }

    @Test
    public void shallParseStrings() throws Exception {
        IParser parser = new Parser();

        String[] res = parser.parse("\"a-string\" \"\\\"a-nested-string\\\"\"").toArray(new String[0]);

        assertEquals("a-string", res[0]);
        assertEquals("\"a-nested-string\"", res[1]);
    }

    @Test
    public void shallParseSymbols() throws Exception {
        IParser parser = new Parser();

        Symbol[] res = parser.parse("a-sym a-n0ther-sym! < + / *").toArray(new Symbol[0]);

        assertEquals("a-sym", res[0].getName());
        assertEquals("a-n0ther-sym!", res[1].getName());
        assertEquals("<", res[2].getName());
        assertEquals("+", res[3].getName());
        assertEquals("/", res[4].getName());
        assertEquals("*", res[5].getName());
    }

    @Test
    public void shallParseLists() throws Exception {
        IParser parser = new Parser();

        ArrayList[] res = parser.parse("(1 2 3) (a b c)").toArray(new ArrayList[0]);

        assertEquals(3, res[0].size());
        assertEquals(new BigDecimal("1"), res[0].get(0));
        assertEquals(new BigDecimal("2"), res[0].get(1));
        assertEquals(new BigDecimal("3"), res[0].get(2));
        assertEquals(3, res[1].size());
        assertEquals("a", ((Symbol)res[1].get(0)).getName());
        assertEquals("b", ((Symbol)res[1].get(1)).getName());
        assertEquals("c", ((Symbol)res[1].get(2)).getName());
    }

    @Test
    public void shallParseQuotedValues() throws Exception {
        IParser parser = new Parser();

        Quoted[] res = parser.parse("'1 'a '(1 2 3)").toArray(new Quoted[0]);

        assertEquals(3, res.length);
        assertEquals(new BigDecimal("1"), res[0].getQuotedValue());
        assertEquals("a", ((Symbol)res[1].getQuotedValue()).getName());
        assertEquals(3, ((ArrayList<BigDecimal>)res[2].getQuotedValue()).size());
    }
}
