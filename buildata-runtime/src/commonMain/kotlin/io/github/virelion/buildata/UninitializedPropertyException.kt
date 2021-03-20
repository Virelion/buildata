package io.github.virelion.buildata

/**
 * Thrown when required property is not set during building.
 *
 * @param propertyName name of the uninitialized property
 */
class UninitializedPropertyException(val propertyName: String) : IllegalStateException("Property $propertyName was not set during build process.")

internal fun requireInitialized(initialized: Boolean, propertyName: String) {
    if (!initialized) {
        throw UninitializedPropertyException(propertyName)
    }
}
