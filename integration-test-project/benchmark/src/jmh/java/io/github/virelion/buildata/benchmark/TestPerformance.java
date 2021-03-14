package io.github.virelion.buildata.benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

@Fork(value = 1, warmups = 1)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
public class TestPerformance {

    @Benchmark
    public void buildWithDefaults(Blackhole bh) {
        bh.consume(Scenarios.INSTANCE.buildWithDefaults());
    }

    @Benchmark
    public void buildWithPopulatedDefaults(Blackhole bh) {
        bh.consume(Scenarios.INSTANCE.buildWithPopulatedDefaults());
    }

    @Benchmark
    public void buildNoDefaults(Blackhole bh) {
        bh.consume(Scenarios.INSTANCE.buildNoDefaults());
    }

    @Benchmark
    public void regularConstructor(Blackhole bh) {
        bh.consume(Scenarios.INSTANCE.regularConstructor());
    }
}
