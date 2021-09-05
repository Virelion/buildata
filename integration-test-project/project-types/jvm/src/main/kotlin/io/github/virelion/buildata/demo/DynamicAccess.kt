package io.github.virelion.buildata.demo

import io.github.virelion.buildata.access.DynamicallyAccessible

@DynamicallyAccessible
class DynamicAccessRoot(
    val map: Map<String, String>,
    val list: List<String>,
    val element: DynamicAccessInner1,
    val nullable_element: DynamicAccessInner1?
)

@DynamicallyAccessible
class DynamicAccessInner1(
    val map: Map<String, String>,
    val list: List<String>,
)
