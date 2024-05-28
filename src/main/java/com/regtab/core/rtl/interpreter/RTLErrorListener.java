package com.regtab.core.rtl.interpreter;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.misc.ParseCancellationException;

/**
 * The `RTLErrorListener` class is a custom error listener for ANTLR parsers.
 * It extends `BaseErrorListener` and overrides the `syntaxError` method to customize
 * the behavior of syntax errors during parsing. When a syntax error is encountered,
 * it throws a `ParseCancellationException`, which effectively cancels the parsing process
 * and allows for error handling at a higher level.
 */
class RTLErrorListener extends BaseErrorListener {
    /**
     * A singleton instance of `RTLErrorListener`.
     */
    static final RTLErrorListener INSTANCE = new RTLErrorListener();

    /**
     * Overrides the `syntaxError` method from `BaseErrorListener` to customize the behavior
     * of syntax errors during parsing. When a syntax error is encountered, it throws a
     * `ParseCancellationException` with a message that includes the line and character
     * position of the error and the error message provided by ANTLR.
     *
     * @param recognizer           The recognizer that encountered the error.
     * @param offendingSymbol      The offending symbol that caused the error.
     * @param line                 The line number where the error occurred.
     * @param charPositionInLine   The character position within the line where the error occurred.
     * @param msg                  The error message provided by ANTLR.
     * @param e                    The recognition exception that caused the error.
     * @throws ParseCancellationException Thrown to cancel the parsing process and allow for error handling at a higher level.
     */
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e)
            throws ParseCancellationException {
        throw new ParseCancellationException("line " + line + ":" + charPositionInLine + " " + msg);
    }
}
