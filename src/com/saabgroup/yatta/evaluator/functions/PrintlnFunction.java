package com.saabgroup.yatta.evaluator.functions;

import java.util.List;

public class PrintlnFunction implements IFunction {
    public Object apply(List args) throws Exception {
        StringBuilder sb = new StringBuilder();

        for (Object arg : args) {
            sb.append(arg.toString());
        }

        System.out.println(sb.toString());

        return null;
    }
}
