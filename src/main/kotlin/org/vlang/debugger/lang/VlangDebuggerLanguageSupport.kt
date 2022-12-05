package org.vlang.debugger.lang

import com.intellij.execution.configurations.RunProfile
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
                val functionName = frame.frame.function
                    .replace("__", ".")
                    .replace("_T_", "<T>")

                val builder = ColoredText.builder()
                    .append(functionName, attributes)

                return builder.build()
            }

            private fun convertType(type: String): String {
                // main__Foo (*)(int)
                if (type.contains("(*)")) {
                    val parts = type.split("(*)").map { it.trim() }
                    val returnType = convertType(parts[0])
                    val argTypes = parts[1]
                        .removeSurrounding("(", ")")
                        .split(",")
                        .joinToString(", ") { convertType(it.trim()) }

                    return "fn ($argTypes) $returnType"
                }

                if (type.startsWith("chan_")) {
                    val chanType = type.substring(5)
                    return "chan " + convertType(chanType)
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
        }
    }

    override fun createDebuggerTypesHelper(process: CidrDebugProcess): CidrDebuggerTypesHelperBase =
        VlangDebuggerTypesHelper(process)

    override fun createEvaluator(frame: CidrStackFrame): CidrEvaluator =
        VlangEvaluator(frame)

    companion object {
        private val GENERIC_TYPE_REGEX = "_[A-Z]_(\\w+)".toRegex()
    }
}
