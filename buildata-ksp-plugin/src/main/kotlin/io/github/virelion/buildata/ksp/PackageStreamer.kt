package io.github.virelion.buildata.ksp

import io.github.virelion.buildata.ksp.utils.CodeBuilder
import java.io.File
import java.io.PrintWriter

internal class PackageStreamer(
    private val codegenDir: String
) {
    fun consume(template: GeneratedFileTemplate) {
        val packageDir = codegenDir +
            File.separator +
            template.pkg.getPathFromPackageName()
        File(packageDir).mkdirs()

        val output = File(
            packageDir +
                File.separator +
                template.name + ".kt"
        )
        output.createNewFile()

        val printWriter = PrintWriter(output)
        printWriter.use {
            it.print(template.generateCode(CodeBuilder()))
        }
    }

    internal fun String.getPathFromPackageName(): String {
        return replace('.', File.separatorChar)
    }
}
