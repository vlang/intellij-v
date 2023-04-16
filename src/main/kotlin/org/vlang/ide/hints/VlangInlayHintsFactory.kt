package org.vlang.ide.hints

import com.intellij.codeInsight.hints.presentation.*
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.vlang.lang.psi.impl.VlangLangUtil
import org.vlang.lang.psi.types.*
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

@Suppress("UnstableApiUsage")
class VlangInlayHintsFactory(
    private val editor: Editor,
    private val factory: PresentationFactory,
) {
    private val textMetricsStorage =
        PresentationFactory::class.memberProperties
            .find { it.name == "textMetricsStorage" }
            ?.getter
            ?.let {
                it.isAccessible = true
                it
            }
            ?.call(factory) as? InlayTextMetricsStorage

    private val offsetFromTopProvider = object : InsetValueProvider {
        override val top = (textMetricsStorage?.getFontMetrics(false)?.offsetFromTop() ?: 1) - 1
    }

    fun implicitErrorVariable(project: Project): InlayPresentation {
        val text = factory.psiSingleReference(text("err")) {
            VlangLangUtil.getErrVariableDefinition(project)
        }

        return container(factory.seq(text, text(" →")))
    }

    fun enumValue(value: String): InlayPresentation = container(text(" = $value"))

    fun typeHint(type: VlangTypeEx, anchor: PsiElement): InlayPresentation = container(
        listOf(text(": "), hint(type, 1, anchor)).join()
    )

    private fun hint(type: VlangTypeEx, level: Int, anchor: PsiElement) = when (type) {
        is VlangFunctionTypeEx         -> functionType(type, level, anchor)
        is VlangAnonStructTypeEx       -> anonStructType(type, level, anchor)
        is VlangGenericInstantiationEx -> genericInstantiation(type, level, anchor)
        is VlangResolvableTypeEx<*>    -> resolvableType(type, anchor)
        is VlangPointerTypeEx          -> pointerType(type, level, anchor)
        else                           -> text(type.readableName(anchor))
    }

    private fun pointerType(type: VlangPointerTypeEx, level: Int, anchor: PsiElement): InlayPresentation {
        return factory.seq(
            text("&"),
            hint(type.inner, level, anchor),
        )
    }

    private fun anonStructType(type: VlangAnonStructTypeEx, level: Int, anchor: PsiElement): InlayPresentation {
        val fields = factory.collapsible(
            prefix = text("{ "),
            collapsed = text(PLACEHOLDER),
            expanded = {
                parametersHint(
                    type.resolve()
                        ?.fieldList
                        ?.mapNotNull { it.getType(null) }
                        ?: emptyList(), level + 1, anchor)
            },
            suffix = text(" }"),
            startWithPlaceholder = true
        )
        return factory.seq(text("struct "), fields)
    }

    private fun resolvableType(type: VlangResolvableTypeEx<*>, anchor: PsiElement): InlayPresentation {
        return factory.psiSingleReference(text((type as VlangTypeEx).readableName(anchor))) {
            type.resolve(anchor.project)
        }
    }

    private fun genericInstantiation(type: VlangGenericInstantiationEx, level: Int, anchor: PsiElement): InlayPresentation {
        val name = hint(type.inner, level, anchor)
        val parameters = factory.collapsible(
            prefix = text("["),
            collapsed = text(PLACEHOLDER),
            expanded = { parametersHint(type.specialization, level + 1, anchor) },
            suffix = text("]"),
            startWithPlaceholder = checkSize(level, type.specialization.size) || checkInnerSize(anchor, type.specialization)
        )

        return factory.seq(name, parameters)
    }

    private fun functionType(type: VlangFunctionTypeEx, level: Int, anchor: PsiElement): InlayPresentation {
        val parameters = type.signature.parameters.paramDefinitionList
        val returnType = type.result

        val fn = if (parameters.isEmpty()) {
            text("fn ()")
        } else {
            factory.collapsible(
                prefix = text("fn ("),
                collapsed = text(PLACEHOLDER),
                expanded = { parametersHint(type.params, level + 1, anchor) },
                suffix = text(")"),
                startWithPlaceholder = true
            )
        }

        if (returnType != null) {
            val startWithPlaceholder = returnType.readableName(anchor).length > 7
            val ret = factory.collapsible(
                prefix = text(" "),
                collapsed = text(PLACEHOLDER),
                expanded = { hint(returnType, level + 1, anchor) },
                suffix = text(""),
                startWithPlaceholder = startWithPlaceholder
            )
            return factory.seq(fn, ret)
        }

        return fn
    }

    private fun parametersHint(kinds: List<VlangTypeEx>, level: Int, anchor: PsiElement): InlayPresentation =
        kinds.map { hint(it, level, anchor) }.join(", ")

    fun rangeHint(inclusive: Boolean): InlayPresentation {
        return withInlayAttributes(
            smallContainer(factory.smallText(if (inclusive) "≤" else "<")),
            DefaultLanguageHighlighterColors.INLINE_PARAMETER_HINT
        )
    }

    private fun smallContainer(base: InlayPresentation): InlayPresentation {
        val rounding = withInlayAttributes(
            RoundWithBackgroundPresentation(
                InsetPresentation(
                    base,
                    left = 4,
                    right = 4,
                    top = 0,
                    down = 0,
                ),
                8,
                8
            ), DefaultLanguageHighlighterColors.INLAY_DEFAULT
        )
        return DynamicInsetPresentation(rounding, offsetFromTopProvider)
    }

    private fun container(base: InlayPresentation): InlayPresentation {
        val rounding = withInlayAttributes(
            RoundWithBackgroundPresentation(
                InsetPresentation(
                    base,
                    left = 7,
                    right = 7,
                    top = 2,
                    down = 1,
                ),
                8,
                8
            ), DefaultLanguageHighlighterColors.INLAY_DEFAULT
        )
        return DynamicInsetPresentation(rounding, offsetFromTopProvider)
    }

    private fun List<InlayPresentation>.join(separator: String = ""): InlayPresentation {
        if (separator.isEmpty()) {
            return factory.seq(*toTypedArray())
        }
        val presentations = mutableListOf<InlayPresentation>()
        var first = true
        for (presentation in this) {
            if (!first) {
                presentations.add(text(separator))
            }
            presentations.add(presentation)
            first = false
        }
        return factory.seq(*presentations.toTypedArray())
    }

    private fun text(text: String): InlayPresentation = factory.smallText(text)

    private fun withInlayAttributes(base: InlayPresentation, attributes: TextAttributesKey) =
        WithAttributesPresentation(
            base, attributes, editor,
            WithAttributesPresentation.AttributesFlags().withIsDefault(true)
        )

    private fun checkSize(level: Int, elementsCount: Int): Boolean =
        level + elementsCount > FOLDING_THRESHOLD

    private fun checkInnerSize(context: PsiElement, elements: List<VlangTypeEx>): Boolean {
        if (elements.isEmpty()) return false
        val first = elements.first()
        return first.readableName(context).length > 8
    }

    companion object {
        private const val PLACEHOLDER: String = "…"
        private const val FOLDING_THRESHOLD: Int = 3
    }
}
