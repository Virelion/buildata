package io.github.virelion.buildata.benchmark

object Scenarios {
    fun regularConstructor(): Root {
        return root()
    }

    fun buildNoDefaults(): Root {
        return Root::class.build { fill() }
    }

    fun buildWithDefaults(): RootWithDefaults {
        return RootWithDefaults::class.build { }
    }

    fun buildWithPopulatedDefaults(): RootWithDefaults {
        return RootWithDefaults::class.build { fill() }
    }
}
