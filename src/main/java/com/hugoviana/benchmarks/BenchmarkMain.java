package com.hugoviana.benchmarks;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.RunnerException;

public class BenchmarkMain {
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include("com.hugoviana.benchmarks.*")
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}