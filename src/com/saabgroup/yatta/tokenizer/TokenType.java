package com.saabgroup.yatta.tokenizer;

public enum TokenType {
    LParen, // (
    RParen, // )
    LBrace, // {
    RBrace, // }
    Symbol, // a-symbol
    Keyword, // :a-keyword
    String, // "a-string"
    Number, // 12 -12 12.3
    ExternalAccessor, Quote // '
}
