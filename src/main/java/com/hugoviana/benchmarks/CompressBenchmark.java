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
    private BitPacking bpConsecutive;
    private BitPacking bpNonConsecutive;
    private int[] copy;

    @Setup(Level.Invocation)
    public void setup() {
        Random random = new Random(42);
        data = random.ints(100_000, 0, 1_000_000).toArray();

        bpConsecutive = BPFactory.createBitPacking("consecutive");
        bpNonConsecutive = BPFactory.createBitPacking("nonconsecutive");

        copy = Arrays.copyOf(data, data.length);
    }

    @Benchmark
    public void consecutiveCompression() {
        bpConsecutive.compress(copy);
    }

    @Benchmark
    public void nonConsecutiveCompression() {
        bpNonConsecutive.compress(copy);
    }
}