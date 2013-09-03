package com.saabgroup.yatta.evaluator;

import com.saabgroup.yatta.*;
import com.saabgroup.yatta.evaluator.functions.*;
import com.saabgroup.yatta.evaluator.special_forms.*;
import com.saabgroup.yatta.reader.IReader;
import com.saabgroup.yatta.reader.Reader;
import com.saabgroup.yatta.tokenizer.Tokenizer;

import java.util.*;

public class Evaluator implements IEvaluator {
    private static final String YATTA_CORE_NS_NAME = "yatta.core";
    private static final String YATTA_USER_NS_NAME = "yatta.user";

    private final IReader parser;
    private final BackquoteExpander backquoteExpander;
    private final MacroExpander macroExpander;

    private final Environment rootEnvironment;
    private IExternalAccessorFunction accessorFunction;
    private Namespace currentNamespace;

    public Evaluator() {
        this(new Reader(new Tokenizer()));
    }

    public Evaluator(IReader parser) {
        this.parser = parser;
        this.backquoteExpander = new BackquoteExpander(this);
        this.macroExpander = new MacroExpander(this);
        rootEnvironment = new Environment();

        setupInitialEnvironment();
    }

    public Namespace getCurrentNamespace() {
        return currentNamespace;
    }

    public void setCurrentNamespace(Namespace namespace) {
        currentNamespace = namespace;
    }

    public Object evaluate(String input) throws Exception {
        return evaluate(input, new Environment(new HashMap<String, Object>()));
    }

    public Object evaluate(String input, IEnvironment env) throws Exception {
        Collection<Object> forms = parser.read(input);
        env = rootEnvironment.createChildEnvironment(env);

        return evaluate(forms, env);
    }

    private Object evaluate(Collection<Object> forms, IEnvironment env) throws Exception {
        Object lastResult = null;

        for (Object form : forms) {
            Object expanded = macroExpander.expand(form, env);

            lastResult = evaluate(expanded, env);
        }

        return lastResult;
    }

    public Object evaluate(Object form, IEnvironment env) throws Exception {
        if (form instanceof Symbol) {
            Object val = lookUpSymbol((Symbol) form, env);
            if (val instanceof IValue) {
                return ((IValue)val).getValue(this, env);
            }
            return val;
        } else if (form instanceof Quoted) {
            return ((Quoted)form).getQuotedValue();
        } else if (form instanceof Backquote) {
            return backquoteExpander.expand((Backquote)form, env);
        } else if (form instanceof Tilde) {
            throw new Exception("~ not allowed outside of backquote!");
        } else if (form instanceof Splice) {
            throw new Exception("~@ not allowed outside of backquote!");
        } else if (form instanceof List) {
            return evaluateList((List) form, env);
        } else if (form instanceof ExternalAccessor) {
            return evaluateExternalAccessor((ExternalAccessor)form, env);
        } else if (form instanceof Macro) {
            throw new Exception("Macro found in already expanded code!");
        } else {
            return form;
        }
    }

    private Object lookUpSymbol(Symbol symbol, IEnvironment env) throws Exception {
        if (symbol.hasNamespace()) {
            return env.lookUp(symbol);
        }

        Symbol namespacedSymbol = Symbol.create(currentNamespace.getName(), symbol.getName());
        if (env.hasDefinedValue(namespacedSymbol)) {
            return env.lookUp(namespacedSymbol);
        }

        Symbol coreSymbol = Symbol.create(YATTA_CORE_NS_NAME, symbol.getName());
        if (env.hasDefinedValue(coreSymbol)) {
            return env.lookUp(coreSymbol);
        }

        return env.lookUp(symbol);
    }

    private Object evaluateExternalAccessor(ExternalAccessor accessor, IEnvironment env) {
        if (accessorFunction == null) {
            return null;
        }

        return accessorFunction.lookup(accessor.getPath());
    }

    public void setRootBinding(String name, Object value) {
        rootEnvironment.put(Symbol.create(currentNamespace.getName(), name).getName(), value);
    }

    public IEnvironment getEnvironment() {
        return rootEnvironment;
    }

    public void setExternalAccessor(IExternalAccessorFunction accessorFunction) {
        this.accessorFunction = accessorFunction;
    }

    private void setupInitialEnvironment() {
        currentNamespace = new Namespace("yatta.core");

        setRootBinding("*ns*", new NsValue());
        setRootBinding("ns", new NsSpecialForm(this));

        setRootBinding("true", true);
        setRootBinding("false", false);
        setRootBinding("nil", null);

        setRootBinding("and", new AndSpecialForm(this));
        setRootBinding("or", new OrSpecialForm(this));
        setRootBinding("do", new DoSpecialForm(this));
        setRootBinding("lambda", new LambdaSpecialForm(this));
        setRootBinding("def", new DefSpecialForm(this));
        setRootBinding("defmacro", new DefMacroSpecialForm(this));
        setRootBinding("if", new IfSpecialForm(this));
        setRootBinding("cond", new CondSpecialForm(this));
        setRootBinding("let", new LetSpecialForm(this));

        setRootBinding("+", new PlusFunction());
        setRootBinding("*", new MultiplyFunction());
        setRootBinding("=", new EqualsFunction());
        setRootBinding("<", new LessThanFunction());
        setRootBinding("not", new NotFunction());
        setRootBinding("map", new MapFunction());
        setRootBinding("list", new ListFunction());
        setRootBinding("cons", new ConsFunction());
        setRootBinding("macro-expand-1", new MacroExpand1Function(this, macroExpander));
        setRootBinding("macro-expand", new MacroExpandFunction(this, macroExpander));
        setRootBinding("println", new PrintlnFunction());

        Namespace userNamespace = new Namespace(YATTA_USER_NS_NAME);
        currentNamespace = userNamespace;
    }

    private Object evaluateList(List list, IEnvironment env) throws Exception {
        Object firstItem = evaluate(list.get(0), env);

        List args = new ArrayList(list);
        args.remove(0);

        if (firstItem instanceof ISpecialForm) {
            return applySpecialForm((ISpecialForm)firstItem, args, env);
        } else if (firstItem instanceof IFunction) {
            return applyFunction((IFunction)firstItem, args, env);
        } else if (firstItem instanceof Macro) {
            throw new Exception("Macro found in already expanded code!");
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
