package io.github.virelion.buildata.benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

@Fork(value = 1, warmups = 1)
@State(Scope.Benchmark)
public class TestPerformance {
    private int N = 1;

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void buildWithDefaults(Blackhole bh) {
        for (int i = 0; i < N; i++) {
            bh.consume(Scenarios.INSTANCE.buildWithDefaults());
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void buildNoDefaults(Blackhole bh) {
        for (int i = 0; i < N; i++) {
            bh.consume(Scenarios.INSTANCE.buildNoDefaults());
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void regularConstructor(Blackhole bh) {
        for (int i = 0; i < N; i++) {
            bh.consume(Scenarios.INSTANCE.regularConstructor());
        }
    }
}
