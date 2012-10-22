package com.saabgroup.yatta.tokenizer;

public enum TokenType {
    LParen, // (
    RParen, // )
    LBrace, // {
    RBrace, // }
    Symbol, // a-symbol
    String, // "a-string"
    Number, // 12 -12 12.3
    Quote // '
}
