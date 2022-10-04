package org.vlang.lang.types

import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.vlang.lang.psi.*
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx

abstract class TypeTestBase : BasePlatformTestCase() {
    override fun getTestDataPath() = "src/test/resources/types"

    protected open fun doTest(vararg fixtureFiles: String) {
        myFixture.configureByFiles(*fixtureFiles)
        runTypeTest(fixtureFiles)
    }

    protected fun runTypeTest(fixtureFiles: Array<out String>) {
        findExprTypeCalls(fixtureFiles).forEach { call ->
            checkExprTypeCall(call)
        }
    }

    private inline fun <reified T> PsiElement.findChildren(crossinline condition: (PsiElement) -> Boolean): List<T> {
        val result = mutableListOf<T>()
        accept(object : VlangVisitor() {
            override fun visitElement(element: PsiElement) {
                if (condition(element) && element is T) {
                    result.add(element)
                }

                var child = element.firstChild
                while (child != null) {
                    child.accept(this)
                    child = child.nextSibling
                }
            }
        })
        return result
    }

    private fun checkExprTypeCall(call: VlangCallExpr) {
        val expr = call.parameters.first() as VlangTypeOwner
        val expectedTypePsi = call.parameters.last() as VlangStringLiteral
        val expectedTypeString = expectedTypePsi.contents
        val gotType = expr.getType(null).toEx()

        val gotTypeString = gotType.readableName(call)

        val file = call.containingFile
        check(gotTypeString == expectedTypeString) {
            """
                In file ${file.name}:${call.line()}
                
                Type mismatch. 
                Expected: $expectedTypeString
                Found: $gotTypeString
                
                
            """.trimIndent()
        }
    }

    private fun findExprTypeCalls(fixtureFiles: Array<out String>): List<VlangCallExpr> {
        return fixtureFiles.map {
            val file = myFixture.findFileInTempDir(it) ?: return@map emptyList<VlangCallExpr>()
            myFixture.openFileInEditor(file)

            myFixture.file.findChildren { el ->
                if (el !is VlangCallExpr) {
                    return@findChildren false
                }

                val parameters = el.parameters
                if (el.expression?.text != "expr_type") {
                    return@findChildren false
                }

                if (parameters.size != 2) {
                    return@findChildren false
                }

                val first = parameters.first()
                val last = parameters.last()

                first is VlangTypeOwner && last is VlangStringLiteral
            }
        }.flatten()
    }

    private fun VlangCompositeElement.line(): Int {
        val document = PsiDocumentManager.getInstance(project).getDocument(containingFile)
        val lineNumber = if (document != null) {
            document.getLineNumber(textRange.startOffset) + 1
        } else {
            0
        }
        return lineNumber
    }
}
