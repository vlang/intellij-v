package org.vlang.ide.hints

import com.intellij.codeInsight.daemon.impl.InlayHintsPassFactoryInternal
import com.intellij.codeInsight.hints.*
import com.intellij.codeInsight.hints.presentation.*
import com.intellij.codeInsight.hints.settings.showInlaySettings
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.childrenOfType
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.lang.VlangLanguage
import org.vlang.lang.psi.*
import org.vlang.lang.psi.types.VlangResultTypeEx
import org.vlang.lang.psi.types.VlangUnknownTypeEx
import org.vlang.utils.line

@Suppress("UnstableApiUsage")
class VlangInlayHintsCollector(
    editor: Editor,
    private val file: PsiFile,
    private val settings: VlangInlayHintsProvider.Settings,
    private val sink: InlayHintsSink,
    private val key: SettingsKey<VlangInlayHintsProvider.Settings>,
) : FactoryInlayHintsCollector(editor) {
    private val typeHintsFactory = VlangInlayHintsFactory(editor, factory)

    override fun collect(element: PsiElement, editor: Editor, sink: InlayHintsSink): Boolean {
        // If the indexing process is in progress.
        if (file.project.service<DumbService>().isDumb) return true

        when {
            element is VlangRangeExpr && settings.showForRanges          -> handleRange(element)
            element is VlangIndexOrSliceExpr && settings.showForRanges   -> handleSlice(element)
            element is VlangVarDefinition && settings.showForVariables   -> handleVarDefinition(element)
            element is VlangOrBlockExpr && settings.showForErrVariable   -> handleOrBlockExpr(element)
            element is VlangElseBranch && settings.showForErrVariable -> handleElseStatement(element)
            element is VlangConstDefinition && settings.showForConstants -> handleConstDefinition(element)
        }

        return true
    }

    private fun handleConstDefinition(element: VlangConstDefinition) {
        val type = element.getTypeInner(null) ?: return
        if (type is VlangUnknownTypeEx) {
            // no need show hint if type is unknown
            return
        }

        val presentation = typeHintsFactory.typeHint(type, element)
        val finalPresentation = presentation.withDisableAction(element.project, "Constants") {
            showForConstants = false
        }

        sink.addInlineElement(
            element.getIdentifier().endOffset,
            relatesToPrecedingText = true,
            presentation = finalPresentation,
            placeAtTheEndOfLine = false
        )
    }

    private fun handleOrBlockExpr(element: VlangOrBlockExpr) {
        val right = element.block
        val openBracket = right.lbrace
        val closeBracket = right.rbrace
        val leftExpr = element.expression ?: return
        val leftType = leftExpr.getType(null)
        if (leftType !is VlangResultTypeEx) {
            return
        }

        handleImplicitErrorVariable(element.project, openBracket, closeBracket)
    }

    private fun handleElseStatement(element: VlangElseBranch) {
        if (!VlangCodeInsightUtil.insideElseBlockIfGuard(element)) {
            return
        }

        val right = element.block ?: return
        val openBracket = right.lbrace
        val closeBracket = right.rbrace

        val ifExpr = element.parent as? VlangIfExpression ?: return
        val expr = ifExpr.guardVarDeclaration?.expression ?: return
        val leftType = expr.getType(null)
        if (leftType !is VlangResultTypeEx) {
            return
        }

        handleImplicitErrorVariable(element.project, openBracket, closeBracket)
    }

    private fun handleImplicitErrorVariable(
        project: Project,
        openBracket: PsiElement,
        closeBracket: PsiElement?,
    ) {
        if (openBracket.line() == closeBracket?.line()) {
            // don't show hint if 'else { ... }'
            return
        }
        val presentation = typeHintsFactory.implicitErrorVariable(project)
        val finalPresentation = presentation.withDisableAction(project, "Implicit err variables") {
            showForErrVariable = false
        }

        sink.addInlineElement(
            openBracket.endOffset,
            relatesToPrecedingText = true,
            presentation = finalPresentation,
            placeAtTheEndOfLine = false
        )
    }

    private fun handleVarDefinition(element: VlangVarDefinition) {
        if (element.isBlank()) {
            // don't show hint for "_" variables
            return
        }

        val decl = element.parent as? VlangVarDeclaration
        if (decl != null) {
            if (decl.expressionList.size == 1) {
                if (isObviousCase(decl.expressionList[0])) {
                    return
                }
            }
        }

        val type = element.getTypeInner(null) ?: return
        if (type is VlangUnknownTypeEx) {
            // no need show hint if type is unknown
            return
        }

        val presentation = typeHintsFactory.typeHint(type, element)
        val finalPresentation = presentation.withDisableAction(element.project, "Variables") {
            showForVariables = false
        }

        sink.addInlineElement(
            element.endOffset,
            relatesToPrecedingText = true,
            presentation = finalPresentation,
            placeAtTheEndOfLine = false
        )
    }

    private fun isObviousCase(element: PsiElement): Boolean {
        return element is VlangLiteralValueExpression || (element is VlangLiteral && element.isBoolean) || element is VlangStringLiteral
    }

    private fun handleSlice(element: VlangIndexOrSliceExpr) {
        // arr[0..1]
        if (element.childrenOfType<VlangRangeExpr>().isNotEmpty()) {
            // processed bellow
            return
        }

        // arr[..]
        if (element.emptySlice != null) {
            return
        }

        val lbrack = element.lbrack ?: element.hashLbrack ?: return
        val rbrack = element.rbrack

        // arr[..10]
        val rangeVariant1 = lbrack.nextSibling
        // arr[10..]
        val rangeVariant2 = rbrack.prevSibling

        if (rangeVariant1.textMatches("..")) {
            // arr[..<10]
            addRangeHint(element.project, rangeVariant1.endOffset, false)
        } else if (rangeVariant2.textMatches("..")) {
            // arr[10≤..]
            addRangeHint(element.project, rangeVariant2.startOffset, true)
        }
    }

    private fun handleRange(element: VlangRangeExpr) {
        val op = element.range ?: element.tripleDot ?: return
        // `a`...`z`
        val inclusive = element.tripleDot != null

        addRangeHint(element.project, op.startOffset, true)
        addRangeHint(element.project, op.endOffset, inclusive)
    }

    private fun addRangeHint(project: Project, offset: Int, inclusive: Boolean) {
        val presentation = typeHintsFactory.rangeHint(inclusive)
        val finalPresentation = presentation.withDisableAction(project, "Ranges") {
            showForRanges = false
        }

        sink.addInlineElement(
            offset,
            relatesToPrecedingText = true,
            presentation = finalPresentation,
            placeAtTheEndOfLine = false
        )
    }

    private fun InlayPresentation.withDisableAction(
        project: Project,
        name: String,
        action: VlangInlayHintsProvider.Settings.() -> Unit,
    ): InsetPresentation = InsetPresentation(
        MenuOnClickPresentation(this, project) {
            listOf(
                object : AnAction("Disable '$name' hints type") {
                    override fun actionPerformed(e: AnActionEvent) {
                        val inlayHintsSettings = InlayHintsSettings.instance()
                        val settings = inlayHintsSettings.findSettings(key, VlangLanguage) { VlangInlayHintsProvider.Settings() }
                        settings.action()
                        inlayHintsSettings.storeSettings(key, VlangLanguage, settings)
                        InlayHintsPassFactoryInternal.forceHintsUpdateOnNextPass()
                    }
                },
                object : AnAction("Hints Settings...") {
                    override fun actionPerformed(e: AnActionEvent) {
                        showInlaySettings(file.project, VlangLanguage, null)
                    }
                }
            )
        }, left = 1
    )
}
