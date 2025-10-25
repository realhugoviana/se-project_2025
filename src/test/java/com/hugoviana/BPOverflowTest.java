package com.hugoviana;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class BPOverflowTest {

    @Test
    public void shouldCompressAndDecompress() {
        BitPacking bp = BPFactory.createBitPacking("overflow");
        int[] array = {65536, 65536};
        bp.compress(array);
        int[] decompressedArray = new int[array.length];
        bp.decompress(decompressedArray);
        assertEquals(array[0], decompressedArray[0]);
        assertEquals(array[1], decompressedArray[1]);
    }

    @Test
    public void shouldCompressAndDecompress2() {
        BitPacking bp = BPFactory.createBitPacking("overflow");
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
        BitPacking bp = BPFactory.createBitPacking("overflow");
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
        BitPacking bp = BPFactory.createBitPacking("overflow");
        int[] array = {3, 5, 7, 15, 31, 65536, 20, 1000, 50000, 65535, 12345, 54321, 32768, 16384, 8192, 4096};
        bp.compress(array);
        for (int i = 0; i < array.length; i++) {
            assertEquals(array[i], bp.get(i));
        }
    }

}
