package org.vlang.ide.codeInsight

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import org.vlang.lang.psi.VlangType
import org.vlang.lang.psi.impl.VlangElementFactory

@Service
class VlangBuiltinTypesUtil(private val project: Project) {
    val int = builtinType("int")
    val i8 = builtinType("i8")
    val i16 = builtinType("i16")
    val i32 = builtinType("i32")
    val i64 = builtinType("i64")
    val isize = builtinType("isize")
    val usize = builtinType("usize")
    val u8 = builtinType("u8")
    val u16 = builtinType("u16")
    val u32 = builtinType("u32")
    val u64 = builtinType("u64")
    val bool = builtinType("bool")
    val byte = builtinType("byte")
    val rune = builtinType("rune")
    val f32 = builtinType("f32")
    val f64 = builtinType("f64")
    val string = builtinType("string")
    val void = builtinType("void")
    val voidptr = builtinType("voidptr")
    val any = builtinType("any")
    val nil = builtinType("nil")
    val iError = builtinType("IError")

    fun get(name: String): VlangType? {
        return when (name) {
            "int"     -> int
            "i8"      -> i8
            "i16"     -> i16
            "i32"     -> i32
            "i64"     -> i64
            "isize"   -> isize
            "usize"   -> usize
            "u8"      -> u8
            "u16"     -> u16
            "u32"     -> u32
            "u64"     -> u64
            "bool"    -> bool
            "byte"    -> byte
            "rune"    -> rune
            "f32"     -> f32
            "f64"     -> f64
            "string"  -> string
            "void"    -> void
            "voidptr" -> voidptr
            "any"     -> any
            "nil"     -> nil
            "IError"  -> iError
            else      -> null
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
