package com.regtab.core.model;

public final class CColor {
    private final int r;
    private final int g;
    private final int b;

    public CColor(String hexRGB) {
        super();

        String hexR = String.format("#%s", hexRGB.substring(0, 2));
        String hexG = String.format("#%s", hexRGB.substring(2, 4));
        String hexB = String.format("#%s", hexRGB.substring(4, 6));

        this.r = Integer.decode(hexR);
        this.g = Integer.decode(hexG);
        this.b = Integer.decode(hexB);
    }

    public CColor(byte[] rgbWithHint) {
        super();

        if (rgbWithHint[0] >= 0) {
            this.r = rgbWithHint[0];
        } else {
            this.r = 256 + rgbWithHint[0];
        }
        if (rgbWithHint[1] >= 0) {
            this.g = rgbWithHint[1];
        } else {
            this.g = 256 + rgbWithHint[1];
        }
        if (rgbWithHint[2] >= 0) {
            this.b = rgbWithHint[2];
        } else {
            this.b = 256 + rgbWithHint[2];
        }
    }

    String toHexRGBString() {
        String hexR = Integer.toHexString(r);
        String hexG = Integer.toHexString(g);
        String hexB = Integer.toHexString(b);

        return String.format("#%s%s%s", hexR, hexG, hexB);
    }

    public String toString() {
        return toHexRGBString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + b;
        result = prime * result + g;
        result = prime * result + r;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        CColor other = (CColor) obj;

        if (b != other.b) return false;
        if (g != other.g) return false;
        if (r != other.r) return false;

        return true;
    }
}