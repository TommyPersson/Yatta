package com.saabgroup.yatta.reader;

import com.saabgroup.yatta.Quoted;
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

        operators.put("||", makeOp(6, false));
        operators.put("&&", makeOp(7, false));

        operators.put("==", makeOp(8, false));
        operators.put("=", makeOp(8, false));
        operators.put("<", makeOp(8, false));
        operators.put("<=", makeOp(8, false));
        operators.put(">", makeOp(8, false));
        operators.put(">=", makeOp(8, false));

        operators.put("+", makeOp(9, true));
        operators.put("-", makeOp(9, true));

        operators.put("*", makeOp(10, true));
        operators.put("/", makeOp(10, true));

        lispyOperators = new HashMap<String, String>();
        lispyOperators.put("==", "=");
        lispyOperators.put("&&", "and");
        lispyOperators.put("||", "or");
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
            output.add(readInfixExpression(buffer));
        }

        return Collections.unmodifiableList(output);
    }

    private Object readInfixExpression(TokenBuffer buffer) throws Exception {
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
                        rpnOutput.add(Symbol.create(stack.pop().getValue()));
                    }

                    stack.pop(); // LParen popped
                    break;

                case Symbol:
                    if (isIf(token)) {
                        rpnOutput.add(readIfExpression(buffer));
                        continue; // skip the moveNext below, resume reading infix expression directly where we are
                    } else if (isOperator(token)) {
                        while (operatorOnStackHasPrecedence(stack, token)) {
                            rpnOutput.add(Symbol.create(stack.pop().getValue()));
                        }

                        stack.push(token);

                    } else {
                        rpnOutput.add(Symbol.create(token.getValue()));
                    }
                    break;
            }

            buffer.moveNext();
        }

        while (!stack.empty()) {
            rpnOutput.add(Symbol.create(stack.pop().getValue()));
        }

        return lispify(rpnOutput);
    }

    private boolean operatorOnStackHasPrecedence(Stack<Token> stack, Token token) {
        return !stack.empty() && isOperator(stack.peek()) &&
                ((isLeftAssociative(token) && getPrecedence(token) <= getPrecedence(stack.peek())) ||
                 getPrecedence(token) < getPrecedence(stack.peek()));
    }

    private boolean isInvalidInfixExpressionToken(Token t) {
        return t.getValue().equals("else") ||
               t.getValue().equals("then") ||
               t.getValue().equals("end") ||
               t.getValue().equals("elsif");
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
            return Symbol.create(lispyOperators.get(name));
        }

        return Symbol.create(name);
    }

    private Object readIfExpression(TokenBuffer buffer) throws Exception {
        buffer.moveNext("if");
        Object predicate = readInfixExpression(buffer);

        buffer.moveNext("then");
        Object thenExpr = readInfixExpression(buffer);

        List<Object> condList = new ArrayList<Object>();
        condList.add(Symbol.create("cond"));
        condList.add(predicate);
        condList.add(thenExpr);

        while (buffer.current().getValue().equals("elsif")) {
            buffer.moveNext();

            Object elsifPred = readInfixExpression(buffer);
            buffer.moveNext("then");

            Object elsifThenExpr = readInfixExpression(buffer);

            condList.add(elsifPred);
            condList.add(elsifThenExpr);
        }

        if (buffer.current().getValue().equals("else")) {
            buffer.moveNext();

            condList.add(new Quoted(Symbol.create("else")));
            condList.add(readInfixExpression(buffer));
        }

        buffer.moveNext("end");


        return condList;
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
}
