package com.saabgroup.yatta.tokenizer;

import java.util.*;

public class Tokenizer implements ITokenizer {
    private final HashSet<Character> invalidSymbolCharacters = new HashSet<Character>(Arrays.asList(new Character[] { '(', ')', ' ', '\n', '\t', '\r' }));

    private InputBuffer inputBuffer;
    private int col;
    private int row;

    public List<Token> tokenize(String input) {
        inputBuffer = new InputBuffer(input);

        ArrayList<Token> tokens = new ArrayList<Token>();

        while (!inputBuffer.isEmpty()) {
            Token token = getNextToken(inputBuffer);
            if (token != null) {
                tokens.add(token);
            }
        }

        tokens.add(new Token(TokenType.EOF, "<EOF>", col, row));

        return Collections.unmodifiableList(tokens);
    }

    private Token getNextToken(InputBuffer inputBuffer) {
        while (!inputBuffer.isEmpty()) {
            char current = inputBuffer.current();
            Character next = inputBuffer.peekNext();

            switch (current) {
                case ' ':
                case '\t':
                case '\n':
                    consumeWhitespace(inputBuffer);
                    break;
                case ';':
                    consumeComment(inputBuffer);
                    break;
                case '(':
                    consume(inputBuffer);
                    return new Token(TokenType.LParen, "(", col, row);
                case ')':
                    consume(inputBuffer);
                    return new Token(TokenType.RParen, ")", col, row);
                case '\'':
                    consume(inputBuffer);
                    return new Token(TokenType.Quote, "'", col, row);
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

                    throw new IllegalArgumentException("Invalid input (" + current + ") at row " + row + ", column " + col);
            }
        }

        return null;
    }

    private void consumeWhitespace(InputBuffer inputBuffer) {
        while (isWhitespace(inputBuffer.current())) {
            consume(inputBuffer);
        }
    }

    private boolean isWhitespace(char ch) {
        return ch == ' ' ||
               ch == '\t' ||
               ch == '\n';
    }

    private void consumeComment(InputBuffer inputBuffer) {
        while (inputBuffer.current() != '\n') {
            consume(inputBuffer);
        }
    }

    private void consume(InputBuffer inputBuffer) {
        if (inputBuffer.current() == '\n') {
            row++;
            col = 0;
        }
        else {
            col++;
        }

        inputBuffer.moveNext();
    }

    private Token readString(InputBuffer inputBuffer) {
        StringBuilder tokenBuilder = new StringBuilder();

        int beginColumn = 0;
        int beginRow = 0;

        boolean begun = false;
        boolean finished = false;

        while (!inputBuffer.isEmpty() && !finished) {
            if (inputBuffer.current() == '\\') {
                consume(inputBuffer);

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
                    beginColumn = col;
                    beginRow = row;
                    begun = true;
                }
                else {
                    finished = true;
                }
            }
            else {
                tokenBuilder.append(inputBuffer.current());
            }

            consume(inputBuffer);
        }

        if (finished) {
            return new Token(TokenType.String, tokenBuilder.toString(), beginColumn, beginRow);
        }

        throw new IllegalArgumentException("Unterminated string detected at " + beginRow + ", column " + beginColumn);
    }

    private Token readNumber(InputBuffer inputBuffer) {
        StringBuilder tokenBuilder = new StringBuilder();

        int beginColumn = col;
        int beginRow = row;

        boolean hasDecimalPoint = false;

        tokenBuilder.append(inputBuffer.current());
        consume(this.inputBuffer);

        while (!inputBuffer.isEmpty() &&
               (Character.isDigit(inputBuffer.current()) || inputBuffer.current() == '.')) {
            if (inputBuffer.current() == '.') {
                if (hasDecimalPoint) {
                    throw new IllegalArgumentException("Invalid number format detected at row " + row + ", column " + col);
                }

                hasDecimalPoint = true;
            }

            tokenBuilder.append(inputBuffer.current());
            consume(inputBuffer);
        }

        return new Token(TokenType.Number, tokenBuilder.toString(), beginColumn, beginRow);
    }

    private Token readSymbol(InputBuffer inputBuffer) {
        StringBuilder tokenBuilder = new StringBuilder();

        int beginColumn = col;
        int beginRow = row;

        while (!inputBuffer.isEmpty() &&
               !invalidSymbolCharacters.contains(inputBuffer.current())) {
            tokenBuilder.append(inputBuffer.current());
            consume(inputBuffer);
        }

        return new Token(TokenType.Symbol, tokenBuilder.toString(), beginColumn, beginRow);
    }
}

