package com.saabgroup.yatta.tokenizer;

import java.util.List;

public interface ITokenizer {
    List<Token> tokenize(String input);
}
