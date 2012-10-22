package com.saabgroup.yatta.evaluator;

import com.saabgroup.yatta.MapLiteral;
import com.saabgroup.yatta.Quoted;
import com.saabgroup.yatta.Symbol;
import com.saabgroup.yatta.evaluator.functions.*;
import com.saabgroup.yatta.evaluator.special_forms.*;
import com.saabgroup.yatta.reader.IReader;
import com.saabgroup.yatta.reader.Reader;
import com.saabgroup.yatta.tokenizer.Tokenizer;

import java.util.*;

public class Evaluator implements IEvaluator {
    private final IReader parser;

    private final Environment rootEnvironment;

    public Evaluator() {
        this(new Reader(new Tokenizer()));
    }

    public Evaluator(IReader parser) {
        this.parser = parser;

        rootEnvironment = createRootEnvironment();
    }

    public Object evaluate(String input) throws Exception {
        return evaluate(input, new Environment(new HashMap<String, Object>()));
    }

    public Object evaluate(String input, IEnvironment env) throws Exception {
        Collection<Object> forms = parser.read(input);
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
        if (form instanceof Symbol) {
            return env.lookUp((Symbol) form);
        } else if (form instanceof Quoted) {
            return ((Quoted)form).getQuotedValue();
        } else if (form instanceof MapLiteral) {
            return evaluateMapLiteral((MapLiteral)form, env);
        } else if (form instanceof List) {
            return evaluateList((List)form, env);
        } else {
            return form;
        }
    }

    public void setRootBinding(String name, Object value) {
        rootEnvironment.put(name, value);
    }

    private Environment createRootEnvironment() {
        Map<String, Object> rootDefs = new HashMap<String, Object>();

        rootDefs.put("true", true);
        rootDefs.put("false", false);
        rootDefs.put("nil", null);

        rootDefs.put("and", new AndSpecialForm(this));
        rootDefs.put("or", new OrSpecialForm(this));
        rootDefs.put("do", new DoSpecialForm(this));
        rootDefs.put("lambda", new LambdaSpecialForm(this));
        rootDefs.put("def", new DefSpecialForm(this));
        rootDefs.put("if", new IfSpecialForm(this));
        rootDefs.put("cond", new CondSpecialForm(this));
        rootDefs.put("let", new LetSpecialForm(this));

        rootDefs.put("+", new PlusFunction());
        rootDefs.put("*", new MultiplyFunction());
        rootDefs.put("=", new EqualsFunction());
        rootDefs.put("<", new LessThanFunction());
        rootDefs.put("not", new NotFunction());
        rootDefs.put("map", new MapFunction());
        rootDefs.put("list", new ListFunction());

        return new Environment(rootDefs);
    }

    private Object evaluateMapLiteral(MapLiteral mapLiteral, IEnvironment env) throws Exception {
        HashMap<Object, Object> resultMap = new HashMap<Object, Object>();

        for (Map.Entry<Object, Object> entry : mapLiteral.getEntries()) {
            Object keyVal = evaluate(entry.getKey(), env);
            Object valueVal = evaluate(entry.getValue(), env);

            resultMap.put(keyVal, valueVal);
        }

        return Collections.unmodifiableMap(resultMap);
    }

    private Object evaluateList(List list, IEnvironment env) throws Exception {
        Object firstItem = evaluate(list.get(0), env);

        List args = new ArrayList(list);
        args.remove(0);

        if (firstItem instanceof ISpecialForm) {
            return applySpecialForm((ISpecialForm)firstItem, args, env);
        } else if (firstItem instanceof IFunction) {
            return applyFunction((IFunction)firstItem, args, env);
        }

        throw new IllegalArgumentException("Not a function dude");
    }

    private Object applySpecialForm(ISpecialForm specialForm, List args, IEnvironment env) throws Exception {
        return specialForm.apply(args, env);
    }

    private Object applyFunction(IFunction function, List args, IEnvironment env) throws Exception {
        ArrayList evaluatedArgs = new ArrayList();

        for (Object arg : args) {
            evaluatedArgs.add(evaluate(arg, env));
        }

        return function.apply(evaluatedArgs);
    }
}
