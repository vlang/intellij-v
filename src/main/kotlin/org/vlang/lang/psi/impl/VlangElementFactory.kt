package org.vlang.lang.psi.impl

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiParserFacade
import com.intellij.psi.util.PsiTreeUtil
import org.vlang.lang.VlangLanguage
import org.vlang.lang.psi.*

object VlangElementFactory {
    fun createFileFromText(project: Project, text: String): VlangFile {
        return PsiFileFactory.getInstance(project).createFileFromText("dummy.v", VlangLanguage.INSTANCE, text) as VlangFile
    }

    fun createVariableDeclarationStatement(project: Project, name: String, expr: PsiElement): VlangStatement {
        val file = createFileFromText(project, "fn main() { $name := ${expr.text} }")
        return PsiTreeUtil.findChildOfType(file, VlangSimpleStatement::class.java)!!
    }

    fun createReferenceExpression(project: Project, name: String): VlangReferenceExpression {
        val file = createFileFromText(project, "fn main() { a := $name }")
        return PsiTreeUtil.findChildOfType(file, VlangReferenceExpression::class.java)!!
    }

    fun createIdentifierFromText(project: Project, text: String): PsiElement {
        val file = createFileFromText(project, "module $text")
        return file.getModule()?.identifier!!
    }

    fun createCaptureList(project: Project, text: String): VlangCaptureList {
        val file = createFileFromText(project, "fn main() { fn [$text]() {} }")
        return PsiTreeUtil.findChildOfType(file, VlangFunctionLit::class.java)!!.captureList!!
    }

    fun createImportDeclaration(project: Project, name: String, alias: String?): VlangImportDeclaration? {
        return createImportList(project, name, alias)?.importDeclarationList?.firstOrNull()
    }

    fun createVarModifiers(project: Project, name: String): VlangVarModifiers {
        val file = createFileFromText(project, "fn main() { $name a := 100 }")
        return PsiTreeUtil.findChildOfType(file, VlangVarModifiers::class.java)!!
    }

    fun createComma(project: Project): PsiElement {
        return PsiTreeUtil.findChildOfType(
            createFileFromText(project, "fn main() { 1,2 }"),
            VlangLiteral::class.java
        )?.nextSibling!!
    }

    fun createRBrack(project: Project): PsiElement {
        return PsiTreeUtil.findChildOfType(
            createFileFromText(project, "fn main() { [1] }"),
            VlangArrayCreation::class.java
        )?.lastChild!!
    }

    fun createStringLiteral(project: Project, text: String): VlangStringLiteral {
        return PsiTreeUtil.findChildOfType(
            createFileFromText(project, "fn main() { $text }"),
            VlangStringLiteral::class.java
        )!!
    }

    fun createOrBlock(project: Project, expression: PsiElement): VlangOrBlockExpr {
        return PsiTreeUtil.findChildOfType(
            createFileFromText(project, "${expression.text} or {  }"),
            VlangOrBlockExpr::class.java
        )!!
    }

    fun createOptionalPropagation(project: Project, expression: PsiElement): VlangDotExpression {
        return PsiTreeUtil.findChildOfType(
            createFileFromText(project, "${expression.text}?"),
            VlangDotExpression::class.java
        )!!
    }

    fun createResultPropagation(project: Project, expression: PsiElement): VlangDotExpression {
        return PsiTreeUtil.findChildOfType(
            createFileFromText(project, "${expression.text}!"),
            VlangDotExpression::class.java
        )!!
    }

    fun createNewLine(project: Project): PsiElement {
        return PsiParserFacade.getInstance(project).createWhiteSpaceFromText("\n")
    }

    fun createSpace(project: Project): PsiElement {
        return PsiParserFacade.getInstance(project).createWhiteSpaceFromText(" ")
    }

    fun createDoubleNewLine(project: Project): PsiElement {
        return PsiParserFacade.getInstance(project).createWhiteSpaceFromText("\n\n")
    }

    fun createImportList(project: Project, name: String, alias: String?): VlangImportList? {
        val text = if (alias != null) "import $name as $alias" else "import $name"
        val file = createFileFromText(project, text)
        return file.getImportList()
    }
}
