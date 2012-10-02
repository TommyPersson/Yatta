package com.saabgroup.yatta.tokenizer;

public class InputBuffer {
    private final String input;
    private int location = 0;

    public InputBuffer(String input) {
        this.input = normalizeInput(input);
    }

    private String normalizeInput(String input) {
        return input.replace("\r", "");
    }

    public void moveNext() {
        location++;
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

    public boolean isEmpty() {
        return location >= input.length();
    }
}
