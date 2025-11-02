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
    private int overflowBitSize;
    private int overflowNum;

    @Override
    public void compress(int[] array) {
        if (array.length < 1) {
            throw new IllegalArgumentException("Array is empty.");
        }

        this.originalLength = array.length;

        int[] effInts = new int[31]; 
        int[] effCumulInts = new int[32];
        int maxLength = 0;

        for (int value : array) {
            int length = Integer.toBinaryString(value).length();

            if (length > maxLength) {
                maxLength = length;
            }

            effInts[length-1]++;

            while (length > 0) {
                effCumulInts[length-1]++;
                length--;
            }
        }

        int compressedLength = Integer.MAX_VALUE;
        this.overflowNum = 0;
        for (int i = 1; i <= maxLength; i++) {
            if (effInts[i-1] != 0 && Integer.toBinaryString(effCumulInts[i]).length() <= i) {
                int totalBits = ((this.originalLength * (i+1)) + (effCumulInts[i] * maxLength));
                int currentCompressedLength = totalBits / 32 + (totalBits % 32 == 0 ? 0 : 1);

                if (currentCompressedLength < compressedLength) {
                    compressedLength = currentCompressedLength;
                    this.bitSize = i+1;
                    this.overflowBitSize = maxLength;
                    this.overflowNum = effCumulInts[i];
                }
            }
        }

        this.compressedArray = new int[compressedLength];
        int[] toOverflow = new int[this.overflowNum];

        int overflowIndex = 0;
        int compressedArrayIndex = 0;
        int bitCounter = 0;
        for (int value : array) {
            for (int position = 0; position < this.bitSize; position++) {
                if (Integer.toBinaryString(value).length() > this.bitSize-1) {
                    this.compressedArray[compressedArrayIndex] |= (1 & ((overflowIndex + (1 << (this.bitSize - 1))) >> position)) << bitCounter;
                    toOverflow[overflowIndex] = value;
                }
                else {
                    this.compressedArray[compressedArrayIndex] |= (1 & (value >> position)) << bitCounter;
                }

                bitCounter++;
                if (bitCounter >= 32) {
                    bitCounter = 0;
                    compressedArrayIndex++;
                }
            }
            if (Integer.toBinaryString(value).length() > this.bitSize-1) {
                overflowIndex++;
            }
        }

        for (int overflowValue : toOverflow) {
            for (int position = 0; position < this.overflowBitSize; position++) {
                this.compressedArray[compressedArrayIndex] |= (1 & (overflowValue >> position)) << bitCounter;

                bitCounter++;
                if (bitCounter >= 32) {
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

        int[] fromOverflow = new int[this.overflowNum];
        int overflowIndex = 0;
        int compressedArrayIndex = 0;
        int bitCounter = 0;
        for (int arrayIndex = 0; arrayIndex < this.originalLength; arrayIndex++) {
            for (int position = 0; position < this.bitSize; position++) {
                array[arrayIndex] |= (1 & (this.compressedArray[compressedArrayIndex] >> bitCounter)) << position;

                bitCounter++;
                if (bitCounter >= 32) {
                    bitCounter = 0;
                    compressedArrayIndex++;
                }
            }
            if (array[arrayIndex] >> (this.bitSize-1) == 1 && this.bitSize != 32) {
                fromOverflow[overflowIndex] = arrayIndex;
                overflowIndex++;
            }
        }

        for (int index = 0; index < this.overflowNum; index++) {
            array[fromOverflow[index]] = 0;
            for (int position = 0; position < this.overflowBitSize; position++) {
                array[fromOverflow[index]] |= (1 & (this.compressedArray[compressedArrayIndex] >> bitCounter)) << position;

                bitCounter++;
                if (bitCounter >= 32) {
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
        int overallPosition = i*this.bitSize;
        int localPosition = overallPosition % 32;
        int compressedArrayIndex = overallPosition / 32;

        for (int j = 0; j < this.bitSize; j++) {
            if (localPosition > 31) {
                localPosition = 0;
                compressedArrayIndex++;
            }

            result |= (1 & (this.compressedArray[compressedArrayIndex] >> (localPosition))) << j;

            localPosition++;
        }

        if (result >> (this.bitSize-1) == 1 && this.bitSize != 32) {
            result -= 1 << (this.bitSize-1);
            overallPosition = this.originalLength * this.bitSize + result * this.overflowBitSize;
            localPosition = overallPosition % 32;
            compressedArrayIndex = overallPosition / 32;

            result = 0;
            for (int j = 0; j < this.overflowBitSize; j++) {
                if (localPosition > 31) {
                    localPosition = 0;
                    compressedArrayIndex++;
                }

                result |= (1 & (this.compressedArray[compressedArrayIndex] >> localPosition)) << j;

                localPosition++;
            }
        }

        return result;
    }

}
