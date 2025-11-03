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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        while(true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Entrez un fichier CSV ou une suite de nombres séparés par des virgules : ");
            String input = scanner.nextLine();

            int[] array;

            
            if (new java.io.File(input).exists()) {
                array = lireFichierCSV(input);
            } else {
                array = lireSaisieUtilisateur(input);
            }


            System.out.print("Veuillez entrer votre type de compression 'consecutive', 'nonconsecutive' ou 'overflow' : ");
            String texte = new java.util.Scanner(System.in).nextLine();
            BitPacking bp = BPFactory.createBitPacking(texte);
            bp.compress(array);
            int i = 0;
            while (i != -1) {
                System.out.print("Entrez l'indice de l'élément à récupérer ou -1 pour décompresser : ");
                i = scanner.nextInt();
                if (i == -1) {
                    int[] decompressedArray = new int[array.length];
                    bp.decompress(decompressedArray);
                    System.out.println("Tableau décompressé :");
                    for (int val : decompressedArray) {
                        System.out.print(val + " ");
                    }
                    System.out.println();
                    break;
                } else {
                    try {
                        int value = bp.get(i);
                        System.out.println("Valeur à l'index " + i + " : " + value);
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }

    private static int[] lireSaisieUtilisateur(String input) {
        String[] parts = input.split(",");
        int[] result = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            result[i] = Integer.parseInt(parts[i].trim());
        }
        return result;
    }

    private static int[] lireFichierCSV(String fichier) {
        ArrayList<Integer> liste = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fichier))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] parts = ligne.split(",");
                for (String p : parts) {
                    liste.add(Integer.parseInt(p.trim()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return liste.stream().mapToInt(Integer::intValue).toArray();
    }
}