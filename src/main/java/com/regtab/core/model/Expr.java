package com.regtab.core.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * The Expr class represents a boolean or arithmetic expression that can be evaluated against a cell.
 */
@Slf4j
@Builder(toBuilder = true)
@Getter
@NonNull
public class Expr {
    private String asString;

    private Expr notExpr;
    private Expr left;
    private Expr right;
    private CompOperator compOperator;
    private BinaryOperator binaryOperator;
    private ArithmOperator arithmOperator;
    private StrOperator strOperator;
    private Boolean bool;
    private Integer integer;
    private Double doubl;
    private String string;
    private String hex;
    private Prop<?> prop;
    private Func<?> func;

    private boolean useCaller;

    /**
     * Evaluates the expression using the caller cell.
     *
     * @param caller The caller cell.
     * @return The result of the evaluation.
     */
    public Object eval(@NonNull ICell caller) {
        return eval(caller, null);
    }

    /**
     * Evaluates the expression using the caller and candidate cells.
     *
     * @param caller The caller cell.
     * @param candidate The candidate cell.
     * @return The result of the evaluation.
     */
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

        if (doubl != null)
            return doubl;

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

        return switch (compOperator) {
            case EQUAL -> equal(o1, o2);
            case NOT_EQUAL -> notEqual(o1, o2);
            case GREATER -> greater(o1, o2);
            case GREATER_OR_EQUAL -> greaterOrEqual(o1, o2);
            case LESS -> less(o1, o2);
            case LESS_OR_EQUAL -> lessOrEqual(o1, o2);
            case CONTAINS -> contains(o1, o2);
            case MATCHES -> matches(o1, o2);
        };
    }

    private boolean equal(Object o1, Object o2) {
        if (o1 == null && o2 == null)
            return true;

        if (o1 == null ^ o2 == null)
            return false;

        if (o1 instanceof Boolean && o2 instanceof Boolean) {
            return o1.equals(o2);
        }
        if (o1 instanceof String && o2 instanceof String) {
            return o1.equals(o2);
        }
        if (o1 instanceof Number && o2 instanceof Number) {
            return asDouble(o1) == asDouble(o2);
        }

        throw new IllegalExpressionException(this, o1, o2);
    }

    private boolean notEqual(Object o1, Object o2) {
        return ! equal(o1, o2);
    }

    private boolean greater(Object o1, Object o2) {
        if (o1 == null || o2 == null)
            new IllegalStateException("Comparison operator is not applicable");

        if (o1 instanceof Number && o2 instanceof Number) {
            return asDouble(o1) > asDouble(o2);
        }

        throw new IllegalExpressionException(this, o1, o2);
    }

    private boolean greaterOrEqual(Object o1, Object o2) {
        if (o1 == null || o2 == null)
            new IllegalStateException("Comparison operator is not applicable");

        if (o1 instanceof Number && o2 instanceof Number) {
            return asDouble(o1) >= asDouble(o2);
        }

        throw new IllegalExpressionException(this, o1, o2);
    }

    private boolean less(Object o1, Object o2) {
        if (o1 == null || o2 == null)
            new IllegalStateException("Comparison operator is not applicable");

        if (o1 instanceof Number && o2 instanceof Number) {
            return asDouble(o1) < asDouble(o2);
        }

        throw new IllegalExpressionException(this, o1, o2);
    }

    private boolean lessOrEqual(Object o1, Object o2) {
        if (o1 == null || o2 == null)
            new IllegalStateException("Comparison operator is not applicable");

        if (o1 instanceof Number && o2 instanceof Number) {
            return asDouble(o1) <= asDouble(o2);
        }

        throw new IllegalExpressionException(this, o1, o2);
    }

    private boolean contains(Object o1, Object o2) {
        if (o1 == null || o2 == null)
            new IllegalStateException("Contains operator is not applicable");

        if (o1 instanceof String && o2 instanceof String) {
            return ((String) o1).contains((String) o2);
        }

        throw new IllegalExpressionException(this, o1, o2);
    }

    private boolean matches(Object o1, Object o2) {
        if (o1 == null || o2 == null)
            new IllegalStateException("Matches operator is not applicable");

        if (o1 instanceof String && o2 instanceof String) {
            return ((String) o1).matches((String) o2);
        }

        throw new IllegalExpressionException(this, o1, o2);
    }

    private Boolean evalBinaryOp(ICell caller, ICell candidate) {
        final Boolean b1 = (Boolean) left.eval(caller, candidate);
        final Boolean b2 = (Boolean) right.eval(caller, candidate);

        return switch (binaryOperator) {
            case AND -> b1 && b2;
            case OR -> b1 || b2;
        };
    }

    private double asDouble(Object o) {
        if (o instanceof Number)
            return ((Number) o).doubleValue();
        else
            throw new IllegalArgumentException("Not a number");
    }

    private Double evalArithmOp(ICell caller, ICell candidate) {
        final Double i1 = asDouble(left.eval(caller, candidate));
        final Double i2 = asDouble(right.eval(caller, candidate));

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

    /**
     * The CompOperator enum represents the different comparison operators.
     */
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

    /**
     * The BinaryOperator enum represents the different binary operators.
     */
    public enum BinaryOperator {
        AND("&&"),
        OR("||");

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

    /**
     * The ArithmOperator enum represents the different arithmetic operators.
     */
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

    /**
     * The StrOperator enum represents the different string operators.
     */
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

