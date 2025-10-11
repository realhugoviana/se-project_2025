package com.hugoviana;

import org.junit.jupiter.api.Test;

public class BPConsecutiveTest {
    
    @Test
    public void shouldPickMaxBitSize() {
        BPConsecutive bp = new BPConsecutive();
        int[] array = {3, 5, 7, 15, 31};
        bp.compress(array);
        System.out.println("Compressed Array: ");
        assert bp.bitSize == Integer.toBinaryString(31).length();
    }

    @Test
    public void shouldAllocateCorrectSize() {
        BPConsecutive bp = new BPConsecutive();
        int[] array = {65536, 65536};
        bp.compress(array);
        int expectedLength = 2;
        assert bp.compressedArray.length == expectedLength;
    }

    @Test
    public void shouldAllocateCorrectSize2() {
        BPConsecutive bp = new BPConsecutive();
        int[] array = {3};
        bp.compress(array);
        int expectedLength = bp.bitSize / 32 + (bp.bitSize % 32 == 0 ? 0 : 1);
        assert bp.compressedArray.length == expectedLength;
    }

    @Test
    public void shouldCompress3As3() {
        BPConsecutive bp = new BPConsecutive();
        int[] array = {3};
        bp.compress(array);
        assert bp.compressedArray[0] == 3;
    }

    @Test
    public void shouldCompress5As5() {
        BPConsecutive bp = new BPConsecutive();
        int[] array = {5};
        bp.compress(array);
        assert bp.compressedArray[0] == 5;
    }

    @Test
    public void shouldCompressAndDecompress() {
        BPConsecutive bp = new BPConsecutive();
        int[] array = {65536, 65536};
        bp.compress(array);
        int[] decompressedArray = new int[array.length];
        bp.decompress(decompressedArray);
        System.out.println("Decompressed Array: "   + decompressedArray[0] + ", " + decompressedArray[1]);
        assert array[0] == decompressedArray[0];
        assert array[1] == decompressedArray[1];
    }
}
