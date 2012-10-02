package com.saabgroup.yatta.tokenizer;

import java.util.*;

public class Tokenizer implements ITokenizer {
    private final HashSet<Character> invalidSymbolCharacters = new HashSet<Character>(Arrays.asList(new Character[] { '(', ')', ' ', '\n', '\t' }));

    public List<Token> tokenize(String input) {
        InputBuffer inputBuffer = new InputBuffer(input);
        ArrayList<Token> tokens = new ArrayList<Token>();

        while (!inputBuffer.isEmpty()) {
            Token token = readNextToken(inputBuffer);
            if (token != null) {
                tokens.add(token);
            }
        }

        return Collections.unmodifiableList(tokens);
    }

    private Token readNextToken(InputBuffer inputBuffer) {
        while (!inputBuffer.isEmpty()) {
            inputBuffer.movePastWhitespace();
            if (inputBuffer.isEmpty()) {
                break;
            }

            char current = inputBuffer.current();
            Character next = inputBuffer.peekNext();

            int column = inputBuffer.currentColumn();
            int row = inputBuffer.currentRow();

            switch (current) {
                case ';':
                    inputBuffer.moveToNextLine();
                    break;
                case '(':
                    inputBuffer.moveNext();
                    return new Token(TokenType.LParen, "(", column, row);
                case ')':
                    inputBuffer.moveNext();
                    return new Token(TokenType.RParen, ")", column, row);
                case '\'':
                    inputBuffer.moveNext();
                    return new Token(TokenType.Quote, "'", column, row);
                case '"':
                    inputBuffer.moveNext();
                    return new Token(TokenType.String, readString(inputBuffer), column, row);
                default:
                    if (isBeginningOfNumber(current, next)) {
                        return new Token(TokenType.Number, readNumber(inputBuffer), column, row);
                    } else if (isBeginningOfSymbol(current)) {
                        return new Token(TokenType.Symbol, readSymbol(inputBuffer), column, row);
                    }

                    throw new IllegalArgumentException("Invalid input (" + current + ") at row " + row + ", column " + column);
            }
        }

        return null;
    }

    private boolean isBeginningOfSymbol(char current) {
        return !invalidSymbolCharacters.contains(current);
    }

    private boolean isBeginningOfNumber(char current, Character next) {
        return Character.isDigit(current) ||
               (current == '-' && next != null && Character.isDigit(next));
    }

    private String readString(InputBuffer inputBuffer) {
        StringBuilder stringBuilder = new StringBuilder();

        boolean finished = false;

        while (!inputBuffer.isEmpty() && !finished) {
            if (inputBuffer.current() == '\\') {
                inputBuffer.moveNext();

                stringBuilder.append(createEscapedLiteral(inputBuffer.current()));
            }
            else if (inputBuffer.current() == '"') {
                finished = true;
            }
            else {
                stringBuilder.append(inputBuffer.current());
            }

            inputBuffer.moveNext();
        }

        if (finished) {
            return stringBuilder.toString();
        }

        throw new IllegalArgumentException("Unterminated string detected at " + inputBuffer.currentRow() + ", column " + inputBuffer.currentColumn());
    }

    private String readNumber(InputBuffer inputBuffer) {
        StringBuilder numberBuilder = new StringBuilder();

        boolean hasDecimalPoint = false;

        numberBuilder.append(inputBuffer.current());
        inputBuffer.moveNext();

        while (!inputBuffer.isEmpty() &&
               (Character.isDigit(inputBuffer.current()) || inputBuffer.current() == '.')) {
            if (inputBuffer.current() == '.') {
                if (hasDecimalPoint) {
                    throw new IllegalArgumentException("Invalid number format detected at row " + inputBuffer.currentRow() + ", column " + inputBuffer.currentRow());
                }

                hasDecimalPoint = true;
            }

            numberBuilder.append(inputBuffer.current());
            inputBuffer.moveNext();
        }

        return numberBuilder.toString();
    }

    private String readSymbol(InputBuffer inputBuffer) {
        StringBuilder symbolBuilder = new StringBuilder();

        while (!inputBuffer.isEmpty() &&
               !invalidSymbolCharacters.contains(inputBuffer.current())) {
            symbolBuilder.append(inputBuffer.current());
            inputBuffer.moveNext();
        }

        return symbolBuilder.toString();
    }

    private char createEscapedLiteral(char ch) {
        switch(ch) {
            case 't':
                return '\t';
            case 'r':
                return '\r';
            case 'n':
                return '\n';
            case 'b':
                return '\b';
            case 'f':
                return '\f';
            default:
                return ch;
        }
    }
}
