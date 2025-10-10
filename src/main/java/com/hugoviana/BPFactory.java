package com.hugoviana;

public class BPFactory {
    public static BitPacking createBitPacking(String typeCompression) {
        if (typeCompression == null) {
            return null;
        }
        switch (typeCompression.toLowerCase()) {
            case "consecutive":
                return new BPConsecutive();
            case "nonconsecutive":
                return new BPNonConsecutive();
            default:
                throw new IllegalArgumentException("Type de compression inconnu : " + typeCompression);
        }
    }
}
