package org.vlang.lang.psi.impl

import com.intellij.lang.parser.GeneratedParserUtilBase
import com.intellij.openapi.util.Conditions
import com.intellij.psi.PsiElement
import com.intellij.psi.SyntaxTraverser
import com.intellij.psi.util.elementType
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.*

object VlangPsiImplUtil {
    @JvmStatic
    fun getName(o: VlangFunctionDeclaration): String {
        return o.getIdentifier().text ?: ""
    }

    @JvmStatic
    fun getIdentifier(o: VlangMethodDeclaration): PsiElement? {
        return if (o.methodName.elementType == VlangTypes.IDENTIFIER) o.methodName else null
    }

    @JvmStatic
    fun getName(o: VlangStructDeclaration): String {
        return o.getIdentifier()?.text ?: ""
    }

    @JvmStatic
    fun getName(o: VlangUnionDeclaration): String {
        return o.getIdentifier()?.text ?: ""
    }

    @JvmStatic
    fun getName(o: VlangEnumDeclaration): String {
        return o.getIdentifier()?.text ?: ""
    }

    @JvmStatic
    fun getName(o: VlangTypeAliasDeclaration): String {
        return o.getIdentifier()?.text ?: ""
    }

    @JvmStatic
    fun getName(o: VlangImportSpec): String {
        return o.getIdentifier().text ?: ""
    }

    @JvmStatic
    fun getLastPart(o: VlangImportSpec): String {
        return o.name.split(".").last()
    }

    @JvmStatic
    fun getIdentifier(o: VlangStructDeclaration): PsiElement? {
        return o.structType.getIdentifier()
    }

    @JvmStatic
    fun getIdentifier(o: VlangTypeDecl): PsiElement? {
        return o.typeReferenceExpressionList.lastOrNull()?.getIdentifier()
    }

    @JvmStatic
    fun getReference(o: VlangReferenceExpression): VlangReference {
        return VlangReference(o)
    }

    @JvmStatic
    fun getReference(o: VlangTypeReferenceExpression): VlangReference {
        return VlangReference(o)
    }

    @JvmStatic
    fun getIdentifier(o: VlangImportSpec): PsiElement {
        return o.firstChild
    }


    @JvmStatic
    fun getName(o: VlangModuleClause): String {
        return o.identifier?.text ?: "<unknown>"
    }

    fun goTraverser(): SyntaxTraverser<PsiElement?> {
        return SyntaxTraverser.psiTraverser()
            .forceDisregardTypes(Conditions.equalTo(GeneratedParserUtilBase.DUMMY_BLOCK))
    }

}