package io.github.virelion.buildata.benchmark

import kotlin.test.Test

class ScenarioTest {
    @Test
    fun testRegularConstructorScenario() {
        Scenarios.regularConstructor()
    }

    @Test
    fun testNoDefaultsScenario() {
        Scenarios.buildNoDefaults()
    }

    @Test
    fun testDefaultsScenario() {
        Scenarios.buildWithDefaults()
        Scenarios.buildWithPopulatedDefaults()
    }
}
