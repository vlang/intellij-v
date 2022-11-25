package org.vlang.ide.codeInsight

import com.intellij.codeInsight.CodeInsightBundle
import com.intellij.lang.parameterInfo.*
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.parentOfType
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.*
import org.vlang.lang.psi.types.VlangFunctionTypeEx
import org.vlang.lang.psi.types.VlangTypeEx

class VlangParameterInfoHandler : ParameterInfoHandlerWithTabActionSupport<VlangArgumentList, VlangTypeEx?, VlangElement> {
    override fun getActualParameters(o: VlangArgumentList) = o.elementList.toTypedArray()

    override fun getActualParameterDelimiterType(): IElementType = VlangTypes.COMMA

    override fun getActualParametersRBraceType(): IElementType = VlangTypes.RPAREN

    override fun getArgumentListAllowedParentClasses() = setOf<Class<*>>()

    override fun getArgListStopSearchClasses() = setOf<Class<*>>()

    override fun getArgumentListClass() = VlangArgumentList::class.java

    override fun findElementForParameterInfo(context: CreateParameterInfoContext) = getList(context)

    override fun findElementForUpdatingParameterInfo(context: UpdateParameterInfoContext) = getList(context)

    override fun updateParameterInfo(list: VlangArgumentList, context: UpdateParameterInfoContext) {
        context.setCurrentParameter(ParameterInfoUtils.getCurrentParameterIndex(list.node, context.offset, VlangTypes.COMMA))
    }

    override fun updateUI(type: VlangTypeEx?, context: ParameterInfoUIContext) {
        updatePresentation(type, context)
    }

    override fun showParameterInfo(argList: VlangArgumentList, context: CreateParameterInfoContext) {
        val parent = argList.parent as? VlangCallExpr ?: return
        val type = findFunctionType(parent.expression!!.getType(null))
        if (type != null) {
            context.itemsToShow = arrayOf(type)
            context.showHint(argList, argList.textRange.startOffset, this)
        }
    }

    companion object {
        private fun getList(context: ParameterInfoContext): VlangArgumentList? {
            val at = context.file.findElementAt(context.offset)
            return at?.parentOfType()
        }

        private fun findFunctionType(type: VlangTypeEx?) = type as? VlangFunctionTypeEx

        fun updatePresentation(type: VlangTypeEx?, context: ParameterInfoUIContext): String? {
            if (type == null) {
                context.isUIComponentEnabled = false
                return null
            }

            val signature = if (type is VlangFunctionTypeEx) type.signature else null
            if (signature == null) {
                context.isUIComponentEnabled = false
                return null
            }

            val parameters = signature.parameters
            val parametersPresentations = getParameterPresentations(parameters)
            val builder = StringBuilder()

            var startHighlighting = 0
            var endHighlighting = 0

            if (parametersPresentations.isNotEmpty()) {
                val selected = if (isLastParameterVariadic(parameters.paramDefinitionList))
                    context.currentParameterIndex.coerceAtMost(parametersPresentations.size - 1)
                else
                    context.currentParameterIndex

                for (i in parametersPresentations.indices) {
                    if (i != 0) {
                        builder.append(", ")
                    }
                    if (i == selected) {
                        startHighlighting = builder.length
                    }
                    builder.append(parametersPresentations[i])
                    if (i == selected) {
                        endHighlighting = builder.length
                    }
                }
            } else {
                builder.append(CodeInsightBundle.message("parameter.info.no.parameters"))
            }

            return context.setupUIComponentPresentation(
                builder.toString(), startHighlighting, endHighlighting,
                false, false,
                false, context.defaultParameterColor,
            )
        }

        private fun getParameterPresentations(parameters: VlangParameters): List<String> {
            val paramDeclarations = parameters.paramDefinitionList
            val paramPresentations = mutableListOf<String>()

            for (paramDefinition in paramDeclarations) {
                val isVariadic = paramDefinition.isVariadic
                val separator = if (isVariadic) "..." else " "
                paramPresentations.add(paramDefinition.name + separator + paramDefinition.type.text)
            }

            return paramPresentations
        }

        private fun isLastParameterVariadic(declarations: List<VlangParamDefinition>): Boolean {
            val lastItem = declarations.lastOrNull()
            return lastItem != null && lastItem.isVariadic
        }
    }
}
