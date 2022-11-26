package org.vlang.documentation

import com.intellij.codeInsight.documentation.DocumentationManager
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.vlang.lang.psi.VlangNamedElement
import org.vlang.utils.parentOfType
import java.io.File

@Suppress("DEPRECATION")
abstract class DocumentationTestBase : BasePlatformTestCase() {
    override fun getTestDataPath() = "src/test/resources/documentation"

    fun doTest(dir: String, filename: String) {
        val file = myFixture.configureByFile("$dir/$filename")
        val newText = file.text.replace(CARET, CARET_ORIGINAL)
        myFixture.configureByText(filename, newText)

        val documentations = mutableListOf<String>()

        val carets = myFixture.editor.caretModel.allCarets
        carets.forEach { caret ->
            val offset = caret.offset
            val originalElement = myFixture.file.findElementAt(offset)
            check(originalElement != null) { "No element at caret" }

            val element = originalElement.parentOfType<VlangNamedElement>()
            check(element != null) { "No named element at caret" }

            val documentationProvider = DocumentationManager.getProviderFromElement(element)
            val generateDoc = documentationProvider.generateDoc(element, originalElement)
            check(generateDoc != null) { "No documentation for element" }

            documentations.add(generateDoc)
        }

        val allDocs = documentations.joinToString(DOCUMENTATION_SEPARATOR)

        checkDoc(allDocs, "$testDataPath/$dir/Doc.expected.html")
    }

    private fun checkDoc(generateDoc: String, fileToCheck: String) {
        val file = File(fileToCheck)

        if (!file.exists()) {
            file.createNewFile()
            file.writeText(generateDoc)
            error("File $fileToCheck not found. Generated doc was written to $file")
        }

        val text = file.readText()
        assertEquals(text, generateDoc)
    }

    companion object {
        const val CARET = "/*caret*/"
        const val CARET_ORIGINAL = "<caret>"
        const val DOCUMENTATION_SEPARATOR = "<br><br><br>"
    }
}
