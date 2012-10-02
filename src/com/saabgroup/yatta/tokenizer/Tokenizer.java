package com.saabgroup.yatta.tokenizer;

import java.util.*;

public class Tokenizer implements ITokenizer {
    private final HashSet<Character> invalidSymbolCharacters = new HashSet<Character>(Arrays.asList(new Character[] { '(', ')', ' ', '\n', '\t', '\r' }));

    public List<Token> tokenize(String input) {
        InputBuffer inputBuffer = new InputBuffer(input);
        ArrayList<Token> tokens = new ArrayList<Token>();

        while (!inputBuffer.isEmpty()) {
            Token token = readNextToken(inputBuffer);
            if (token != null) {
                tokens.add(token);
            }
        }

        tokens.add(new Token(TokenType.EOF, "<EOF>", inputBuffer.currentColumn(), inputBuffer.currentRow()));

        return Collections.unmodifiableList(tokens);
    }

    private Token readNextToken(InputBuffer inputBuffer) {
        while (!inputBuffer.isEmpty()) {
            char current = inputBuffer.current();
            Character next = inputBuffer.peekNext();

            switch (current) {
                case ' ':
                case '\t':
                case '\n':
                    inputBuffer.movePastWhitespace();
                    break;
                case ';':
                    inputBuffer.moveToNextLine();
                    break;
                case '(':
                    inputBuffer.moveNext();
                    return new Token(TokenType.LParen, "(", inputBuffer.currentColumn(), inputBuffer.currentRow());
                case ')':
                    inputBuffer.moveNext();
                    return new Token(TokenType.RParen, ")", inputBuffer.currentColumn(), inputBuffer.currentRow());
                case '\'':
                    inputBuffer.moveNext();
                    return new Token(TokenType.Quote, "'", inputBuffer.currentColumn(), inputBuffer.currentRow());
                case '"':
                    return readString(inputBuffer);
                default:
                    if ((current == '-' && next != null && Character.isDigit(next)) ||
                        Character.isDigit(current)) {
                        return readNumber(inputBuffer);
                    }

                    if (!invalidSymbolCharacters.contains(current)) {
                        return readSymbol(inputBuffer);
                    }

                    throw new IllegalArgumentException("Invalid input (" + current + ") at row " + inputBuffer.currentRow() + ", column " + inputBuffer.currentColumn());
            }
        }

        return null;
    }

    private Token readString(InputBuffer inputBuffer) {
        StringBuilder tokenBuilder = new StringBuilder();

        int beginColumn = 0;
        int beginRow = 0;

        boolean begun = false;
        boolean finished = false;

        while (!inputBuffer.isEmpty() && !finished) {
            if (inputBuffer.current() == '\\') {
                inputBuffer.moveNext();

                char ch = inputBuffer.current();

                switch(inputBuffer.current()) {
                    case 't':
                        ch = '\t';
                        break;
                    case 'r':
                        ch = '\r';
                        break;
                    case 'n':
                        ch = '\n';
                        break;
                    case '\\':
                        break;
                    case '"':
                        break;
                    case 'b':
                        ch = '\b';
                        break;
                    case 'f':
                        ch = '\f';
                        break;
                }

                tokenBuilder.append(ch);
            }
            else if (inputBuffer.current() == '"') {
                if (!begun) {
                    beginColumn = inputBuffer.currentColumn();
                    beginRow = inputBuffer.currentRow();
                    begun = true;
                }
                else {
                    finished = true;
                }
            }
            else {
                tokenBuilder.append(inputBuffer.current());
            }

            inputBuffer.moveNext();
        }

        if (finished) {
            return new Token(TokenType.String, tokenBuilder.toString(), beginColumn, beginRow);
        }

        throw new IllegalArgumentException("Unterminated string detected at " + beginRow + ", column " + beginColumn);
    }

    private Token readNumber(InputBuffer inputBuffer) {
        StringBuilder tokenBuilder = new StringBuilder();

        int beginColumn = inputBuffer.currentColumn();
        int beginRow = inputBuffer.currentRow();

        boolean hasDecimalPoint = false;

        tokenBuilder.append(inputBuffer.current());
        inputBuffer.moveNext();

        while (!inputBuffer.isEmpty() &&
               (Character.isDigit(inputBuffer.current()) || inputBuffer.current() == '.')) {
            if (inputBuffer.current() == '.') {
                if (hasDecimalPoint) {
                    throw new IllegalArgumentException("Invalid number format detected at row " + inputBuffer.currentRow() + ", column " + inputBuffer.currentRow());
                }

                hasDecimalPoint = true;
            }

            tokenBuilder.append(inputBuffer.current());
            inputBuffer.moveNext();
        }

        return new Token(TokenType.Number, tokenBuilder.toString(), beginColumn, beginRow);
    }

    private Token readSymbol(InputBuffer inputBuffer) {
        StringBuilder tokenBuilder = new StringBuilder();

        int beginColumn = inputBuffer.currentColumn();
        int beginRow = inputBuffer.currentRow();

        while (!inputBuffer.isEmpty() &&
               !invalidSymbolCharacters.contains(inputBuffer.current())) {
            tokenBuilder.append(inputBuffer.current());
            inputBuffer.moveNext();
        }

        return new Token(TokenType.Symbol, tokenBuilder.toString(), beginColumn, beginRow);
    }
}
