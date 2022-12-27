package org.vlang.lang.types

import com.intellij.psi.PsiElement
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.intellij.lang.annotations.Language
import org.vlang.lang.psi.VlangCallExpr
import org.vlang.lang.psi.VlangStringLiteral
import org.vlang.lang.psi.VlangTypeOwner
import org.vlang.lang.psi.VlangVisitor
import org.vlang.lang.psi.types.VlangUnknownTypeEx
import org.vlang.utils.line

abstract class TypeTestBase : BasePlatformTestCase() {
    override fun getTestDataPath() = "src/test/resources/types"

    protected open fun doTest(@Language("vlang") text: String) {
        myFixture.configureByText("a.v", text)
        runTypeTest(arrayOf("a.v"))
    }

    protected open fun doTestFile(vararg fixtureFiles: String) {
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
        val gotType = expr.getType(null) ?: VlangUnknownTypeEx.INSTANCE

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
                    fail("Invalid test call: ${el.text}")
                    return@findChildren false
                }

                val first = parameters.first()
                val last = parameters.last()

                assert(first is VlangTypeOwner && last is VlangStringLiteral) {
                    "Invalid test call: ${el.text}"
                }
                first is VlangTypeOwner && last is VlangStringLiteral
            }
        }.flatten()
    }
}
