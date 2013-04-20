package com.saabgroup.yatta.tokenizer;

public enum TokenType {
    LParen, // (
    RParen, // )
    Quote, // '
    Symbol, // a-symbol
    String, // "a-string"
    Number, // 12 -12 12.3
    ExternalAccessor, //<something>
}
