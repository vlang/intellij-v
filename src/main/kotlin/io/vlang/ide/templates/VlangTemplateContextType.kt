package io.vlang.ide.templates

import com.intellij.codeInsight.template.TemplateActionContext
import com.intellij.codeInsight.template.TemplateContextType
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiUtilCore
import com.intellij.psi.util.parentOfType
import io.vlang.lang.VlangLanguage
import io.vlang.lang.completion.VlangCompletionUtil
import io.vlang.lang.psi.VlangFile
import io.vlang.lang.psi.VlangImportSpec
import io.vlang.lang.psi.VlangModuleClause
import io.vlang.lang.psi.VlangSimpleStatement
import io.vlang.utils.inside

abstract class VlangTemplateContextType(presentableName: String) : TemplateContextType(presentableName) {

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

    class Generic : VlangTemplateContextType("V") {
        override fun isInContext(element: PsiElement) = element.parent is VlangFile || element.parent.parent is VlangFile
    }

    class TopLevel : VlangTemplateContextType("Top-level") {
        override fun isInContext(element: PsiElement): Boolean {
            val parentSimpleStatement = element.parentOfType<VlangSimpleStatement>() ?: return false
            return parentSimpleStatement.parent is VlangFile
        }
    }

    class Statement : VlangTemplateContextType("Statement") {
        override fun isInContext(element: PsiElement) = element.inside<VlangSimpleStatement>()
    }

    class Comment : VlangTemplateContextType("Comment") {
        override fun isInContext(element: PsiElement) = false
        override fun isCommentInContext() = true
    }
}
