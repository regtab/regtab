package com.regtab.core.rtl.interpreter;

/**
 * The `Quantifier` record encapsulates the quantification information for a pattern.
 * It has two fields: `times` and `exactly`.
 *
 * - `times` is an enumeration that represents the quantification type. It can be one of:
 *   - `ZERO_OR_ONE`: Zero or one occurrence of the pattern.
 *   - `ZERO_OR_MORE`: Zero or more occurrences of the pattern.
 *   - `ONE_OR_MORE`: One or more occurrences of the pattern.
 *   - `EXACTLY`: Exactly a certain number of occurrences of the pattern.
 *   - `UNDEFINED`: The quantification is not defined.
 *
 * - `exactly` is an integer that holds the exact number of times when the quantification type is `EXACTLY`.
 *   If the quantification type is not `EXACTLY`, this field is `null`.
 */
record Quantifier(Times times, Integer exactly) {
    enum Times {
        ZERO_OR_ONE, ZERO_OR_MORE, ONE_OR_MORE, EXACTLY, UNDEFINED
    }
}