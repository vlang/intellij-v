package org.vlang.debugger.v2c

import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.lang.psi.*
import org.vlang.lang.psi.types.*

class VlangExpressionEvaluator : VlangVisitor() {
    private var toPredefine = false
    private val predefined: StringBuilder = StringBuilder()
    private val result: StringBuilder = StringBuilder()
    private var tmpVarCounter = 0

    fun text(): String {
        return """
                    ({
                        $predefined
                        $result;
                    })
                    """.trimIndent()
    }

    override fun visitReferenceExpression(expr: VlangReferenceExpression) {
        val qualifier = expr.getQualifier() as? VlangTypeOwner

        if (qualifier != null) {
            append("(")
            qualifier.accept(this)
            append(")")

            val resolvedQualifier = if (qualifier is VlangReferenceExpression) qualifier.resolve() else null

            val qualifierType = qualifier.getType(null)

            val isPointer = qualifierType is VlangPointerTypeEx ||
                    // mutable parameters and receivers are always pointers
                    (resolvedQualifier is VlangParamDefinition && resolvedQualifier.isMutable()) ||
                    (resolvedQualifier is VlangReceiver && resolvedQualifier.isMutable())

            if (isPointer) {
                append("->")
            } else {
                append(".")
            }
        }

        val resolved = expr.resolve() as? VlangNamedElement
        if (resolved == null) {
            append(expr.text)
            throw IllegalArgumentException("Cannot resolve reference expression: ${expr.text}")
        }

        if (resolved is VlangVarDefinition || resolved is VlangParamDefinition || resolved is VlangReceiver || resolved is VlangFieldDefinition) {
            append(resolved.name!!)
            return
        }

        val fqn = resolved.getQualifiedName()?.toCname(resolved is VlangMethodDeclaration) ?: resolved.name
        if (fqn != null) {
            append(fqn)
        }
    }

    override fun visitCallExpr(call: VlangCallExpr) {
        val called = call.resolve()
        val caller = call.expression as? VlangReferenceExpression
        val qualifier = caller?.getQualifier() as? VlangTypeOwner

        val args = call.argumentList.elementList

        if (VlangCodeInsightUtil.isArrayMethodCall(call, "all", "any", "map")) {
            throw IllegalArgumentException("'all', 'any' and 'map' are not supported in debugger")
        }

        if (VlangCodeInsightUtil.isArrayMethodCall(call, "last", "first")) {
            val qualifierType = qualifier?.getType(null) ?: return

            if (qualifierType is VlangArrayTypeEx) {
                val elementType = qualifierType.inner
                append("*(${elementType.toCname()}*)")
            }
        }

        if (called is VlangMethodDeclaration) {
            if (called.getModuleName() == "stubs") {
                val qualifierType = qualifier?.getType(null)
                append(qualifierType?.toCname() + "_str")
            } else {
                val fqn = called.getQualifiedName()?.toCname(true) ?: called.name
                if (fqn != null) {
                    append(fqn)
                }
            }
        }

        append("(")

        if (qualifier != null) {
            if (called is VlangMethodDeclaration) {
                val receiverByPointer = called.receiverType is VlangPointerType || called.receiver.isMutable()
                val qualifierType = qualifier.getType(null)
                val qualifierTypeIsPointer = qualifierType is VlangPointerTypeEx

                if (!receiverByPointer && qualifierTypeIsPointer) {
                    append("*")
                }
            }

            qualifier.accept(this)

            if (args.isNotEmpty()) {
                append(", ")
            }
        }

        args.forEachIndexed { index, arg ->
            if (index != 0) {
                append(", ")
            }
            arg.accept(this)
        }
        append(")")
    }

    override fun visitElement(element: VlangElement) {
        element.value?.accept(this)
    }

    override fun visitValue(value: VlangValue) {
        val expr = value.expression
        expr.accept(this)
    }

    override fun visitIndexOrSliceExpr(expr: VlangIndexOrSliceExpr) {
        if (expr.isSlice) {
            processSlice(expr)
            return
        }

        val inner = expr.expressionList.firstOrNull() as VlangTypeOwner
        val index = expr.expressionList.lastOrNull()

        val innerType = inner.getType(null) ?: return

        if (innerType is VlangArrayTypeEx) {
            val elementType = innerType.inner

            append("*(${elementType.toCname()}*)")
            append("array_get(")
            inner.accept(this)
            append(", ")
            index?.accept(this)
            append(")")
        }

        if (innerType is VlangMapTypeEx) {
            val keyType = innerType.key
            val valueType = innerType.value

            val tmpVar = createTmpVar()
            predefined.apply {
                toPredefine = true
                append("auto $tmpVar = ")
                index?.accept(this@VlangExpressionEvaluator)
                append(";\n")
                toPredefine = false
            }

            append("*(${valueType.toCname()}*)")
            append("map_get(")
            append("&")
            inner.accept(this)
            append(", ")
            append("&$tmpVar")
            append(", 0")
            append(")")
        }
    }

    private fun processSlice(expr: VlangIndexOrSliceExpr) {
        val inner = expr.expression as? VlangTypeOwner ?: return

        if (expr.emptySlice != null) {
            // just return the inner expression
            inner.accept(this)
            return
        }

        val range = expr.range ?: return
        val innerType = inner.getType(null) ?: return

        append("(${innerType.toCname()})")
        append("array_slice")
        append("(")
        inner.accept(this)

        if (range.first != null) {
            append(", ")
            range.first?.accept(this)
        } else {
            append(", ")
            append("0")
        }

        if (range.second != null) {
            append(", ")
            range.second?.accept(this)
        } else {
            throw IllegalArgumentException("Slice without end is not supported")
        }

        append(")")
    }

    override fun visitStringLiteral(literal: VlangStringLiteral) {
        append(slit("\"${literal.contents}\""))
    }

    override fun visitLiteral(literal: VlangLiteral) {
        append(literal.text)
    }

    fun append(s: String) {
        if (toPredefine) {
            predefined.append(s)
        } else {
            result.append(s)
        }
    }

    private fun VlangTypeEx.toCname(): String {
        return when (this) {
            is VlangPrimitiveTypeEx -> name.value
            is VlangArrayTypeEx     -> toCname()
            is VlangPointerTypeEx   -> toCname()
            else                    -> qualifiedName().toCname()
        }
    }

    private fun VlangArrayTypeEx.toCname(): String {
        val inner = inner.toCname()
        return "Array_$inner"
    }

    private fun VlangPointerTypeEx.toCname(): String {
        val inner = inner.toCname()
        return "$inner*"
    }

    private fun String.toCname(isMethod: Boolean = false): String {
        val name = removePrefix("builtin.")
        if (isMethod) {
            val parts = name.split(".")
            if (parts.size == 2) {
                return parts.joinToString("_")
            }
            return parts.subList(0, parts.size - 1).joinToString("__") + "_" + parts.last()
        }

        return name.replace(".", "__")
    }

    private fun createTmpVar() = "tmp${tmpVarCounter++}"

    private fun slit(s: String) = "((string){.str=(byteptr)(\"\" $s), .len=(sizeof($s)-1), .is_lit=1})"
}