package com.saabgroup.yatta.tests;

import com.saabgroup.yatta.*;
import com.saabgroup.yatta.reader.IReader;
import com.saabgroup.yatta.reader.Reader;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class ReaderTests {
    @Test
    public void shallReadNumbers() throws Exception {
        IReader Reader = new Reader();

        BigDecimal[] res = Reader.read("1 23 23.5 -1 -23 -23.5").toArray(new BigDecimal[0]);

        assertEquals(new BigDecimal("1"), res[0]);
        assertEquals(new BigDecimal("23"), res[1]);
        assertEquals(new BigDecimal("23.5"), res[2]);
        assertEquals(new BigDecimal("-1"), res[3]);
        assertEquals(new BigDecimal("-23"), res[4]);
        assertEquals(new BigDecimal("-23.5"), res[5]);
    }

    @Test
    public void shallReadStrings() throws Exception {
        IReader Reader = new Reader();

        String[] res = Reader.read("\"a-string\" \"\\\"a-nested-string\\\"\"").toArray(new String[0]);

        assertEquals("a-string", res[0]);
        assertEquals("\"a-nested-string\"", res[1]);
    }

    @Test
    public void shallReadSymbols() throws Exception {
        IReader Reader = new Reader();

        Symbol[] res = Reader.read("a-sym a-n0ther-sym! < + / *").toArray(new Symbol[0]);

        assertEquals("a-sym", res[0].getName());
        assertEquals("a-n0ther-sym!", res[1].getName());
        assertEquals("<", res[2].getName());
        assertEquals("+", res[3].getName());
        assertEquals("/", res[4].getName());
        assertEquals("*", res[5].getName());
    }

    @Test
    public void shallReadLists() throws Exception {
        IReader Reader = new Reader();

        List[] res = Reader.read("(1 2 3) (a b c)").toArray(new List[0]);

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
    public void shallReadQuotedValues() throws Exception {
        IReader Reader = new Reader();

        Quoted[] res = Reader.read("'1 'a '(1 2 3)").toArray(new Quoted[0]);

        assertEquals(3, res.length);
        assertEquals(new BigDecimal("1"), res[0].getQuotedValue());
        assertEquals("a", ((Symbol)res[1].getQuotedValue()).getName());
        assertEquals(3, ((List<BigDecimal>)res[2].getQuotedValue()).size());
    }

    @Test
    public void shallReadExternalAccessors() throws Exception {
        IReader reader = new Reader();

        ExternalAccessor[] res = reader.read("<accessor>").toArray(new ExternalAccessor[0]);

        assertEquals("accessor", res[0].getPath());
    }

    @Test
    public void shallReadBackquotedValues() throws Exception {
        IReader Reader = new Reader();

        Backquote[] res = Reader.read("`1 `a `(1 2 3)").toArray(new Backquote[0]);

        assertEquals(3, res.length);
        assertEquals(new BigDecimal("1"), res[0].getQuotedValue());
        assertEquals("a", ((Symbol)res[1].getQuotedValue()).getName());
        assertEquals(3, ((List<BigDecimal>)res[2].getQuotedValue()).size());
    }

    @Test
    public void shallReadTildedValues() throws Exception {
        IReader Reader = new Reader();

        Tilde[] res = Reader.read("~1 ~a ~(1 2 3)").toArray(new Tilde[0]);

        assertEquals(3, res.length);
        assertEquals(new BigDecimal("1"), res[0].getQuotedValue());
        assertEquals("a", ((Symbol)res[1].getQuotedValue()).getName());
        assertEquals(3, ((List<BigDecimal>)res[2].getQuotedValue()).size());
    }

    @Test
    public void shallReadSplicedValues() throws Exception {
        IReader Reader = new Reader();

        Splice[] res = Reader.read("~@1 ~@a ~@(1 2 3)").toArray(new Splice[0]);

        assertEquals(3, res.length);
        assertEquals(new BigDecimal("1"), res[0].getQuotedValue());
        assertEquals("a", ((Symbol)res[1].getQuotedValue()).getName());
        assertEquals(3, ((List<BigDecimal>)res[2].getQuotedValue()).size());
    }
}
