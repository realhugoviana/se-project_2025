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

public class BPNonConsecutive extends BitPacking {
    private int bitsPerInteger;

    @Override
    public void compress(int[] array) {
        this.originalLength = array.length;

        int max  = -1;
        for (int value : array) {
            if (value > max) {
                max = value;
            }
        }

        this.bitSize = Integer.toBinaryString(max).length();
        
        this.bitsPerInteger = 32 / this.bitSize;
        int compressedLength = this.originalLength / this.bitsPerInteger + (this.originalLength % this.bitsPerInteger == 0 ? 0 : 1);

        this.compressedArray = new int[compressedLength];

        int compressedArrayIndex = 0;
        int bitCounter = 0;
        for (int value : array) {
            for (int position = 0; position < this.bitSize; position++) {
                this.compressedArray[compressedArrayIndex] |= (1 & (value >> position)) << bitCounter;

                bitCounter++;
                if (bitCounter >= this.bitSize*bitsPerInteger) {
                    bitCounter = 0;
                    compressedArrayIndex++;
                }
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
            for (int position = 0; position < this.bitSize; position++) {
                array[arrayIndex] |= (1 & (this.compressedArray[compressedArrayIndex] >> bitCounter)) << position;

                bitCounter++;
                if (bitCounter >= this.bitSize*this.bitsPerInteger) {
                    bitCounter = 0;
                    compressedArrayIndex++;
                }
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
        int compressedArrayIndex = i / this.bitsPerInteger + (i % this.bitsPerInteger == 0 ? 0 : 1);

        for (int j = 0; j < this.bitSize; j++) {
            result |= (1 & (this.compressedArray[compressedArrayIndex] >> (position))) << j;

            position++;
        }

        return result;
    }

}
