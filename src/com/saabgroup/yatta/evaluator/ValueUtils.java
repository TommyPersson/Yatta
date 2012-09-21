package com.saabgroup.yatta.evaluator;

public class ValueUtils {
    public static boolean isTruthy(Object value) {
        boolean isNotNull = value != null;
        boolean isFalse = value instanceof Boolean && !(Boolean)value;

        return isNotNull && !isFalse;
    }
}
