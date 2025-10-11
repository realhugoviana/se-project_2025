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
public class GetBenchmark {

    private int[] data;
    private BitPacking bpConsecutive;
    private BitPacking bpNonConsecutive;
    private int index;

    @Setup(Level.Invocation)
    public void setup() {
        Random random = new Random(42);
        data = random.ints(100_000, 0, 1_000_000).toArray();

        bpConsecutive = BPFactory.createBitPacking("consecutive");
        bpNonConsecutive = BPFactory.createBitPacking("nonconsecutive");

        int[] copy = Arrays.copyOf(data, data.length);
        index = random.nextInt(data.length);

        bpConsecutive.compress(copy);
        bpNonConsecutive.compress(copy);
    }

    @Benchmark
    public int consecutiveGet() {
        return bpConsecutive.get(index);
    }

    @Benchmark
    public int nonConsecutiveGet() {
        return bpNonConsecutive.get(index);
    }
}