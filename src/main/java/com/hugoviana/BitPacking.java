package com.hugoviana;

public abstract class BitPacking {
    protected int[] compressedData;

    abstract void compress(int[] array);

    abstract void decompress(int[] array);

    abstract int get(int i);
}
