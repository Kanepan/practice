package com.kane.practice.program.logicexecutor.utils;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.Arrays;


public class Indent {

    /**
     * 最大缩进层次
     */
    static final int MAX_INDENT_LEVEL = 64;

    private static final char[] BLANKS = new char[MAX_INDENT_LEVEL << 1];

    private static final char INDENT_CHAR = ' ';

    static {
        Arrays.fill(BLANKS, INDENT_CHAR);
    }

    private static final CharBuffer BLANKS_CHAR_BUFF = CharBuffer.wrap(BLANKS);

    public static <T extends Appendable> T applyTo(T appendable, int indent) throws IOException {
        return applyTo(appendable, indent, 1);
    }

    static StringBuilder applyTo(StringBuilder stringBuilder, int indent) {
        return applyTo(stringBuilder, indent, 1);
    }

    static StringBuffer applyTo(StringBuffer stringBuffer, int indent) {
        return applyTo(stringBuffer, indent, 1);
    }

    static <T extends Appendable> T applyTo(T appendable, int indent, int step) throws IOException {
        appendable.append(BLANKS_CHAR_BUFF, 0, indent << step);
        return appendable;
    }

    static StringBuilder applyTo(StringBuilder stringBuilder, int indent, int step) {
        stringBuilder.append(BLANKS, 0, indent << step);
        return stringBuilder;
    }

    static StringBuffer applyTo(StringBuffer stringBuffer, int indent, int step) {
        stringBuffer.append(BLANKS, 0, indent << step);
        return stringBuffer;
    }
}

