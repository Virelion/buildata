package io.github.virelion.buildata.access

class MissingPropertyException(val propertyName: String, className: String) :
    RuntimeException("Property '$propertyName' could not be found in $className")
