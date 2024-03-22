package com.regtab.core.model.semantics;

import com.regtab.core.model.ICell;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder(toBuilder = true)
@Getter
@NonNull
public class Expr {
    private Expr expr;
    private boolean not;
    private Expr left;
    private Expr right;
    private CompOperator compOperator;
    private BinaryOperator binaryOperator;
    private ArithmOperator arithmOperator;
    private StrOperator strOperator;
    private Boolean bool;
    private Integer integer;
    private String string;
    private Prop prop;
    private Func func;
    private boolean useCaller;

    public Object evalThis(ICell caller) {
        useCaller = true;
        return eval(caller, null);
    }

    public Object eval(ICell caller, ICell candidate) {
        if (expr != null) {
            if (not)
                return ! (Boolean) expr.eval(caller, candidate);
            else
                return expr.eval(caller, candidate);
        }

        if (compOperator != null) {
            Object o1 = left.eval(caller, candidate);
            Object o2 = right.eval(caller, candidate);

            return switch (compOperator) {
                case EQUAL -> o1.equals(o2);
                case NOT_EQUAL -> !o1.equals(o2);
                case GREATER -> (Integer) o1 > (Integer) o2;
                case GREATER_OR_EQUAL -> (Integer) o1 >= (Integer) o2;
                case LESS -> (Integer) o1 < (Integer) o2;
                case LESS_OR_EQUAL -> (Integer) o1 <= (Integer) o2;
                case CONTAINS -> ((String) o1).contains((String) o2);
                case MATCHES -> ((String) o1).matches((String) o2);
            };
        }

        if (binaryOperator != null) {
            Boolean b1 = (Boolean) left.eval(caller, candidate);
            Boolean b2 = (Boolean) right.eval(caller, candidate);

            return switch (binaryOperator) {
                case AND -> b1 && b2;
                case OR -> b1 || b2;
            };
        }

        if (arithmOperator != null) {
            Integer i1 = (Integer) left.eval(caller, candidate);
            Integer i2 = (Integer) right.eval(caller, candidate);

            // Невозможная ситуация
            if (i1 == null || i2 == null) return null;

            return switch (arithmOperator) {
                case SUM -> i1 + i2;
                case SUB -> i1 - i2;
                case MUL -> i1 * i2;
                case MOD -> i1 % i2;
            };
        }

        if (strOperator != null) {
            String s1 = (String) left.eval(caller, candidate);
            String s2 = (String) right.eval(caller, candidate);

            // Невозможная ситуация
            if (s1 == null || s2 == null) return null;

            return switch (strOperator) {
                case CONCAT -> s1 + s2;
            };
        }

        if (prop != null)
            return useCaller ? prop.eval(caller) : prop.eval(candidate);

        if (func != null)
            return useCaller ? func.eval(caller) : func.eval(candidate);

        if (bool != null)
            return bool;

        if (integer != null)
            return integer;

        if (string != null)
            return string;

        return null;
    }

    public enum CompOperator {
        EQUAL("=="),
        NOT_EQUAL("!="),
        GREATER(">"),
        GREATER_OR_EQUAL(">="),
        LESS("<"),
        LESS_OR_EQUAL("<="),
        CONTAINS("contains"),
        MATCHES("matches");

        public final String token;

        public static CompOperator get(String token) {
            for (CompOperator value : values()) {
                if (value.token.equals(token))
                    return value;
            }
            return null;
        }

        CompOperator(String token) {
            this.token = token;
        }
    }

    public enum BinaryOperator {
        AND("&"),
        OR("|");

        public final String token;

        public static BinaryOperator get(String token) {
            for (BinaryOperator value : values()) {
                if (value.token.equals(token))
                    return value;
            }
            return null;
        }

        BinaryOperator(String token) {
            this.token = token;
        }
    }

    public enum ArithmOperator {
        SUM("+"),
        SUB("-"),
        MUL("*"),
        MOD("%");

        public final String token;

        public static ArithmOperator get(String token) {
            for (ArithmOperator value : values()) {
                if (value.token.equals(token))
                    return value;
            }
            return null;
        }

        ArithmOperator(String token) {
            this.token = token;
        }
    }

    public enum StrOperator {
        CONCAT("+");

        public final String token;

        public static StrOperator get(String token) {
            for (StrOperator value : values()) {
                if (value.token.equals(token))
                    return value;
            }
            return null;
        }

        StrOperator(String token) {
            this.token = token;
        }
    }
}

