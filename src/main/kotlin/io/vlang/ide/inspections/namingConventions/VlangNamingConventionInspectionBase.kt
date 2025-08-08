package io.vlang.ide.inspections.namingConventions

import com.intellij.codeInsight.intention.preview.IntentionPreviewInfo
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.project.Project
import com.intellij.psi.util.parentOfType
import com.intellij.refactoring.RefactoringFactory
import com.intellij.polySymbols.utils.NameCaseUtils
import io.vlang.ide.codeInsight.VlangCodeInsightUtil
import io.vlang.ide.inspections.VlangBaseInspection
import io.vlang.lang.psi.VlangConstDefinition
import io.vlang.lang.psi.VlangEnumFieldDefinition
import io.vlang.lang.psi.VlangNamedElement

abstract class VlangNamingConventionInspectionBase : VlangBaseInspection() {
    protected fun ProblemsHolder.checkName(named: VlangNamedElement, elementKind: String, checkLen: Boolean = true) {
        val name = named.name ?: return

        if (VlangCodeInsightUtil.insideBuiltinModule(named)) return
        if (VlangCodeInsightUtil.insideTranslatedFile(named)) return
        if (VlangCodeInsightUtil.nonVlangName(name)) return

        val identifier = named.getIdentifier() ?: return

        if (name.length == 1 && checkLen) {
            registerProblem(
                identifier,
                "Name must be at least 2 characters long",
                ProblemHighlightType.GENERIC_ERROR,
            )
            return
        }

        if (!name.first().isUpperCase()) {
            registerProblem(
                identifier, "$elementKind name must start with uppercase letter",
                ProblemHighlightType.GENERIC_ERROR, NAME_FIX
            )
        }
    }

    protected fun ProblemsHolder.checkSnakeCase(named: VlangNamedElement, elementKind: String) {
        val name = named.name ?: return
        if (VlangCodeInsightUtil.insideBuiltinModule(named)) return
        if (VlangCodeInsightUtil.insideTranslatedFile(named)) return
        if (VlangCodeInsightUtil.nonVlangName(name)) return

        val identifier = named.getIdentifier() ?: return

        if (elementKind == "Variable" && name == "_") {
            // allow '_' as variable name
            return
        }

        if (name.startsWith("_") && named !is VlangEnumFieldDefinition && named !is VlangConstDefinition) {
            registerProblem(
                identifier, "$elementKind name cannot start with '_'",
                ProblemHighlightType.GENERIC_ERROR, TO_SNAKE_CASE_FIX
            )
        }

        if (name.indexOfFirst { it.isUpperCase() } != -1) {
            registerProblem(
                identifier, "$elementKind name cannot contain uppercase letters, use snake_case instead",
                ProblemHighlightType.GENERIC_ERROR, TO_SNAKE_CASE_FIX
            )
        }
    }

    class NameQuickFix : LocalQuickFix {
        override fun getFamilyName() = "Fix name"

        override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
            val named = descriptor.psiElement.parentOfType<VlangNamedElement>()

            val name = named?.name ?: return
            val newName = name.first().uppercaseChar() + name.substring(1)

            invokeLater {
                RefactoringFactory.getInstance(project).createRename(named, newName).run()
            }
        }

        override fun generatePreview(project: Project, previewDescriptor: ProblemDescriptor): IntentionPreviewInfo {
            return IntentionPreviewInfo.EMPTY
        }
    }

    class ToSnakeCaseQuickFix : LocalQuickFix {
        override fun getFamilyName() = "Convert to snake_case"

        override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
            val named = descriptor.psiElement.parentOfType<VlangNamedElement>()

            val name = named?.name ?: return
            val newName = NameCaseUtils.toSnakeCase(name)

            invokeLater {
                RefactoringFactory.getInstance(project).createRename(named, newName).run()
            }
        }

        override fun generatePreview(project: Project, previewDescriptor: ProblemDescriptor): IntentionPreviewInfo {
            return IntentionPreviewInfo.EMPTY
        }
    }

    companion object {
        private val NAME_FIX = NameQuickFix()
        private val TO_SNAKE_CASE_FIX = ToSnakeCaseQuickFix()
    }
}
