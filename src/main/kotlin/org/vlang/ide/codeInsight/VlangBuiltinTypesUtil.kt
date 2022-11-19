package org.vlang.ide.codeInsight

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import org.vlang.lang.psi.VlangType
import org.vlang.lang.psi.impl.VlangElementFactory

@Service
class VlangBuiltinTypesUtil(private val project: Project) {
    val int = builtinType("int")
    val bool = builtinType("bool")
    val rune = builtinType("rune")
    val f32 = builtinType("f32")
    val f64 = builtinType("f64")
    val string = builtinType("string")
    val void = builtinType("void")
    val any = builtinType("any")
    val nil = builtinType("nil")
    val iError = builtinType("IError")

    fun get(name: String): VlangType? {
        return when (name) {
            "int"    -> int
            "bool"   -> bool
            "rune"   -> rune
            "f32"    -> f32
            "f64"    -> f64
            "string" -> string
            "void"   -> void
            "any"    -> any
            "nil"    -> nil
            "IError" -> iError
            else     -> null
        }
    }

    private fun builtinType(name: String): VlangType {
        val file = VlangElementFactory.createFileFromText(
            project, """
            module builtin
            fn f(a $name) {}
        """.trimIndent()
        )

        return file.getFunctions().first().getSignature()!!.parameters.paramDefinitionList.first().type
    }

    companion object {
        fun getInstance(project: Project) = project.service<VlangBuiltinTypesUtil>()
    }
}
