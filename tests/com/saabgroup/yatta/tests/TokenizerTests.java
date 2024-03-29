package com.saabgroup.yatta.tests;

import com.saabgroup.yatta.tokenizer.ITokenizer;
import com.saabgroup.yatta.tokenizer.Token;
import com.saabgroup.yatta.tokenizer.TokenType;
import com.saabgroup.yatta.tokenizer.Tokenizer;
import junit.framework.TestSuite;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

public class TokenizerTests extends TestSuite {

    @Test
    public void shallBeAbleToReadEmptyInput() {
        ITokenizer tokenizer = new Tokenizer();

        List<Token> result = tokenizer.tokenize("");

        assertEquals(0, result.size());
    }

    @Test
    public void shallBeAbleToReadWhitespaceInput() {
        ITokenizer tokenizer = new Tokenizer();

        List<Token> result = tokenizer.tokenize("    ");

        assertEquals(0, result.size());
    }

    @Test
    public void shallBeAbleToTokenizeLParen() {
        ITokenizer tokenizer = new Tokenizer();

        List<Token> result = tokenizer.tokenize("(");

        assertEquals(1, result.size());
        assertEquals(TokenType.LParen, result.get(0).getType());
    }

    @Test
    public void shallBeAbleToTokenizeRParen() {
        ITokenizer tokenizer = new Tokenizer();

        List<Token> result = tokenizer.tokenize(")");

        assertEquals(1, result.size());
        assertEquals(TokenType.RParen, result.get(0).getType());
    }

    @Test
    public void shallBeAbleToTokenizeSymbol() {
        ITokenizer tokenizer = new Tokenizer();

        List<Token> result = tokenizer.tokenize("a-symbol");

        assertEquals(1, result.size());
        assertEquals(TokenType.Symbol, result.get(0).getType());
    }

    @Test
    public void shallBeAbleToTokenizeString() {
        ITokenizer tokenizer = new Tokenizer();

        List<Token> result = tokenizer.tokenize("\"a-string\"");

        assertEquals(1, result.size());
        assertEquals(TokenType.String, result.get(0).getType());
    }

    @Test
    public void shallBeAbleToTokenizeNumbers() {
        ITokenizer tokenizer = new Tokenizer();

        List<Token> result = tokenizer.tokenize("1 23 23.5 -1 -23 -23.5");

        assertEquals(6, result.size());
        assertEquals(TokenType.Number, result.get(0).getType());
        assertEquals(TokenType.Number, result.get(1).getType());
        assertEquals(TokenType.Number, result.get(2).getType());
        assertEquals(TokenType.Number, result.get(3).getType());
        assertEquals(TokenType.Number, result.get(4).getType());
        assertEquals(TokenType.Number, result.get(5).getType());
        assertEquals("1", result.get(0).getValue());
        assertEquals("23", result.get(1).getValue());
        assertEquals("23.5", result.get(2).getValue());
        assertEquals("-1", result.get(3).getValue());
        assertEquals("-23", result.get(4).getValue());
        assertEquals("-23.5", result.get(5).getValue());
    }

    @Test
    public void shallBeAbleToTokenizeQuote() {
        ITokenizer tokenizer = new Tokenizer();

        List<Token> result = tokenizer.tokenize("'");

        assertEquals(1, result.size());
        assertEquals(TokenType.Quote, result.get(0).getType());
    }

    @Test
    public void shallBeAbleToTokenizeExternalAccessors() {
        ITokenizer tokenizer = new Tokenizer();

        List<Token> result = tokenizer.tokenize("<an-accessor>");

        assertEquals(1, result.size());
        assertEquals(TokenType.ExternalAccessor, result.get(0).getType());
        assertEquals("an-accessor", result.get(0).getValue());
    }

    @Test
    public void shallBeAbleToTokenizeBackQuote() {
        ITokenizer tokenizer = new Tokenizer();

        List<Token> result = tokenizer.tokenize("`");

        assertEquals(1, result.size());
        assertEquals(TokenType.Backquote, result.get(0).getType());
    }

    @Test
    public void shallBeAbleToTokenizeTilde() {
        ITokenizer tokenizer = new Tokenizer();

        List<Token> result = tokenizer.tokenize("~");

        assertEquals(1, result.size());
        assertEquals(TokenType.Tilde, result.get(0).getType());
    }

    @Test
    public void shallBeAbleToTokenizeSplice() {
        ITokenizer tokenizer = new Tokenizer();

        List<Token> result = tokenizer.tokenize("~@");

        assertEquals(1, result.size());
        assertEquals(TokenType.Splice, result.get(0).getType());
    }
}
