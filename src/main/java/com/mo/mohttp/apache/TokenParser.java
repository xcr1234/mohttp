package com.mo.mohttp.apache;

import java.util.BitSet;

public class TokenParser {

    public static BitSet INIT_BITSET(final int ... b) {
        final BitSet bitset = new BitSet();
        for (final int aB : b) {
            bitset.set(aB);
        }
        return bitset;
    }

    /** US-ASCII CR, carriage return (13) */
    public static final char CR = '\r';

    /** US-ASCII LF, line feed (10) */
    public static final char LF = '\n';

    /** US-ASCII SP, space (32) */
    public static final char SP = ' ';

    /** US-ASCII HT, horizontal-tab (9) */
    public static final char HT = '\t';

    /** Double quote */
    public static final char DQUOTE = '\"';

    /** Backward slash / escape character */
    public static final char ESCAPE = '\\';

    public static boolean isWhitespace(final char ch) {
        return ch == SP || ch == HT || ch == CR || ch == LF;
    }

    public static final TokenParser INSTANCE = new TokenParser();

    /**
     * Extracts from the sequence of chars a token terminated with any of the given delimiters
     * discarding semantically insignificant whitespace characters.
     *
     * @param buf buffer with the sequence of chars to be parsed
     * @param cursor defines the bounds and current position of the buffer
     * @param delimiters set of delimiting characters. Can be {@code null} if the token
     *  is not delimited by any character.
     */
    public String parseToken(final CharArrayBuffer buf, final ParserCursor cursor, final BitSet delimiters) {
        final StringBuilder dst = new StringBuilder();
        boolean whitespace = false;
        while (!cursor.atEnd()) {
            final char current = buf.charAt(cursor.getPos());
            if (delimiters != null && delimiters.get(current)) {
                break;
            } else if (isWhitespace(current)) {
                skipWhiteSpace(buf, cursor);
                whitespace = true;
            } else {
                if (whitespace && dst.length() > 0) {
                    dst.append(' ');
                }
                copyContent(buf, cursor, delimiters, dst);
                whitespace = false;
            }
        }
        return dst.toString();
    }

    /**
     * Extracts from the sequence of chars a value which can be enclosed in quote marks and
     * terminated with any of the given delimiters discarding semantically insignificant
     * whitespace characters.
     *
     * @param buf buffer with the sequence of chars to be parsed
     * @param cursor defines the bounds and current position of the buffer
     * @param delimiters set of delimiting characters. Can be {@code null} if the value
     *  is not delimited by any character.
     */
    public String parseValue(final CharArrayBuffer buf, final ParserCursor cursor, final BitSet delimiters) {
        final StringBuilder dst = new StringBuilder();
        boolean whitespace = false;
        while (!cursor.atEnd()) {
            final char current = buf.charAt(cursor.getPos());
            if (delimiters != null && delimiters.get(current)) {
                break;
            } else if (isWhitespace(current)) {
                skipWhiteSpace(buf, cursor);
                whitespace = true;
            } else if (current == DQUOTE) {
                if (whitespace && dst.length() > 0) {
                    dst.append(' ');
                }
                copyQuotedContent(buf, cursor, dst);
                whitespace = false;
            } else {
                if (whitespace && dst.length() > 0) {
                    dst.append(' ');
                }
                copyUnquotedContent(buf, cursor, delimiters, dst);
                whitespace = false;
            }
        }
        return dst.toString();
    }

    /**
     * Skips semantically insignificant whitespace characters and moves the cursor to the closest
     * non-whitespace character.
     *
     * @param buf buffer with the sequence of chars to be parsed
     * @param cursor defines the bounds and current position of the buffer
     */
    public void skipWhiteSpace(final CharArrayBuffer buf, final ParserCursor cursor) {
        int pos = cursor.getPos();
        final int indexFrom = cursor.getPos();
        final int indexTo = cursor.getUpperBound();
        for (int i = indexFrom; i < indexTo; i++) {
            final char current = buf.charAt(i);
            if (!isWhitespace(current)) {
                break;
            } else {
                pos++;
            }
        }
        cursor.updatePos(pos);
    }

    /**
     * Transfers content into the destination buffer until a whitespace character or any of
     * the given delimiters is encountered.
     *
     * @param buf buffer with the sequence of chars to be parsed
     * @param cursor defines the bounds and current position of the buffer
     * @param delimiters set of delimiting characters. Can be {@code null} if the value
     *  is delimited by a whitespace only.
     * @param dst destination buffer
     */
    public void copyContent(final CharArrayBuffer buf, final ParserCursor cursor, final BitSet delimiters,
                            final StringBuilder dst) {
        int pos = cursor.getPos();
        final int indexFrom = cursor.getPos();
        final int indexTo = cursor.getUpperBound();
        for (int i = indexFrom; i < indexTo; i++) {
            final char current = buf.charAt(i);
            if ((delimiters != null && delimiters.get(current)) || isWhitespace(current)) {
                break;
            } else {
                pos++;
                dst.append(current);
            }
        }
        cursor.updatePos(pos);
    }

    /**
     * Transfers content into the destination buffer until a whitespace character,  a quote,
     * or any of the given delimiters is encountered.
     *
     * @param buf buffer with the sequence of chars to be parsed
     * @param cursor defines the bounds and current position of the buffer
     * @param delimiters set of delimiting characters. Can be {@code null} if the value
     *  is delimited by a whitespace or a quote only.
     * @param dst destination buffer
     */
    public void copyUnquotedContent(final CharArrayBuffer buf, final ParserCursor cursor,
                                    final BitSet delimiters, final StringBuilder dst) {
        int pos = cursor.getPos();
        final int indexFrom = cursor.getPos();
        final int indexTo = cursor.getUpperBound();
        for (int i = indexFrom; i < indexTo; i++) {
            final char current = buf.charAt(i);
            if ((delimiters != null && delimiters.get(current))
                    || isWhitespace(current) || current == DQUOTE) {
                break;
            } else {
                pos++;
                dst.append(current);
            }
        }
        cursor.updatePos(pos);
    }

    /**
     * Transfers content enclosed with quote marks into the destination buffer.
     *
     * @param buf buffer with the sequence of chars to be parsed
     * @param cursor defines the bounds and current position of the buffer
     * @param dst destination buffer
     */
    public void copyQuotedContent(final CharArrayBuffer buf, final ParserCursor cursor,
                                  final StringBuilder dst) {
        if (cursor.atEnd()) {
            return;
        }
        int pos = cursor.getPos();
        int indexFrom = cursor.getPos();
        final int indexTo = cursor.getUpperBound();
        char current = buf.charAt(pos);
        if (current != DQUOTE) {
            return;
        }
        pos++;
        indexFrom++;
        boolean escaped = false;
        for (int i = indexFrom; i < indexTo; i++, pos++) {
            current = buf.charAt(i);
            if (escaped) {
                if (current != DQUOTE && current != ESCAPE) {
                    dst.append(ESCAPE);
                }
                dst.append(current);
                escaped = false;
            } else {
                if (current == DQUOTE) {
                    pos++;
                    break;
                }
                if (current == ESCAPE) {
                    escaped = true;
                } else if (current != CR && current != LF) {
                    dst.append(current);
                }
            }
        }
        cursor.updatePos(pos);
    }

}