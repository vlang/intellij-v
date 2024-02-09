package org.vlang.lang.psi.impl

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiParserFacade
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.childrenOfType
import org.vlang.lang.VlangLanguage
import org.vlang.lang.doc.psi.VlangDocComment
import org.vlang.lang.psi.*

object VlangElementFactory {
    fun createFileFromText(project: Project, text: String): VlangFile {
        return PsiFileFactory.getInstance(project).createFileFromText("dummy.v", VlangLanguage, text) as VlangFile
    }

    fun createVariableDeclarationStatement(project: Project, name: String, expr: PsiElement?): VlangStatement {
        val file = createFileFromText(project, "fn main() { $name := ${expr?.text} }")
        return PsiTreeUtil.findChildOfType(file, VlangSimpleStatement::class.java)!!
    }

    fun createVariableDefinition(project: Project, name: String): VlangVarDefinition {
        val stmt = createVariableDeclarationStatement(project, name, null)
        val decl = stmt.childrenOfType<VlangVarDeclaration>().firstOrNull() ?: error("Cannot create variable definition")
        return decl.varDefinitionList.firstOrNull() ?: error("Cannot create variable definition")
    }

    fun createType(project: Project, text: String): VlangType {
        val fieldDeclaration = createFieldDeclaration(project, "a", text) as VlangFieldDeclaration
        return fieldDeclaration.type!!
    }

    fun createReferenceExpression(project: Project, name: String): VlangReferenceExpression {
        val file = createFileFromText(project, "fn main() { a := $name }")
        return PsiTreeUtil.findChildOfType(file, VlangReferenceExpression::class.java)!!
    }

    fun createIdentifier(project: Project, text: String): PsiElement {
        val file = createFileFromText(project, "module $text")
        return file.getModule()?.getIdentifier()!!
    }

    fun createModuleClause(project: Project, text: String): VlangModuleClause {
        val file = createFileFromText(project, "module $text")
        return file.getModule()!!
    }

    fun createStatement(project: Project, text: String): VlangSimpleStatement {
        val file = createFileFromText(project, text)
        return PsiTreeUtil.findChildOfType(file, VlangSimpleStatement::class.java)!!
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

    fun createVisibilityModifiers(project: Project, name: String): VlangSymbolVisibility {
        val file = createFileFromText(project, "$name fn main() {}")
        return PsiTreeUtil.findChildOfType(file, VlangSymbolVisibility::class.java)!!
    }

    fun createFunctionResult(project: Project, text: String): VlangResult {
        val file = createFileFromText(project, "fn main() $text {}")
        return PsiTreeUtil.findChildOfType(file, VlangResult::class.java)!!
    }

    fun createElseBranch(project: Project, text: String = "else {\n\t\n}"): VlangElseBranch {
        val file = createFileFromText(project, "fn main() { if true {} $text")
        return PsiTreeUtil.findChildOfType(file, VlangElseBranch::class.java)!!
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

    fun createMemberModifiers(project: Project, text: String): VlangMemberModifiers {
        val file = createFileFromText(project, "struct Foo { $text a int }")
        return PsiTreeUtil.findChildOfType(file, VlangMemberModifiers::class.java)!!
    }

    fun createResultPropagation(project: Project, expression: PsiElement): VlangCompositeElement {
        val file = createFileFromText(project, "${expression.text}!")
        val call = PsiTreeUtil.findChildOfType(file, VlangCallExprWithPropagate::class.java)
        if (call != null) {
            return call
        }
        return PsiTreeUtil.findChildOfType(file, VlangDotExpression::class.java)!!
    }

    fun createReference(project: Project, text: String): VlangReferenceExpression {
        val children = PsiTreeUtil.findChildrenOfType(
            createFileFromText(project, text),
            VlangReferenceExpression::class.java
        )
        return children.last()
    }

    fun createDocComment(project: Project, text: String): VlangDocComment {
        return PsiTreeUtil.findChildOfType(
            createFileFromText(project, text),
            VlangDocComment::class.java
        )!!
    }

    fun createNewLine(project: Project): PsiElement {
        return PsiParserFacade.getInstance(project).createWhiteSpaceFromText("\n")
    }

    fun createSpace(project: Project): PsiElement {
        return PsiParserFacade.getInstance(project).createWhiteSpaceFromText(" ")
    }

    fun createTab(project: Project): PsiElement {
        return PsiParserFacade.getInstance(project).createWhiteSpaceFromText("\t")
    }

    fun createDoubleNewLine(project: Project): PsiElement {
        return PsiParserFacade.getInstance(project).createWhiteSpaceFromText("\n\n")
    }

    fun createImportList(project: Project, name: String, alias: String?): VlangImportList? {
        val text = if (alias != null) "import $name as $alias" else "import $name"
        val file = createFileFromText(project, text)
        return file.getImportList()
    }

    fun createBlock(project: Project): VlangBlock {
        val function = createFileFromText(project, "fn t() {\n}").getFunctions().firstOrNull()
            ?: error("Impossible situation! Parser is broken.")
        return function.getBlock()!!
    }

    fun createFieldDeclaration(project: Project, name: String, type: String): PsiElement {
        val file = createFileFromText(project, "struct S {\n\t$name $type\n}")
        return file.getStructs().firstOrNull()?.structType?.fieldList?.firstOrNull()?.parent
            ?: error("Impossible situation! Parser is broken.")
    }

    fun createFieldsGroup(project: Project, mutable: Boolean, name: String, type: String): VlangFieldsGroup {
        val file = createFileFromText(
            project,
            "struct S {\n\t${if (mutable) "mut:\n\t" else ""}$name $type\n\n}"
        )
        return file.getStructs().firstOrNull()?.structType?.fieldsGroupList?.firstOrNull()
            ?: error("Impossible situation! Parser is broken.")
    }

    fun createMatch(project: Project, expression: String, cases: List<String>): VlangMatchExpression {
        val casesText = cases.joinToString("\n\t") { "$it {}" }
        val text = "match $expression {\n\t$casesText\n}"

        val file = createFileFromText(project, text)
        val children = PsiTreeUtil.findChildrenOfType(file, VlangMatchExpression::class.java)
        return children.last()
    }

    fun createMutExpression(project: Project, text: String?): VlangMutExpression {
        val file = createFileFromText(project, "fn main() { foo(mut $text) }")
        return PsiTreeUtil.findChildOfType(file, VlangMutExpression::class.java)!!
    }
}
