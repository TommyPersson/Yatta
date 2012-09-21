package com.saabgroup.yatta.parser;

import com.saabgroup.yatta.tokenizer.Token;

import java.util.Collection;

public interface IParser {
    Collection<Object> parse(String input) throws Exception;

    Collection<Object> parse(Collection<Token> tokens) throws Exception;
}
