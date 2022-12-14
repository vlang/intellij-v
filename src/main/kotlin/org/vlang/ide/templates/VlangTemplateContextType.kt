package org.vlang.ide.templates

import com.intellij.codeInsight.template.EverywhereContextType
import com.intellij.codeInsight.template.TemplateActionContext
import com.intellij.codeInsight.template.TemplateContextType
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiUtilCore
import com.intellij.psi.util.parentOfType
import org.vlang.lang.VlangLanguage
import org.vlang.lang.completion.VlangCompletionUtil
import org.vlang.lang.psi.VlangFile
import org.vlang.lang.psi.VlangFunctionDeclaration
import org.vlang.lang.psi.VlangImportSpec
import org.vlang.lang.psi.VlangModuleClause

abstract class VlangTemplateContextType(
    id: String,
    presentableName: String,
    baseContextType: Class<out TemplateContextType>?
) : TemplateContextType(id, presentableName, baseContextType) {

    override fun isInContext(templateActionContext: TemplateActionContext): Boolean {
        val file = templateActionContext.file
        val offset = templateActionContext.startOffset

        if (!PsiUtilCore.getLanguageAtOffset(file, offset).isKindOf(VlangLanguage)) {
            return false
        }

        var element = file.findElementAt(offset)
        if (element == null) {
            element = file.findElementAt(offset - 1)
        }

        if (element == null) {
            return false
        }

        if (VlangCompletionUtil.shouldSuppressCompletion(element)) {
            return false
        }

        when {
            element is PsiWhiteSpace                          -> return false
            element.parentOfType<PsiComment>() != null        -> return isCommentInContext()
            element.parentOfType<VlangModuleClause>() != null -> return false
            element.parentOfType<VlangImportSpec>() != null   -> return false
        }

        return isInContext(element)
    }

    protected abstract fun isInContext(element: PsiElement): Boolean

    protected open fun isCommentInContext() = false

    class Generic : VlangTemplateContextType("VLANG_GENERIC", "vlang", EverywhereContextType::class.java) {
        override fun isInContext(element: PsiElement) = element.parent is VlangFile || element.parent.parent is VlangFile
    }

    class TopLevel : VlangTemplateContextType("VLANG_TOPLEVEL", "top-level", Generic::class.java) {
        override fun isInContext(element: PsiElement) = element.parent is VlangFile || element.parent.parent is VlangFile
    }

    class Statement : VlangTemplateContextType("VLANG_STATEMENT", "statement", Generic::class.java) {
        override fun isInContext(element: PsiElement) = element.parentOfType<VlangFunctionDeclaration>() != null
    }

    class Comment : VlangTemplateContextType("VLANG_COMMENT", "comment", Generic::class.java) {
        override fun isInContext(element: PsiElement) = false
        override fun isCommentInContext() = true
    }
}
