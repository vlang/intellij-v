package org.vlang.documentation

import com.intellij.codeInsight.documentation.DocumentationManager
import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfTypes
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.util.io.readText
import org.vlang.lang.psi.VlangImportName
import org.vlang.lang.psi.VlangNamedElement
import org.vlang.lang.psi.VlangReferenceExpression
import org.vlang.utils.toPath
import java.io.File
import java.nio.file.Files

@Suppress("DEPRECATION")
abstract class DocumentationTestBase : BasePlatformTestCase() {
    override fun getTestDataPath() = "src/test/resources/documentation"

    fun doTest(dir: String, filename: String) {
        val modDir = "$testDataPath/$dir/mod"
        if (Files.exists(modDir.toPath())) {
            myFixture.copyDirectoryToProject("$dir/mod", "mod")
        }
        val readmeFile = "$testDataPath/$dir/README.md"
        if (Files.exists(readmeFile.toPath())) {
            myFixture.copyFileToProject("$dir/README.md", "README.md")
        }

        val text = "$testDataPath/$dir/$filename".toPath().readText()
        val newText = text.replace(CARET, CARET_ORIGINAL)
        myFixture.configureByText(filename, newText)

        val documentations = mutableListOf<String>()

        val carets = myFixture.editor.caretModel.allCarets
        carets.forEach { caret ->
            val offset = caret.offset
            val originalElement = myFixture.file.findElementAt(offset)
            check(originalElement != null) { "No element at caret" }

            var element: PsiElement? = originalElement.parentOfTypes(VlangNamedElement::class, VlangReferenceExpression::class)
            check(element != null) { "No named element at caret" }

            if (element is VlangReferenceExpression) {
                element = element.resolve() as? VlangNamedElement
            } else if (element is VlangImportName) {
                element = element.resolve().firstOrNull()?.toPsi()
            }
            check(element != null) { "Can't resolve element" }

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

//        file.writeText(generateDoc)
//        return

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
