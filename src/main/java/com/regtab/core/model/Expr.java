package com.regtab.core.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder(toBuilder = true)
@Getter
@NonNull
public class Expr {
    private Expr notExpr;
    private Expr left;
    private Expr right;
    private CompOperator compOperator;
    private BinaryOperator binaryOperator;
    private ArithmOperator arithmOperator;
    private StrOperator strOperator;
    private Boolean bool;
    private Integer integer;
    private String string;
    private String hex;
    private Prop<?> prop;
    private Func<?> func;

    private boolean useCaller;

    public Object eval(@NonNull ICell caller) {
        return eval(caller, null);
    }

    public Object eval(@NonNull ICell caller, ICell candidate) {
       if (candidate == null)
            useCaller = true;

        if (notExpr != null) {
            return evalNot(caller, candidate);
        }

        if (compOperator != null) {
            return evalCompOp(caller, candidate);
        }

        if (binaryOperator != null) {
            return evalBinaryOp(caller, candidate);
        }

        if (arithmOperator != null) {
            return evalArithmOp(caller, candidate);
        }

        if (strOperator != null) {
            return evalStrOp(caller, candidate);
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

        if (hex != null)
            return hex;

        return null;
    }

    private Boolean evalNot(ICell caller, ICell candidate) {
        return ! (Boolean) notExpr.eval(caller, candidate);
    }

    private Boolean evalCompOp(ICell caller, ICell candidate) {
        final Object o1 = left.eval(caller, candidate);
        final Object o2 = right.eval(caller, candidate);

        if (o1 == null || o2 == null)
            return false;

        return switch (compOperator) {
            case EQUAL ->  o1.equals(o2);
            case NOT_EQUAL -> !o1.equals(o2);
            case GREATER -> (Integer) o1 > (Integer) o2;
            case GREATER_OR_EQUAL -> (Integer) o1 >= (Integer) o2;
            case LESS -> (Integer) o1 < (Integer) o2;
            case LESS_OR_EQUAL -> (Integer) o1 <= (Integer) o2;
            case CONTAINS -> ((String) o1).contains((String) o2);
            case MATCHES -> ((String) o1).matches((String) o2);
        };
    }

    private Boolean evalBinaryOp(ICell caller, ICell candidate) {
        final Boolean b1 = (Boolean) left.eval(caller, candidate);
        final Boolean b2 = (Boolean) right.eval(caller, candidate);

        return switch (binaryOperator) {
            case AND -> b1 && b2;
            case OR -> b1 || b2;
        };
    }

    private Integer evalArithmOp(ICell caller, ICell candidate) {
        final Integer i1 = (Integer) left.eval(caller, candidate);
        final Integer i2 = (Integer) right.eval(caller, candidate);

        if (i1 == null || i2 == null)
            return null; // Impossible

        return switch (arithmOperator) {
            case SUM -> i1 + i2;
            case SUB -> i1 - i2;
            case MUL -> i1 * i2;
            case MOD -> i1 % i2;
        };
    }

    private String evalStrOp(ICell caller, ICell candidate) {
        final Object o1 = left.eval(caller, candidate);
        final Object o2 = right.eval(caller, candidate);

        final String s1;
        if (o1 == null)
            s1 = "";
        else if (o1 instanceof String)
            s1 = (String) o1;
        else if (o1 instanceof Integer || o1 instanceof Boolean)
            s1 = String.valueOf(o1);
        else
            s1 = o1.toString();

        final String s2;
        if (o2 == null)
            s2 = "";
        else if (o2 instanceof String)
            s2 = (String) o2;
        else if (o2 instanceof Integer || o2 instanceof Boolean)
            s2 = String.valueOf(o2);
        else
            s2 = o2.toString();

        return switch (strOperator) {
            case CONCAT -> s1 + s2;
        };
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

