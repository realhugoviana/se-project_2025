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
public class CompressBenchmark {

    private int[] data;

    @Setup(Level.Invocation)
    public void setup() {
        Random random = new Random(42);
        data = random.ints(100_000, 0, 1_000_000).toArray();
    }

    @Benchmark
    public void consecutiveCompression() {
        int[] copy = Arrays.copyOf(data, data.length);
        BitPacking bpConsecutive = BPFactory.createBitPacking("consecutive");
        bpConsecutive.compress(copy);
    }
}