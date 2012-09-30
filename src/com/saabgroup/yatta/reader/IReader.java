package com.saabgroup.yatta.reader;

import com.saabgroup.yatta.tokenizer.Token;

import java.util.Collection;

public interface IReader {
    // Output is an AST based on lists
    Collection<Object> read(String input) throws Exception;

    Collection<Object> read(Collection<Token> tokens) throws Exception;
}
