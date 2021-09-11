package io.github.virelion.buildata.path

data class ScalarPathPropertyWrapper<T>(
    override val item: T,
    override val pathRecorder: PathRecorder
): PathPropertyWrapper<T> {
    override fun clone(): ScalarPathPropertyWrapper<T> {
        return ScalarPathPropertyWrapper(item, pathRecorder.clone())
    }

}