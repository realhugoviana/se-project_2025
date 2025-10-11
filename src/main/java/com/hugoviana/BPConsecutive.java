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

public class BPConsecutive extends BitPacking {

    @Override
    public void compress(int[] array) {
        int max  = -1;
        for (int value : array) {
            if (value > max) {
                max = value;
            }
        }

        this.bitSize = Integer.toBinaryString(max).length();
        
        int compressedLength = this.bitSize / 32 + (this.bitSize % 32 == 0 ? 0 : 1);

        this.compressedArray = new int[compressedLength];

        int compressedArrayIndex = 0;
        int bitCounter = 0;
        for (int value : array) {
            for (int i = 0; i < this.bitSize; i++) {
                this.compressedArray[compressedArrayIndex] |= (1 & (value >> i)) << bitCounter;

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
        // À compléter
    }

    @Override
    public int get(int i) {
        // À compléter
        return 0;
    }

}

