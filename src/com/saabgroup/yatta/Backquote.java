package com.saabgroup.yatta;

public class Backquote {
    private Object quotedValue;

    public Backquote(Object quotedValue) {
        this.quotedValue = quotedValue;
    }

    public Object getQuotedValue() {
        return quotedValue;
    }
}
