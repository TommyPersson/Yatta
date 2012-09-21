package com.saabgroup.yatta.evaluator;

import com.saabgroup.yatta.Quoted;
import com.saabgroup.yatta.Symbol;
import com.saabgroup.yatta.evaluator.functions.*;
import com.saabgroup.yatta.parser.IParser;
import com.saabgroup.yatta.parser.Parser;
import com.saabgroup.yatta.tokenizer.Tokenizer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Evaluator implements IEvaluator {
    private final IParser parser;

    private final Environment rootEnvironment;

    public Evaluator() {
        this(new Parser(new Tokenizer()));
    }

    public Evaluator(IParser parser) {
        this.parser = parser;

        rootEnvironment = createRootEnvironment();
    }

    public Object evaluate(String input) throws Exception {
        return evaluate(input, new Environment(new HashMap<String, Object>()));
    }

    public Object evaluate(String input, IEnvironment env) throws Exception {
        Collection<Object> forms = parser.parse(input);
        env = rootEnvironment.createChildEnvironment(env);

        return evaluate(forms, env);
    }

    public Object evaluate(Collection<Object> forms, IEnvironment env) throws Exception {
        Object lastResult = null;

        for (Object form : forms) {
            lastResult = evaluate(form, env);
        }

        return lastResult;
    }

    public Object evaluate(Object form, IEnvironment env) throws Exception {
        if (form instanceof BigDecimal ||
            form instanceof String ||
            form instanceof Boolean) {
            return form;
        } else if (form instanceof Symbol) {
            return env.lookUp((Symbol)form);
        } else if (form instanceof Quoted) {
            return ((Quoted)form).getQuotedValue();
        } else if (form instanceof ArrayList) {
            return evaluateList((ArrayList)form, env);
        }

        throw new Exception("Wat");
    }

    public void setRootBinding(String name, Object value) {
        rootEnvironment.put(name, value);
    }

    private Environment createRootEnvironment() {
        Map<String, Object> rootDefs = new HashMap<String, Object>();
        rootDefs.put("true", true);
        rootDefs.put("false", false);
        rootDefs.put("nil", null);
        rootDefs.put("+", new PlusFunction());
        rootDefs.put("=", new EqualsFunction());
        rootDefs.put("<", new LessThanFunction());
        rootDefs.put("and", new AndSpecialForm(this));
        rootDefs.put("or", new OrSpecialForm(this));
        rootDefs.put("not", new NotFunction());
        rootDefs.put("lambda", new LambdaSpecialForm(this));
        rootDefs.put("def", new DefSpecialForm(this));
        rootDefs.put("if", new IfSpecialForm(this));
        rootDefs.put("let", new LetSpecialForm(this));
        rootDefs.put("map", new MapFunction());
        rootDefs.put("list", new ListFunction());
        rootDefs.put("do", new DoSpecialForm(this));

        return new Environment(rootDefs);
    }

    private Object evaluateList(ArrayList list, IEnvironment env) throws Exception {
        IFunction function = (IFunction)evaluate(list.get(0), env);

        ArrayList args = new ArrayList(list);
        args.remove(0);

        if (!function.isSpecialForm()) {
            ArrayList evaluatedArgs = new ArrayList();

            for (Object arg : args) {
                evaluatedArgs.add(evaluate(arg, env));
            }

            return function.apply(evaluatedArgs, env);
        }

        return function.apply(args, env);
    }
}
