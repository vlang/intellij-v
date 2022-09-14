package org.vlang.lang.completion

import com.intellij.codeInsight.completion.InsertHandler
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.lookup.LookupElementPresentation
import com.intellij.codeInsight.lookup.LookupElementRenderer
import org.vlang.ide.ui.VIcons
import org.vlang.lang.psi.*

object VlangCompletionUtil {
    const val KEYWORD_PRIORITY = 20
    const val CONTEXT_KEYWORD_PRIORITY = 25
    const val NOT_IMPORTED_FUNCTION_PRIORITY = 3
    const val FUNCTION_PRIORITY = NOT_IMPORTED_FUNCTION_PRIORITY + 10
    const val NOT_IMPORTED_TYPE_PRIORITY = 5
    const val TYPE_PRIORITY = NOT_IMPORTED_TYPE_PRIORITY + 10
    const val NOT_IMPORTED_TYPE_CONVERSION = 1
    const val TYPE_CONVERSION = NOT_IMPORTED_TYPE_CONVERSION + 10
    const val NOT_IMPORTED_VAR_PRIORITY = 15
    const val VAR_PRIORITY = NOT_IMPORTED_VAR_PRIORITY + 10
    const val FIELD_PRIORITY = CONTEXT_KEYWORD_PRIORITY + 1
    const val LABEL_PRIORITY = 15
    const val PACKAGE_PRIORITY = 5

    fun createVariableLikeLookupElement(v: VlangNamedElement): LookupElement? {
        val name = v.name
        if (name.isNullOrEmpty()) {
            return null
        }
        return createVariableLikeLookupElement(
            v, name, null, // Lazy.VARIABLE_OR_FUNCTION_INSERT_HANDLER,
            VAR_PRIORITY.toDouble()
        )
    }

    fun createVariableLikeLookupElement(
        v: VlangNamedElement, lookupString: String,
        insertHandler: InsertHandler<LookupElement?>?,
        priority: Double,
    ): LookupElement {
        return PrioritizedLookupElement.withPriority(
            LookupElementBuilder.createWithSmartPointer(lookupString, v)
                .withRenderer(VARIABLE_RENDERER)
                .withInsertHandler(insertHandler), priority
        )
    }

    private val VARIABLE_RENDERER = object : LookupElementRenderer<LookupElement>() {
        override fun renderElement(element: LookupElement, p: LookupElementPresentation) {
            val o = element.psiElement as? VlangNamedElement ?: return
            val v = o
//            val text: String = VlangPsiImplUtil.getText(type)
            val type = v.getType(null)
            val text = type?.text ?: ""
            val icon =
                if (v is VlangVarDefinition)
                    VIcons.Variable
                else when (v) {
                    is VlangParamDefinition          -> VIcons.Parameter
                    is VlangReceiver                 -> VIcons.Receiver
                    is VlangConstDefinition          -> VIcons.Constant
                    is VlangAnonymousFieldDefinition -> VIcons.Field
                    else                             -> null
                }
            p.icon = icon
            p.setTailText("", true)
            p.typeText = text
            p.isTypeGrayed = true
            p.itemText = element.lookupString
        }
    }
}
