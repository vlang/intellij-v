package org.vlang.integration

import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.openapi.ui.naturalSorted
import com.intellij.psi.PsiDirectory
import com.intellij.psi.search.searches.DefinitionsScopedSearch
import com.intellij.psi.util.parentOfType
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import com.intellij.util.CommonProcessors
import org.intellij.lang.annotations.Language
import org.vlang.lang.psi.VlangNamedElement
import org.vlang.lang.psi.impl.VlangPomTargetPsiElement
import org.vlang.lang.search.VlangGotoUtil

abstract class IntegrationTestBase : BasePlatformTestCase() {
    override fun getTestDataPath() = "src/test/resources/integration"

    class IntegrationTestBaseContext(private val myFixture: CodeInsightTestFixture) {
        private fun moveCaretTo(offset: Int) {
            myFixture.editor.caretModel.moveToOffset(offset)
        }

        private fun moveCaretToPos(index: Int) {
            myFixture.editor.caretModel.moveToLogicalPosition(myFixture.editor.offsetToLogicalPosition(index))

            val positions = POS_MARKER_REGEX.findAll(myFixture.file.text)
            val pos = positions.find { it.destructured.component1() == index.toString() }
            checkNotNull(pos) { "No position marker with $index index" }
            val size = pos.value.length
            moveCaretTo(pos.range.first + size + 1)
        }

        fun assertReferencedTo(caretIndex: Int, name: String, qualifier: Boolean = true) {
            moveCaretToPos(caretIndex)

            val offset = myFixture.editor.caretModel.offset
            val ref = myFixture.file.findReferenceAt(offset)
            check(ref != null) { "No reference at caret $caretIndex" }

            val resolved = ref.resolve()
            check(resolved != null) { "Cannot resolve reference $caretIndex" }

            if (resolved is PsiDirectory) {
                val expected = "DIRECTORY ${resolved.virtualFile.path}"
                check(name == expected) { "Expected $expected but got $name" }
                return
            }

            if (resolved is VlangPomTargetPsiElement) {
                val expected = "MODULE ${resolved.target.name}"
                check(expected == name) { "Expected to resolve to $name, but got $expected" }
                return
            }

            check(resolved is VlangNamedElement) { "Expected VlangNamedElement but got $resolved" }

            val kind = resolved.toString()
            val resolvedName = if (!qualifier) resolved.name else resolved.getQualifiedName()
            val expected = "$kind $resolvedName"
            check(expected == name) { "Expected to resolve to $name, but got $expected" }
        }

        fun file(name: String, @Language("vlang") content: String) {
            myFixture.configureByText(name, content)
        }

        fun <T : VlangNamedElement> assertSearch(caretIndex: Int, search: QueryExecutorBase<T, DefinitionsScopedSearch.SearchParameters>, vararg results: String) {
            moveCaretToPos(caretIndex)

            val offset = myFixture.editor.caretModel.offset
            val namedElement = myFixture.file.findElementAt(offset)?.parentOfType<VlangNamedElement>()
            check(namedElement != null) { "No named element at caret $caretIndex" }

            val processor = CommonProcessors.CollectProcessor<VlangNamedElement>()
            search.processQuery(VlangGotoUtil.param(namedElement), processor)

            val actual = processor.results.map { it.getQualifiedName() }.naturalSorted()
            val expected = results.toList().naturalSorted()
            check(actual == expected) {
                "Expected to find [${expected.joinToString(", ")}], but got [${actual.joinToString(", ")}]"
            }
        }

        fun <T : VlangNamedElement> assertNoSearchResults(caretIndex: Int, search: QueryExecutorBase<T, DefinitionsScopedSearch.SearchParameters>) {
            moveCaretToPos(caretIndex)

            val offset = myFixture.editor.caretModel.offset
            val structDeclaration = myFixture.file.findElementAt(offset)?.parentOfType<VlangNamedElement>()
            check(structDeclaration != null) { "No named element at caret $caretIndex" }

            val processor = CommonProcessors.CollectProcessor<VlangNamedElement>()
            search.processQuery(VlangGotoUtil.param(structDeclaration), processor)

            check(processor.results.isEmpty()) { "Expected to find no elements, but got [${processor.results.joinToString(", ")}]" }
        }

        companion object {
            private val POS_MARKER_REGEX = "/\\*caret (\\d)\\*/".toRegex()
        }
    }

    fun doTest(cb: IntegrationTestBaseContext.() -> Unit) {
        val context = IntegrationTestBaseContext(myFixture)
        context.cb()
    }
}
