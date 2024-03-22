package com.regtab.core.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class HtmlTag {
    private final boolean th;
    private final boolean td;

    public static final HtmlTag TH;
    public static final HtmlTag TD;

    static {
        TH = new HtmlTag(true, false);
        TD = new HtmlTag(false, true);
    }
}
