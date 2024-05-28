package com.regtab.core.model.format;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The HtmlTag class represents an HTML tag. It provides static instances for 'th' and 'td' tags.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class HtmlTag {
    private final boolean th;
    private final boolean td;

    /**
     * Represents the 'th' HTML tag.
     */
    public static final HtmlTag TH;

    /**
     * Represents the 'td' HTML tag.
     */
    public static final HtmlTag TD;

    static {
        TH = new HtmlTag(true, false);
        TD = new HtmlTag(false, true);
    }
}
