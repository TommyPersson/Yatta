package com.saabgroup.yatta.parser;

import com.saabgroup.yatta.QuoteType;
import com.saabgroup.yatta.Symbol;
import com.saabgroup.yatta.Tuple;
import com.saabgroup.yatta.tokenizer.ITokenizer;
import com.saabgroup.yatta.tokenizer.Token;
import com.saabgroup.yatta.tokenizer.Tokenizer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

public class Parser implements IParser {
    ITokenizer tokenizer;
    private Stack<Tuple<List<Object>, Stack<QuoteType>>> lists;

    public Parser() {
        tokenizer = new Tokenizer();
    }

    public Parser(ITokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    public Collection<Object> parse(String input) throws Exception {
        Collection<Token> tokens = tokenizer.tokenize(input);

        return parse(tokens);
    }

    public Collection<Object> parse(Collection<Token> tokens) throws Exception {
        lists = new Stack<Tuple<List<Object>, Stack<QuoteType>>>();
        Tuple<List<Object>, Stack<QuoteType>> result = new Tuple<List<Object>, Stack<QuoteType>>(new ArrayList<Object>(), new Stack<QuoteType>());
        lists.push(result);

        for (Token token : tokens)
        {
            switch (token.getType())
            {
                case LParen:
                    lists.push(new Tuple<List<Object>, Stack<QuoteType>>(new ArrayList<Object>(), new Stack<QuoteType>()));
                    break;

                case RParen:
                    if (lists.size() == 1) {
                        throw new Exception("Extra parenthesis detected at " + token.getRow() + ":" + token.getCol());
                    }

                    Tuple<List<Object>, Stack<QuoteType>> popped = lists.pop();
                    addNode(popped.getItem1());
                    break;

                case Quote:
                    lists.peek().getItem2().push(QuoteType.Normal);
                    break;

                case EOF:
                    if (lists.size() > 1){
                        throw new Exception("Unexpected EOF at " + token.getRow() + ":" + token.getCol() + ". Unbalanced parenthesis.");
                    }

                    break;

                default:
                    addNode(parse(token));
                    break;
            }
        }

        return result.getItem1();
    }

    private Object parse(Token token) {
        switch (token.getType()) {
            case Number:
                return new BigDecimal(token.getValue());
            case String:
                return token.getValue();
            case Symbol:
                return new Symbol(token.getValue());
        }

        return null;
    }

    private void addNode(Object node) {
        Object value = node;

        while (lists.peek().getItem2().size() > 0) {
            value = lists.peek().getItem2().pop().createQuoted(value);
        }

        lists.peek().getItem1().add(value);
    }

}
