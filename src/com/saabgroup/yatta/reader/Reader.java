package com.saabgroup.yatta.reader;

import com.saabgroup.yatta.Quoted;
import com.saabgroup.yatta.Symbol;
import com.saabgroup.yatta.tokenizer.ITokenizer;
import com.saabgroup.yatta.tokenizer.Token;
import com.saabgroup.yatta.tokenizer.TokenType;
import com.saabgroup.yatta.tokenizer.Tokenizer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

public class Reader implements IReader {
    ITokenizer tokenizer;

    public Reader() {
        tokenizer = new Tokenizer();
    }

    public Reader(ITokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    public Collection<Object> read(String input) throws Exception {
        Collection<Token> tokens = tokenizer.tokenize(input);

        return read(tokens);
    }

    public Collection<Object> read(Collection<Token> tokens) throws Exception {
        TokenBuffer buffer = new TokenBuffer(tokens);

        Collection<Object> expressions = new ArrayList<Object>();

        while (!buffer.isEmpty()) {
            expressions.add(readExpression(buffer));
        }

        return expressions;
    }

    private Object readExpression(TokenBuffer buffer) throws Exception {
        Token token = buffer.current();

        switch (token.getType()) {
            case LParen:
                return readList(buffer);
            case Quote:
                return readQuoted(buffer);
            case Number:
                return readNumber(buffer);
            case String:
                return readString(buffer);
            case Symbol:
                return readSymbol(buffer);
        }

        throw new Exception("Unknown token");
    }

    private Object readList(TokenBuffer buffer) throws Exception {
        buffer.moveNext(); // discard LPAREN

        Collection<Object> list = new ArrayList<Object>();

        while (buffer.current().getType() != TokenType.RParen) {
            list.add(readExpression(buffer));
        }

        buffer.moveNext(); // discard RPAREN

        return list;
    }

    private Object readSymbol(TokenBuffer buffer) {
        Token token = buffer.current();
        buffer.moveNext();

        return new Symbol(token.getValue());
    }

    private Object readQuoted(TokenBuffer buffer) throws Exception {
        buffer.moveNext(); // discard QUOTE

        return new Quoted(readExpression(buffer));
    }

    private Object readString(TokenBuffer buffer) {
        Token token = buffer.current();
        buffer.moveNext();

        return token.getValue();
    }

    private Object readNumber(TokenBuffer buffer) {
        Token token = buffer.current();
        buffer.moveNext();

        return new BigDecimal(token.getValue());
    }

    private class TokenBuffer {
        private int location;
        private ArrayList<Token> buffer;

        public TokenBuffer(Collection<Token> tokens) {
            buffer = new ArrayList<Token>(tokens);
        }

        Token current() {
            return buffer.get(location);
        }

        void moveNext() {
            location++;
        }

        boolean isEmpty() {
            return location >= buffer.size() || current().getType() == TokenType.EOF;
        }
    }
}
