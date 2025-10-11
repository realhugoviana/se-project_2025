package com.hugoviana.benchmarks;

import org.openjdk.jmh.annotations.*;

import com.hugoviana.*;

import java.util.concurrent.TimeUnit;
import java.util.Arrays;
import java.util.Random;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@Fork(1)
@State(Scope.Thread)
public class DecompressBenchmark {

    private int[] data;
    private BitPacking bpConsecutive;

    @Setup(Level.Invocation)
    public void setup() {
        Random random = new Random(42);
        data = random.ints(100_000, 0, 1_000_000).toArray();
        bpConsecutive = BPFactory.createBitPacking("consecutive");
        int[] copy = Arrays.copyOf(data, data.length);
        bpConsecutive.compress(copy);
    }

    @Benchmark
    public void consecutiveDecompression() {
        bpConsecutive.decompress(new int[data.length]);
    }
}