package io.github.virelion.buildata.path

/**
 * Abstraction for various path element identification.
 */
sealed class PathIdentifier

/**
 * Element accessed as [Int] index.
 *
 * Occurs when accessed element is part of the List.
 * ```kotlin
 * @PathReflection
 * data class Data(val list: List<String>)
 *
 * KClass<Data>.path().list[2]
 * ```
 */
class IntIndexPathIdentifier(val index: Int) : PathIdentifier()

/**
 * Element accessed as [String] index.
 *
 * Occurs when accessed element is part of the Map with String keys.
 * ```kotlin
 * @PathReflection
 * data class Data(val map: Map<String, String>)
 *
 * KClass<Data>.path().map["element"]
 * ```
 */
class StringIndexPathIdentifier(val index: String) : PathIdentifier()

/**
 * Element accessed as [String] property name.
 *
 * Occurs when accessed element is regular class member.
 * ```kotlin
 * @PathReflection
 * data class Data(val str: String)
 *
 * KClass<Data>.path().str
 * ```
 */
class StringNamePathIdentifier(val name: String) : PathIdentifier()
