package org.vlang.integration

import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.Lookup
import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.ui.naturalSorted
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.search.searches.DefinitionsScopedSearch
import com.intellij.psi.util.parentOfType
import com.intellij.refactoring.BaseRefactoringProcessor
import com.intellij.testFramework.TestModeFlags
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import com.intellij.util.CommonProcessors
import org.intellij.lang.annotations.Language
import org.vlang.ide.refactoring.VlangImplementMethodsHandler
import org.vlang.lang.psi.VlangInterfaceDeclaration
import org.vlang.lang.psi.VlangNamedElement
import org.vlang.lang.psi.impl.VlangModule
import org.vlang.lang.search.VlangGotoUtil
import org.vlang.lang.stubs.index.VlangNamesIndex

abstract class IntegrationTestBase : BasePlatformTestCase() {
    override fun getTestDataPath() = "src/test/resources/integration"

    class CompletionContext(private val myFixture: CodeInsightTestFixture) {
        fun equalsTo(vararg variants: String) {
            val elements = myFixture.lookupElementStrings ?: mutableListOf()
            assertSameElements(elements, *variants)
        }

        fun includes(vararg variants: String) {
            val elements = myFixture.lookupElementStrings ?: mutableListOf()

            check(elements.containsAll(variants.toList())) {
                """
                    Actual doesn't contain all of the expected variants.
                    
                    Expected: ${variants.toList()}
                    Actual: $elements
                """.trimIndent()
            }
        }

        fun excludes(vararg variants: String) {
            val elements = myFixture.lookupElementStrings ?: mutableListOf()

            check(!elements.containsAll(variants.toList())) {
                """
                    Actual contains all of the excluded variants.
                    
                    Excluded: ${variants.toList()}
                    Actual: $elements
                """.trimIndent()
            }
        }
    }

    class DirectoryContext(private val myFixture: CodeInsightTestFixture, val directory: VirtualFile) {
        fun file(name: String, @Language("vlang") text: String) {
            myFixture.addFileToProject("${directory.path}/$name", text)
            myFixture.configureFromTempProjectFile("${directory.path}/$name")
        }

        fun directory(name: String, action: DirectoryContext.() -> Unit) {
            val dir = WriteAction.compute<VirtualFile, RuntimeException> { directory.createChildDirectory(this, name) }
            val context = DirectoryContext(myFixture, dir)
            context.action()
        }
    }

    class IntegrationTestBaseContext(private val myFixture: CodeInsightTestFixture) {
        private fun moveCaretTo(offset: Int) {
            myFixture.editor.caretModel.moveToOffset(offset)
        }

        fun moveCaretToPos(index: Int, before: Boolean = false) {
            myFixture.editor.caretModel.moveToLogicalPosition(myFixture.editor.offsetToLogicalPosition(index))

            val positions = POS_MARKER_REGEX.findAll(myFixture.file.text)
            val pos = positions.find { it.destructured.component1() == index.toString() }
            checkNotNull(pos) { "No position marker with $index index" }
            val size = pos.value.length
            if (before) {
                moveCaretTo(pos.range.first - 1)
                return
            }
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

            if (resolved is VlangModule.VlangPomTargetPsiElement) {
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

        fun directory(name: String, action: DirectoryContext.() -> Unit) {
            val root = myFixture.project.guessProjectDir()!!
            val dir = WriteAction.compute<VirtualFile, RuntimeException> { root.createChildDirectory(this, name) }
            val context = DirectoryContext(myFixture, dir)
            context.action()
        }

        fun file(name: String, @Language("vlang") content: String) {
            myFixture.configureByText(name, content)
        }

        fun <T : VlangNamedElement> assertSearch(
            caretIndex: Int,
            search: QueryExecutorBase<T, DefinitionsScopedSearch.SearchParameters>,
            vararg results: String,
        ) {
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

        fun <T : VlangNamedElement> assertNoSearchResults(
            caretIndex: Int,
            search: QueryExecutorBase<T, DefinitionsScopedSearch.SearchParameters>,
        ) {
            moveCaretToPos(caretIndex)

            val offset = myFixture.editor.caretModel.offset
            val structDeclaration = myFixture.file.findElementAt(offset)?.parentOfType<VlangNamedElement>()
            check(structDeclaration != null) { "No named element at caret $caretIndex" }

            val processor = CommonProcessors.CollectProcessor<VlangNamedElement>()
            search.processQuery(VlangGotoUtil.param(structDeclaration), processor)

            check(processor.results.isEmpty()) { "Expected to find no elements, but got [${processor.results.joinToString(", ")}]" }
        }

        fun renameElement(caretIndex: Int, newName: String) {
            moveCaretToPos(caretIndex)

            val offset = myFixture.editor.caretModel.offset
            val element = myFixture.file.findElementAt(offset)?.parentOfType<VlangNamedElement>()
            check(element != null) { "No named element at caret $caretIndex" }

            myFixture.renameElement(element, newName)
        }

        fun renameWithConflicts(
            caretIndex: Int,
            newName: String,
            conflicts: Set<String>,
        ) {
            try {
                renameElement(caretIndex, newName)
                error("Conflicts $conflicts missing")
            } catch (e: BaseRefactoringProcessor.ConflictsInTestsException) {
                assertEquals(e.messages.toSet(), conflicts)
            }
        }

        fun checkFile(name: String, @Language("vlang") content: String) {
            myFixture.checkResult(name, content, true)
        }

        fun completion(caretIndex: Int, action: CompletionContext.() -> Unit) {
            moveCaretToPos(caretIndex, before = true)

            myFixture.complete(CompletionType.BASIC, 1)
            CompletionContext(myFixture).action()
        }

        fun implementInterface(caretIndex: Int, name: String) {
            moveCaretToPos(caretIndex)

            val offset = myFixture.editor.caretModel.offset
            val element = myFixture.file.findElementAt(offset)?.parentOfType<VlangNamedElement>()
            check(element != null) { "No named element at caret $caretIndex" }

            val iface = VlangNamesIndex.find(name, myFixture.project, null).firstOrNull() as? VlangInterfaceDeclaration
                ?: error("No interface $name found")

            TestModeFlags.set(VlangImplementMethodsHandler.TESTING_INTERFACE_TO_IMPLEMENT, iface, myFixture.testRootDisposable)

            VlangImplementMethodsHandler().invoke(myFixture.project, myFixture.editor, myFixture.file)
        }

        fun finishCompletion() {
            myFixture.finishLookup(Lookup.NORMAL_SELECT_CHAR)
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
