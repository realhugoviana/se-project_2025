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
            case "overflow":
                return new BPOverflow();
            default:
                throw new IllegalArgumentException("Type de compression inconnu : " + typeCompression);
        }
    }
}
