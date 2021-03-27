# Benchmark
Comparison of constructor invocation versus building using generated code.

## Scenario
Construction of 6 level data class tree with each node containing 10 elements, with string elements in the leafs.

### 1. Regular constructor 
Every class in the tree is constructed using Kotlin constructor. Done purely for comparison.

### 2. Building with no default data classes
Every class in the tree did not have any default values for its content.

### 3. Building with populated parameters in data class with default
Every class in tree has default value for every element. Defaults are not used in this build.

### 4. Building with defaults
Every class in tree has default value for every element. Defaults are used for construction.

## Benchmark project
To see and run benchmark project use `./gradlew benchmark:jmh` in 
[integration-test-project](../integration-test-project) folder.

After running results are present in 
`integration-test-project/benchmark/build/results/jmh/results.txt`.

You can observe already run results in 
[integration-test-project/benchmark/results.txt](../integration-test-project/benchmark/results.txt).

## Results
|Scenario|Score|Error|Unit|
|--------|-----|-----|----|
|1 - regularConstructor|0,013| ± 0,003|s/op|
|2 - buildNoDefaults|0,170|± 0,016|s/op|
|3 - buildWithPopulatedDefaults|0,285|± 0,001|s/op|
|4 - buildWithDefaults|0,095|± 0,005|s/op|
