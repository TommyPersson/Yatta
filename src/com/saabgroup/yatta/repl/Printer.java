package com.saabgroup.yatta.repl;

import com.saabgroup.yatta.ExternalAccessor;
import com.saabgroup.yatta.Quoted;
import com.saabgroup.yatta.Symbol;

import java.util.List;

public class Printer {
    public String print(Object form) throws Exception {
        return printObject(form);
    }

    private String printObject(Object obj) {
        if (obj instanceof Symbol) {
            return printSymbol((Symbol)obj);
        } else if (obj instanceof Quoted) {
            return printQuotedValue((Quoted)obj);
        } else if (obj instanceof List) {
            return printList((List) obj);
        } else if (obj instanceof ExternalAccessor) {
            return printExternalAccessor((ExternalAccessor)obj);
        } else if (obj instanceof String) {
            return printString((String)obj);
        } else {
            if (obj == null) {
                return "nil";
            }

            return obj.toString();
        }
    }

    private String printString(String str) {
        return String.format("\"%s\"", str);
    }

    private String printExternalAccessor(ExternalAccessor acc) {
        return String.format("<%s>", acc.getPath());
    }

    private String printList(List list) {
        StringBuilder sb = new StringBuilder();

        sb.append("(");

        for (Object obj : list) {
            sb.append(printObject(obj));
            sb.append(" ");
        }

        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");

        return sb.toString();
    }

    private String printQuotedValue(Quoted quoted) {
        return String.format("'%s", printObject(quoted.getQuotedValue()));
    }

    private String printSymbol(Symbol sym) {
        return sym.getName();
    }
}
