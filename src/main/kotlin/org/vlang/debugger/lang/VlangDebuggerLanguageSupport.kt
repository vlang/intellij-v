package org.vlang.debugger.lang

import com.intellij.execution.configurations.RunProfile
import com.intellij.openapi.util.SystemInfo
import com.intellij.ui.ColoredText
import com.intellij.ui.SimpleTextAttributes
import com.intellij.xdebugger.evaluation.XDebuggerEditorsProvider
import com.jetbrains.cidr.execution.debugger.*
import com.jetbrains.cidr.execution.debugger.backend.DebuggerDriver
import com.jetbrains.cidr.execution.debugger.evaluation.CidrDebuggerTypesHelperBase
import com.jetbrains.cidr.execution.debugger.evaluation.CidrPhysicalValue
import org.vlang.debugger.renderers.VlangCTypeParser

class VlangDebuggerLanguageSupport : CidrDebuggerLanguageSupport() {
    override fun getSupportedDebuggerLanguages() = setOf(DebuggerDriver.StandardDebuggerLanguage.C)

    override fun createEditor(profile: RunProfile?): XDebuggerEditorsProvider {
        return createEditorProvider()
    }

    override fun createFrameTypeDecorator(frame: CidrStackFrame): CidrFrameTypeDecorator {
        return object : CidrFrameTypeDecorator {
            override fun getFrameFunctionColoredText(
                frame: CidrStackFrame,
                attributes: SimpleTextAttributes,
                renderForUiLabel: Boolean,
            ): ColoredText {
                val rawName = frame.frame.function
                    .replace(GENERIC_TYPE_IN_CALLSTACK_REGEX, "[$1]")

                val parts = rawName.split("__")
                val fixedName = parts.joinToString(".") {
                    if (it.isEmpty()) {
                        return@joinToString ""
                    }

                    val firstCharIsUpperCase = it.isNotEmpty() && it.first().isUpperCase()
                    var res = it
                    if (firstCharIsUpperCase) {
                        val indexOfUnderscore = it.indexOf('_')
                        if (indexOfUnderscore != -1) {
                            res = it.replaceFirst("_", ".")
                        }
                    }
                    res
                }

                val builder = ColoredText.builder()
                    .append(fixedName, attributes)

                return builder.build()
            }

            private fun convertPrimitive(type: String): String? {
                if (!IS_WINDOWS) {
                    return null
                }

                return PRIMITIVE_TYPES_MAP[type]
            }

            private fun convertType(type: String): String {
                val primitive = convertPrimitive(type)
                if (primitive != null) {
                    return primitive
                }

                if (type == "void") {
                    return ""
                }

                // main__Foo (*)(int)
                if (type.contains("(*)")) {
                    val parts = type.split("(*)").map { it.trim() }
                    val returnType = convertType(parts[0])
                    val argTypes = parts[1]
                        .removeSurrounding("(", ")")
                        .split(",")
                        .joinToString(", ") { convertType(it.trim()) }

                    val base = if (argTypes == "...") "fn ()" else "fn ($argTypes)"
                    return if (returnType.isNotEmpty()) "$base $returnType" else base
                }

                if (type.startsWith("chan_")) {
                    val chanType = type.substring(5)
                    return "chan " + convertType(chanType)
                }

                if (type.startsWith("_option_")) {
                    val optionType = VlangCTypeParser.parseOptionType(type)
                    return "?" + convertType(optionType)
                }

                if (type.startsWith("Map_")) {
                    val (key, value) = VlangCTypeParser.parseMapType(type)
                    return "map[" + convertType(key) + "]" + convertType(value)
                }

                if (type.startsWith("Array_fixed_")) {
                    val parts = type.split('_', limit = 3)
                    val size = parts[2]
                    val arrayType = parts[3]
                    return "[$size]" + convertType(arrayType)
                }

                if (type.startsWith("Array_")) {
                    val arrayType = type.substringAfter("Array_")
                    return "[]" + convertType(arrayType)
                }

                if (type.endsWith("_ptr")) {
                    val elementType = type.removeSuffix("_ptr")
                    return "&" + convertType(elementType)
                }

                if (type.startsWith("anon_fn_")) {
                   return VlangCTypeParser.parseAnonFnType(type)
                }
               
                if (type.contains("__")) {
                    return convertType(type.replace("__", "."))
                }

                val generics = GENERIC_TYPE_REGEX.findAll(type)
                if (generics.count() == 1) {
                    val generic = generics.first()
                    val genericType = generic.groupValues[1].replace("_", ", ")
                    return type.replaceRange(generic.range, "[${convertType(genericType)}]")
                }

                return type
            }

            override fun getValueDisplayType(value: CidrPhysicalValue, renderForUiLabel: Boolean): String {
                val type = value.type
                val converted = convertType(type)
                if (converted != type) {
                    return converted
                }

                return super.getValueDisplayType(value, renderForUiLabel)
            }

            val IS_WINDOWS = SystemInfo.isWindows
            val PRIMITIVE_TYPES_MAP = mapOf(
                "double" to "f64",
                "float" to "f32",
                "int" to "int",
                "long" to "i64",
                "long long" to "i64",
                "unsigned int" to "u32",
                "unsigned long" to "u64",
                "unsigned long long" to "u64",
                "char" to "i8",
                "unsigned char" to "u8",
                "short" to "i16",
                "unsigned short" to "u16",
            )
        }
    }

    override fun createDebuggerTypesHelper(process: CidrDebugProcess): CidrDebuggerTypesHelperBase =
        VlangDebuggerTypesHelper(process)

    override fun createEvaluator(frame: CidrStackFrame): CidrEvaluator =
        VlangEvaluator(frame)

    companion object {
        private val GENERIC_TYPE_REGEX = "_[A-Z]_(\\w+)".toRegex()
        private val GENERIC_TYPE_IN_CALLSTACK_REGEX = "_([A-Z])_".toRegex()
    }
}
