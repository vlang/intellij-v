package org.vlang.ide.inspections.unused

import com.intellij.codeInsight.intention.FileModifier.SafeFieldForPreview
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.vlang.lang.psi.*
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

class VlangDeleteQuickFix(
    private val elementName: String,
    @SafeFieldForPreview val klass: KClass<out PsiElement>,
) : LocalQuickFix {
    override fun getFamilyName() = elementName

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val element = klass.safeCast(descriptor.startElement)
        if (element != null && element.isValid) {
            element.delete()
        }
    }

    companion object {
        val DELETE_FUNCTION_FIX = VlangDeleteQuickFix("Delete function", VlangFunctionDeclaration::class)
        val DELETE_METHOD_FIX = VlangDeleteQuickFix("Delete method", VlangMethodDeclaration::class)
        val DELETE_TYPE_ALIAS_FIX = VlangDeleteQuickFix("Delete type alias", VlangTypeAliasDeclaration::class)
        val DELETE_SUM_TYPE_FIX = VlangDeleteQuickFix("Delete sum type", VlangTypeAliasDeclaration::class)
        val DELETE_STRUCT_FIX = VlangDeleteQuickFix("Delete struct", VlangStructDeclaration::class)
        val DELETE_UNION_FIX = VlangDeleteQuickFix("Delete union", VlangStructDeclaration::class)
        val DELETE_ENUM_FIX = VlangDeleteQuickFix("Delete enum", VlangEnumDeclaration::class)
        val DELETE_INTERFACE_FIX = VlangDeleteQuickFix("Delete interface", VlangInterfaceDeclaration::class)
    }
}
