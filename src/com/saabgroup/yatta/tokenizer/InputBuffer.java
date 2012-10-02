package com.saabgroup.yatta.tokenizer;

public class InputBuffer {
    private final String input;

    private int location = 0;
    private int row = 0;
    private int column = 0;

    public InputBuffer(String input) {
        this.input = normalizeInput(input);
    }

    private String normalizeInput(String input) {
        return input.replace("\r", "");
    }

    public void moveNext() {
        if (current() == '\n') {
            row++;
            column = 0;
        }
        else {
            column++;
        }

        location++;
    }

    public void movePastWhitespace() {
        while (!isEmpty() && isWhitespace(current())) {
            moveNext();
        }
    }

    public void moveToNextLine() {
        while (!isEmpty() && current() != '\n') {
            moveNext();
        }
    }

    public Character peekNext() {
        if (location < input.length() - 1) {
            return input.charAt(location + 1);
        }

        return null;
    }

    public char current() {
        return input.charAt(location);
    }

    public int currentRow() {
        return row;
    }

    public int currentColumn() {
        return column;
    }

    public boolean isEmpty() {
        return location >= input.length();
    }

    private boolean isWhitespace(char ch) {
        return ch == ' ' ||
               ch == '\t' ||
               ch == '\n';
    }
}
