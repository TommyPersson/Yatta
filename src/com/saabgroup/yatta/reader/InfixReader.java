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
    private static HashMap<String, String> lispyOperators;

    static {
        operators = new HashMap<String, Tuple<Integer, Boolean>>();

        operators.put("==", makeOp(1, false));
        operators.put("=", makeOp(1, false));
        operators.put("<", makeOp(1, false));
        operators.put("<=", makeOp(1, false));
        operators.put(">", makeOp(1, false));
        operators.put(">=", makeOp(1, false));

        operators.put("+", makeOp(2, true));
        operators.put("-", makeOp(2, true));

        operators.put("*", makeOp(3, true));
        operators.put("/", makeOp(3, true));

        lispyOperators = new HashMap<String, String>();
        lispyOperators.put("==", "=");
    }

    private static Tuple<Integer, Boolean> makeOp(Integer precedence, Boolean isLeftAssociative) {
        return new Tuple<Integer, Boolean>(precedence, isLeftAssociative);
    }

    private ITokenizer tokenizer;

    public InfixReader() {
        tokenizer = new Tokenizer();
    }

    public InfixReader(ITokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    public Collection<Object> read(String input) throws Exception {
        Collection<Token> tokens = tokenizer.tokenize(input);

        return read(tokens);
    }

    public Collection<Object> read(Collection<Token> tokens) throws Exception {
        ArrayList<Object> output = new ArrayList<Object>();

        TokenBuffer buffer = new TokenBuffer(tokens);

        while (!buffer.isEmpty()) {
            output.add(readExpression(buffer));
        }

        return Collections.unmodifiableList(output);
    }

    private Object readExpression(TokenBuffer buffer) throws Exception {
        Token token = buffer.current();

        switch (token.getType()) {
            case LParen:
            case Number:
            case String:
                return readInfixExpression(buffer);

            case Symbol:
                if (isIf(token)) {
                    return readIfExpression(buffer);
                } else {
                    return readInfixExpression(buffer);
                }
        }

        throw new Exception("Wat");
    }

    private Object readInfixExpression(TokenBuffer buffer) {
        Stack<Token> stack = new Stack<Token>();
        Collection<Object> rpnOutput = new ArrayList<Object>();

        while (!buffer.isEmpty() && !isInvalidInfixExpressionToken(buffer.current())) {
            Token token = buffer.current();

            switch (token.getType()) {
                case Number:
                    rpnOutput.add(new BigDecimal(token.getValue()));
                    break;
                case String:
                    rpnOutput.add(token.getValue());
                    break;

                case LParen:
                    stack.push(token);
                    break;

                case RParen:
                    while (stack.peek().getType() != TokenType.LParen) {
                        rpnOutput.add(new Symbol(stack.pop().getValue()));
                    }

                    stack.pop(); // LParen popped
                    break;

                case Symbol:
                    if (isIf(token)) {
                        rpnOutput.add(readIfExpression(buffer));
                        continue; // skip the moveNext below, resume reading infix expression directly where we are
                    } else if (isOperator(token)) {
                        while (!stack.empty() && isOperator(stack.peek()) &&
                                ((isLeftAssociative(token) && getPrecedence(token) <= getPrecedence(stack.peek())) ||
                                 getPrecedence(token) < getPrecedence(stack.peek()))) {
                            rpnOutput.add(new Symbol(stack.pop().getValue()));
                        }

                        stack.push(token);

                    } else {
                        rpnOutput.add(new Symbol(token.getValue()));
                    }
                    break;

                case EOF:
                    break;
            }

            buffer.moveNext();
        }

        while (!stack.empty()) {
            rpnOutput.add(new Symbol(stack.pop().getValue()));
        }

        return lispify(rpnOutput);
    }

    private boolean isInvalidInfixExpressionToken(Token t) {
        return t.getValue().equals("else") ||
               t.getValue().equals("then") ||
               t.getValue().equals("end");
    }

    private Object lispify(Collection<Object> rpnOutput) {
        Stack<Object> stack = new Stack<Object>();

        for (Object obj : rpnOutput) {
            stack.push(obj);
        }

        return lispify(stack);
    }


    private Object lispify(Stack<Object> stack) {
        Object obj = stack.pop();

        if (obj instanceof Symbol && isOperator(((Symbol)obj).getName())) {
            List<Object> list = new ArrayList<Object>();

            list.add(getLispySymbolForOperator(((Symbol) obj).getName()));
            list.add(lispify(stack));
            list.add(lispify(stack));

            return list;
        }

        return obj;
    }

    private Object getLispySymbolForOperator(String name) {
        if (lispyOperators.containsKey(name)) {
            return new Symbol(lispyOperators.get(name));
        }

        return new Symbol(name);
    }

    private Object readIfExpression(TokenBuffer buffer) {
        buffer.moveNext(); // discard if-symbol
        Object predicate = readInfixExpression(buffer);

        buffer.moveNext(); // discard then-symbol
        Object thenExpr = readInfixExpression(buffer);

        buffer.moveNext(); // discard else-symbol
        Object elseExpr = readInfixExpression(buffer);

        buffer.moveNext(); // discard end-symbol

        List<Object> ifList = new ArrayList<Object>();
        ifList.add(new Symbol("if"));
        ifList.add(predicate);
        ifList.add(thenExpr);
        ifList.add(elseExpr);

        return ifList;
    }

    private boolean isIf(Token t) {
        return t.getValue().equals("if");
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

    private class TokenBuffer {
        private int location;
        private ArrayList<Token> buffer;

        public TokenBuffer(Collection<Token> tokens) {
            buffer = new ArrayList<Token>(tokens);
        }

        Token current() {
            return buffer.get(location);
        }

        void moveNext() {
            location++;
        }

        boolean isEmpty() {
            return location >= buffer.size() || current().getType() == TokenType.EOF;
        }
    }
}
