/*
 * Copyright 2025 Hugo Viana
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hugoviana;

public class BPOverflow extends BitPacking {
    private int bitsPerInteger;
    private int overflowIndex;

    @Override
    public void compress(int[] array) {
        if (array.length < 1) {
            throw new IllegalArgumentException("Array is empty.");
        }

        this.originalLength = array.length;

        int[] effInts = new int[32]; 

        for (int value : array) {
            effInts[Integer.toBinaryString(value).length() - 1]++;
        }

        int compressedLength = Integer.MAX_VALUE;
        for (int i = 1; i <= 32; i++) {
            if (effInts[i - 1] != 0) {
                if (i >= 31 && 32*this.originalLength < compressedLength) {
                    compressedLength = 32*this.originalLength;
                    this.bitSize = 32;
                    this.bitsPerInteger = 1;

                }
                else {
                    int currentNumOverflow = 0;

                    for (int j = i + 1; j <= 32; j++) {
                        currentNumOverflow += effInts[j - 1];
                    }

                    int currentBitSize = (i > Integer.toBinaryString(currentNumOverflow).length())? i + 1 : Integer.toBinaryString(currentNumOverflow).length() + 1;
                    int curentBitsPerInteger = 32/(currentBitSize);
                    int currentCompressedLength = (this.originalLength / curentBitsPerInteger) + (this.originalLength % curentBitsPerInteger == 0 ? 0 : 1) + currentNumOverflow;

                    if (currentCompressedLength < compressedLength) {
                        compressedLength = currentCompressedLength;
                        this.bitSize = currentBitSize;
                        this.bitsPerInteger = curentBitsPerInteger;
                    }
                }
            }
        }

        this.compressedArray = new int[compressedLength];
        this.overflowIndex = (this.originalLength / this.bitsPerInteger) + (this.originalLength % this.bitsPerInteger == 0 ? 0 : 1);

        int overflowCounter = 0;
        int compressedArrayIndex = 0;
        int bitCounter = 0;
        for (int value : array) {
            for (int position = 0; position < this.bitSize; position++) {
                if (Integer.toBinaryString(value).length() > this.bitSize - 1) {
                    this.compressedArray[compressedArrayIndex] |= (1 & ((overflowCounter + (1 << (this.bitSize - 1))) >> position)) << bitCounter;
                }
                else {
                    this.compressedArray[compressedArrayIndex] |= (1 & (value >> position)) << bitCounter;
                }

                bitCounter++;
                if (bitCounter >= this.bitSize*bitsPerInteger) {
                    bitCounter = 0;
                    compressedArrayIndex++;
                }
            }

            if (Integer.toBinaryString(value).length() > this.bitSize - 1) {
                this.compressedArray[overflowIndex + overflowCounter] = value;
                overflowCounter++;
            }
        }
    }

    @Override
    public void decompress(int[] array) {
        if (array.length != this.originalLength) {
            throw new IllegalArgumentException("Array length does not match original length.");
        }

        int compressedArrayIndex = 0;
        int bitCounter = 0;
        for (int arrayIndex = 0; arrayIndex < this.originalLength; arrayIndex++) {
            int bitPackedValue = 0;
            for (int position = 0; position < this.bitSize; position++) {
                bitPackedValue|= (1 & (this.compressedArray[compressedArrayIndex] >> bitCounter)) << position;

                bitCounter++;
                if (bitCounter >= this.bitSize*this.bitsPerInteger) {
                    bitCounter = 0;
                    compressedArrayIndex++;
                }
            }

            if (bitPackedValue >> (this.bitSize - 1) == 1 && this.bitSize != 32) {
                array[arrayIndex] = this.compressedArray[this.overflowIndex + (bitPackedValue - (1 << (this.bitSize - 1)))];
            }
            else {
                array[arrayIndex] = bitPackedValue;
            }
        }
    }

    @Override
    public int get(int i) {
        if (i < 0 || i >= this.originalLength) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + i);
        }

        int result = 0;
        int position = (i % this.bitsPerInteger) * this.bitSize;
        int compressedArrayIndex = i / this.bitsPerInteger;

        for (int j = 0; j < this.bitSize; j++) {
            result |= (1 & (this.compressedArray[compressedArrayIndex] >> (position))) << j;

            position++;
        }

        if (result >> (this.bitSize - 1) == 1 && this.bitSize != 32) {
            result = this.compressedArray[this.overflowIndex + (result - (1 << (this.bitSize - 1)))];
        }

        return result;
    }

}
