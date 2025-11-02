package com.hugoviana;

import java.io.*;
import java.util.Random;

public class CompressionTime {
    private Random random;
    private int _runs;
    private int _maxSize;
    private int _intMax;

    private int[] data;
    private BitPacking bpConsecutive;
    private BitPacking bpNonConsecutive;
    private BitPacking bpOverflow;

    public CompressionTime(int seed, int runs, int maxSize, int intMax) throws IOException {
        this.random = new Random(seed);
        this._runs = runs;
        this._maxSize = maxSize;
        this._intMax = intMax;

        this.bpConsecutive = BPFactory.createBitPacking("consecutive");
        this.bpNonConsecutive = BPFactory.createBitPacking("nonconsecutive");
        this.bpOverflow = BPFactory.createBitPacking("overflow");

        String[] header = {"Run", 
                           "Original size",  
                           "Min",
                           "Max",
                           "Sum",
                           "Consecutive Compression (ns)", 
                           "Consecutive Size",
                           "Consecutive Decompression (ns)",
                           "Consective Total Time (ns)",
                           "Consecutive Get (ns)",
                           "Non-Consecutive Compression (ns)", 
                           "Non-Consecutive Size",
                           "Non-Consecutive Decompression (ns)",
                           "Non-Consecutive Total Time (ns)",
                           "Non-Consecutive Get (ns)",
                           "Overflow Compression (ns)",
                           "Overflow Size",
                           "Overflow Decompression (ns)",
                            "Overflow Total Time (ns)",
                           "Overflow Get (ns)"};
        String filePath = "compression_times.csv";

        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath, true))) {
            for (int i = 0; i < header.length; i++) {
                pw.print(header[i]);
                if (i < header.length - 1) {
                    pw.print(",");
                }
            }
            pw.println();
        }
    }

    private void ajouteAuCSV(long[] run) throws IOException {

        String filePath = "compression_times.csv";

        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath, true))) {
            for (int i = 0; i < run.length; i++) {
                pw.print(run[i]);
                if (i < run.length - 1) {
                    pw.print(",");
                }
            }
            pw.println();
        }

    }

    public void run() throws IOException {
        for (int i = 0; i < this._runs; i++) {
            int arraySize = this.random.nextInt(this._maxSize) + 1;
            data = random.ints(arraySize, 0, this._intMax).toArray();

            long[] thisRun = new long[20];
            thisRun[0] = i;
            thisRun[1] = arraySize;
            long startTime, endTime;
            int min = Integer.MAX_VALUE;
            int max = 0;
            long sum = 0;
            for(int value : data) {
                if (value < min) min = value;
                if (value > max) max = value;
                sum += value; 
            }
            thisRun[2] = min;
            thisRun[3] = max;
            thisRun[4] = sum;

            startTime = System.nanoTime();
            bpConsecutive.compress(data);
            endTime = System.nanoTime();

            thisRun[5] = endTime - startTime;

            thisRun[6] = bpConsecutive.getCompressedArray().length;

            startTime = System.nanoTime();
            bpConsecutive.decompress(new int[arraySize]);
            endTime = System.nanoTime();

            thisRun[7] = endTime - startTime;
            thisRun[8] = thisRun[5] + thisRun[7];

            startTime = System.nanoTime();
            bpConsecutive.get(this.random.nextInt(arraySize));
            endTime = System.nanoTime();

            thisRun[9] = endTime - startTime;

            startTime = System.nanoTime();
            bpNonConsecutive.compress(data);
            endTime = System.nanoTime();

            thisRun[10] = endTime - startTime;

            thisRun[11] = bpNonConsecutive.getCompressedArray().length;

            startTime = System.nanoTime();
            bpNonConsecutive.decompress(new int[arraySize]);
            endTime = System.nanoTime();

            thisRun[12] = endTime - startTime;
            thisRun[13] = thisRun[10] + thisRun[12];

            startTime = System.nanoTime();
            bpNonConsecutive.get(this.random.nextInt(arraySize));
            endTime = System.nanoTime();

            thisRun[14] = endTime - startTime;

            startTime = System.nanoTime();
            bpOverflow.compress(data);
            endTime = System.nanoTime();

            thisRun[15] = endTime - startTime;

            thisRun[16] = bpOverflow.getCompressedArray().length;

            startTime = System.nanoTime();
            bpOverflow.decompress(new int[arraySize]);
            endTime = System.nanoTime();

            thisRun[17] = endTime - startTime;
            thisRun[18] = thisRun[15] + thisRun[17];

            startTime = System.nanoTime();
            bpOverflow.get(this.random.nextInt(arraySize));
            endTime = System.nanoTime();

            thisRun[19] = endTime - startTime;

            ajouteAuCSV(thisRun);
        }
    }

}
