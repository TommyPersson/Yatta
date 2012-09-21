package com.saabgroup.yatta;

public enum QuoteType {
    Normal;

    public Object createQuoted(Object value) {
        switch (this) {
            case Normal:
                return new Quoted(value);
        }

        return null;
    }
}
