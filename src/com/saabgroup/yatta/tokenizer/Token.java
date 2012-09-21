package com.saabgroup.yatta.tokenizer;

public class Token {
    private final TokenType type;
    private final String value;
    private final int col;
    private final int row;

    public Token(TokenType type, String value, int col, int row) {
        this.type = type;
        this.value = value;
        this.col = col;
        this.row = row;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }
}