package com.saabgroup.yatta.reader;

import com.saabgroup.yatta.tokenizer.Token;
import com.saabgroup.yatta.tokenizer.TokenType;

import java.util.ArrayList;
import java.util.Collection;

public class TokenBuffer {
    private int location;
    private ArrayList<Token> buffer;

    public TokenBuffer(Collection<Token> tokens) {
        buffer = new ArrayList<Token>(tokens);
    }

    Token current() {
        return buffer.get(location);
    }

    void moveNext(TokenType type) throws Exception {
        if (!current().getType().equals(type)) {
            throw new Exception("Unexpected token type: " + current().getType());
        }

        location++;
    }

    void moveNext(String tokenValue) throws Exception {
        if (!current().getValue().equals(tokenValue)) {
            throw new Exception("Unexpected token: " + current().getValue());
        }

        location++;
    }

    void moveNext() {
        location++;
    }

    boolean isEmpty() {
        return location >= buffer.size();
    }
}
