package org.vlang.ide.inspections.general

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiReference
import com.intellij.psi.formatter.FormatterUtil
import org.vlang.ide.codeInsight.imports.VlangImportModuleQuickFix
import org.vlang.ide.inspections.VlangBaseInspection
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.VlangCompositeElement
import org.vlang.lang.psi.VlangReferenceExpression
import org.vlang.lang.psi.VlangTypeReferenceExpression
import org.vlang.lang.psi.VlangVisitor
import org.vlang.lang.psi.impl.VlangPsiImplUtil
import org.vlang.lang.psi.impl.VlangReference

class VlangUnresolvedReferenceInspection : VlangBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitTypeReferenceExpression(o: VlangTypeReferenceExpression) {
                super.visitTypeReferenceExpression(o)

                val reference = o.reference
                val qualifier = o.getQualifier()
                val id = o.getIdentifier()
                processReference(o, id, qualifier, reference)
            }

            override fun visitReferenceExpression(o: VlangReferenceExpression) {
                super.visitReferenceExpression(o)

                val reference = o.reference
                val qualifier = o.getQualifier()
                val id = o.getIdentifier()
                processReference(o, id, qualifier, reference)
            }

            private fun processReference(
                expr: VlangCompositeElement,
                id: PsiElement,
                qualifier: VlangCompositeElement?,
                reference: VlangReference,
            ) {
                val qualifierResolve = qualifier?.reference?.resolve()
                if (qualifier != null && qualifierResolve == null) return

                if (VlangPsiImplUtil.prevDot(id)) {
                    // TODO: remove this check when we have a better reference handling
                    //       for now checks only potential module ref
                    return
                }

                val name = id.text

                if (reference.resolve() == null) {
                    if (isProhibited(expr, qualifier)) {
                        val fixes = createImportPackageFixes(expr, reference, holder.isOnTheFly)
                        holder.registerProblem(id, "Unresolved reference '$name'", ProblemHighlightType.LIKE_UNKNOWN_SYMBOL, *fixes)
                    }
                }
            }
        }
    }

    private fun createImportPackageFixes(target: VlangCompositeElement, reference: PsiReference, onTheFly: Boolean): Array<LocalQuickFix> {
        if (onTheFly) {
            val importFix = VlangImportModuleQuickFix(reference)
            if (importFix.isAvailable(target.project, target.containingFile, target, target)) {
                return arrayOf(importFix)
            }
        }

        val packagesToImport = VlangImportModuleQuickFix.getImportVariantsToImport(reference.canonicalText, target)
        if (packagesToImport.isNotEmpty()) {
            val result = mutableListOf<LocalQuickFix>()
            for (importPath in packagesToImport) {
                val importFix = VlangImportModuleQuickFix(target, importPath)
                if (importFix.isAvailable(target.project, target.containingFile, target, target)) {
                    result.add(importFix)
                }
            }
            return result.toTypedArray()
        }

        return LocalQuickFix.EMPTY_ARRAY
    }

    private fun isProhibited(o: VlangCompositeElement, qualifier: VlangCompositeElement?): Boolean {
        val next = FormatterUtil.getNextNonWhitespaceSibling(o.node)
        val isDot = next != null && next.elementType === VlangTypes.DOT
        return isDot || qualifier != null
    }
}
