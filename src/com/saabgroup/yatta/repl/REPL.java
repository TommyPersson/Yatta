package com.saabgroup.yatta.repl;

import com.saabgroup.yatta.evaluator.Evaluator;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class REPL {
    public static void main(String args[]) {
        Evaluator evaluator = new Evaluator();

        boolean running = true;

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        while (running) {
            System.out.print(String.format("%s> ", evaluator.getCurrentNamespace().getName()));

            try {
                String input = in.readLine();
                Object result = evaluator.evaluate(input);
                System.out.println(String.format("=> %s", new Printer().print(result)));
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }
}
