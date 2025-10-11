package com.hugoviana;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class BPNonConsecutiveTest {
    
    @Test
    public void shouldPickMaxBitSize() {
        BitPacking bp = BPFactory.createBitPacking("nonconsecutive");
        int[] array = {3, 5, 7, 15, 31};
        bp.compress(array);
        System.out.println("Compressed Array: ");
        assert bp.bitSize == Integer.toBinaryString(31).length();
    }

    @Test
    public void shouldAllocateCorrectSize() {
        BitPacking bp = BPFactory.createBitPacking("nonconsecutive");
        int[] array = {65536, 65536};
        bp.compress(array);
        int expectedLength = 2;
        assert bp.compressedArray.length == expectedLength;
    }

    @Test
    public void shouldAllocateCorrectSize2() {
        BitPacking bp = BPFactory.createBitPacking("nonconsecutive");
        int[] array = {3};
        bp.compress(array);
        int expectedLength = bp.bitSize / 32 + (bp.bitSize % 32 == 0 ? 0 : 1);
        assert bp.compressedArray.length == expectedLength;
    }

    @Test
    public void shouldCompress3As3() {
        BitPacking bp = BPFactory.createBitPacking("nonconsecutive");
        int[] array = {3};
        bp.compress(array);
        assert bp.compressedArray[0] == 3;
    }

    @Test
    public void shouldCompress5As5() {
        BitPacking bp = BPFactory.createBitPacking("nonconsecutive");
        int[] array = {5};
        bp.compress(array);
        assert bp.compressedArray[0] == 5;
    }

    @Test
    public void shouldCompressAndDecompress() {
        BitPacking bp = BPFactory.createBitPacking("nonconsecutive");
        int[] array = {65536, 65536};
        bp.compress(array);
        int[] decompressedArray = new int[array.length];
        bp.decompress(decompressedArray);
        assertEquals(array[0], decompressedArray[0]);
        assertEquals(array[1], decompressedArray[1]);
    }

    @Test
    public void shouldCompressAndDecompress2() {
        BitPacking bp = BPFactory.createBitPacking("nonconsecutive");
        int[] array = {3, 5, 7, 15, 31, 65536, 20, 1000, 50000, 65535, 12345, 54321, 32768, 16384, 8192, 4096};
        bp.compress(array);
        int[] decompressedArray = new int[array.length];
        bp.decompress(decompressedArray);
        for (int i = 0; i < array.length; i++) {
            assertEquals(array[i], decompressedArray[i]);  
        }
    }

    @Test
    public void shouldThrowExceptionOnOutOfBoundsAccess() {
        BitPacking bp = BPFactory.createBitPacking("nonconsecutive");
        int[] array = {3, 5, 7, 15, 31};
        bp.compress(array);
        try {
            bp.get(10);
            assert false;
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }
    }

    @Test
    public void shouldReturnCorrectValueOnGet() {
        BitPacking bp = BPFactory.createBitPacking("nonconsecutive");
        int[] array = {3, 5, 7, 15, 31, 65536, 20, 1000, 50000, 65535, 12345, 54321, 32768, 16384, 8192, 4096};
        bp.compress(array);
        for (int i = 0; i < array.length; i++) {
            assertEquals(array[i], bp.get(i));
        }
    }

}
