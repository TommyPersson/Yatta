package com.saabgroup.yatta.reader;

import com.saabgroup.yatta.Symbol;
import com.saabgroup.yatta.Tuple;
import com.saabgroup.yatta.tokenizer.ITokenizer;
import com.saabgroup.yatta.tokenizer.Token;
import com.saabgroup.yatta.tokenizer.TokenType;
import com.saabgroup.yatta.tokenizer.Tokenizer;

import java.math.BigDecimal;
import java.util.*;

public class InfixReader implements IReader {
    private static HashMap<String, Tuple<Integer, Boolean>> operators;

    static {
        operators = new HashMap<String, Tuple<Integer, Boolean>>();

        operators.put("=", makeOp(1, false));
        operators.put("<", makeOp(1, false));
        operators.put("<=", makeOp(1, false));
        operators.put(">", makeOp(1, false));
        operators.put(">=", makeOp(1, false));

        operators.put("+", makeOp(2, true));
        operators.put("-", makeOp(2, true));

        operators.put("*", makeOp(3, true));
        operators.put("/", makeOp(3, true));
    }

    private static Tuple<Integer, Boolean> makeOp(Integer precedence, Boolean isLeftAssociative) {
        return new Tuple<Integer, Boolean>(precedence, isLeftAssociative);
    }


    public Collection<Object> read(String input) throws Exception {
        ITokenizer tokenizer = new Tokenizer();

        return read(tokenizer.tokenize(input));
    }

    public Collection<Object> read(Collection<Token> tokens) throws Exception {
        Stack<Token> stack = new Stack<Token>();
        Stack<Object> output = new Stack<Object>();

        List<Token> reversedTokens = new ArrayList<Token>(tokens);
        Collections.reverse(reversedTokens);

        for (Token token : reversedTokens) {
            switch (token.getType()) {
                case Number:
                    output.push(new BigDecimal(token.getValue()));
                    break;
                case String:
                    output.push(token.getValue());
                    break;

                case LParen:
                    while (stack.peek().getType() != TokenType.RParen) {
                        output.push(new Symbol(stack.pop().getValue()));
                    }

                    stack.pop(); // RParen popped
                    break;

                case RParen:
                    stack.push(token);

                    break;

                case Symbol:
                    if (isOperator(token)) {
                        while (!stack.empty() && isOperator(stack.peek()) &&
                               ((isLeftAssociative(token) && getPrecedence(token) <= getPrecedence(stack.peek())) ||
                                getPrecedence(token) < getPrecedence(stack.peek()))) {
                            output.push(new Symbol(stack.pop().getValue()));
                        }

                        stack.push(token);

                    } else {
                        output.push(new Symbol(token.getValue()));
                    }
                    break;

                case EOF:
                    break;
            }
        }

        while (!stack.empty()) {
            output.add(new Symbol(stack.pop().getValue()));
        }

        Collection<Object> lispOutput = new ArrayList<Object>();

        while (!output.empty()) {
            lispOutput.add(lispify(output));
        }

        return lispOutput;
    }

    private Object lispify(Stack<Object> stack) {
        Object obj = stack.pop();

        if (obj instanceof Symbol && isOperator(((Symbol) obj).getName())) {
            List<Object> list = new ArrayList<Object>();
            list.add(obj);
            list.add(lispify(stack));
            list.add(lispify(stack));

            return list;
        }

        return obj;
    }

    private boolean isOperator(Token t) {
        return operators.containsKey(t.getValue());
    }

    private boolean isOperator(String s) {
        return operators.containsKey(s);
    }

    private int getPrecedence(Token t) {
        return operators.get(t.getValue()).getItem1();
    }

    private boolean isLeftAssociative(Token t) {
        return operators.get(t.getValue()).getItem2();
    }

    private class OperatorToken extends Token {
        public OperatorToken(Token t) {
            super(t.getType(), t.getValue(), t.getCol(), t.getRow());
        }

        int getPrecedence() {
            return operators.get(getValue()).getItem1();
        }

        Boolean isLeftAssociative() {
            return operators.get(getValue()).getItem2();
        }
    }
}
