package com.saabgroup.yatta.tokenizer;

import java.util.*;

public class Tokenizer implements ITokenizer {
    private final HashSet<Character> invalidSymbolCharacters = new HashSet<Character>(Arrays.asList(new Character[] { '(', ')', ' ', '\n', '\t', '\r' }));

    private final Character[] lookAhead = new Character[2];
    private String inputBuffer;
    private int readLocation = 0;
    private int col;
    private int row;

    public List<Token> tokenize(String input) {
        readLocation = 0;
        inputBuffer = sanitizeInput(input);
        updateLookAhead();

        ArrayList<Token> tokens = new ArrayList<Token>();

        while (hasMoreTokens()) {
            Token token = getNextToken();
            if (token != null) {
                tokens.add(token);
            }
        }

        tokens.add(new Token(TokenType.EOF, "<EOF>", col, row));

        return Collections.unmodifiableList(tokens);
    }

    private String sanitizeInput(String input) {
        return input.replace("\r", "");
    }

    private boolean hasMoreTokens() {
        return lookAhead[0] != null;
    }

    private Token getNextToken() {
        while (lookAhead[0] != null) {
            switch (lookAhead[0]) {
                case ' ':
                case '\t':
                case '\n':
                    consumeWhitespace();
                    break;
                case ';':
                    consumeComment();
                    break;
                case '(':
                    consume();
                    return new Token(TokenType.LParen, "(", col, row);
                case ')':
                    consume();
                    return new Token(TokenType.RParen, ")", col, row);
                case '\'':
                    consume();
                    return new Token(TokenType.Quote, "'", col, row);
                case '"':
                    return parseString();
                default:
                    if ((lookAhead[0] == '-' &&
                         lookAhead[1] != null && Character.isDigit(lookAhead[1])) ||
                        Character.isDigit(lookAhead[0]))
                    {
                        return parseNumber();
                    }

                    if (!invalidSymbolCharacters.contains(lookAhead[0]))
                    {
                        return parseSymbol();
                    }

                    throw new IllegalArgumentException("Invalid input (" + lookAhead[0] + ") at row " + row + ", column " + col);
            }
        }

        return null;
    }

    private void consumeWhitespace() {
        while (lookAhead[0] == ' ' ||
               lookAhead[0] == '\t' ||
               lookAhead[0] == '\n') {
            consume();
        }
    }

    private void consumeComment() {
        while (lookAhead[0] != '\n') {
            consume();
        }
    }

    private void consume() {
        if (lookAhead[0] == '\n') {
            row++;
            col = 0;
        }
        else
        {
            col++;
        }

        readLocation++;
        updateLookAhead();
    }

    private void updateLookAhead() {
        lookAhead[0] = readLocation < inputBuffer.length() ? inputBuffer.charAt(readLocation) : null;
        lookAhead[1] = readLocation + 1 < inputBuffer.length() ? inputBuffer.charAt(readLocation + 1) : null;
    }

    private Token parseString() {
        StringBuilder tokenBuffer = new StringBuilder();

        int beginColumn = 0;
        int beginRow = 0;

        boolean begun = false;
        boolean finished = false;

        while (lookAhead[0] != null && !finished) {
            if (lookAhead[0] == '\\') {
                consume();

                char ch = lookAhead[0];

                switch(lookAhead[0]) {
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

                tokenBuffer.append(ch);
            }
            else if (lookAhead[0] == '"') {
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
                tokenBuffer.append(lookAhead[0]);
            }

            consume();
        }

        if (finished) {
            return new Token(TokenType.String, tokenBuffer.toString(), beginColumn, beginRow);
        }

        throw new IllegalArgumentException("Unterminated string detected at " + beginRow + ", column " + beginColumn);
    }

    private Token parseNumber() {
        StringBuilder tokenBuffer = new StringBuilder();

        int beginColumn = col;
        int beginRow = row;

        boolean hasDecimalPoint = false;

        tokenBuffer.append(lookAhead[0]);
        consume();

        while (lookAhead[0] != null &&
               (Character.isDigit(lookAhead[0]) || lookAhead[0] == '.')) {
            if (lookAhead[0] == '.') {
                if (hasDecimalPoint) {
                    throw new IllegalArgumentException("Invalid number format detected at row " + row + ", column " + col);
                }

                hasDecimalPoint = true;
            }

            tokenBuffer.append(lookAhead[0]);
            consume();
        }

        return new Token(TokenType.Number, tokenBuffer.toString(), beginColumn, beginRow);
    }

    private Token parseSymbol() {
        StringBuilder tokenBuffer = new StringBuilder();

        int beginColumn = col;
        int beginRow = row;

        while (lookAhead[0] != null && !invalidSymbolCharacters.contains(lookAhead[0])) {
            tokenBuffer.append(lookAhead[0]);
            consume();
        }

        return new Token(TokenType.Symbol, tokenBuffer.toString(), beginColumn, beginRow);
    }
}

