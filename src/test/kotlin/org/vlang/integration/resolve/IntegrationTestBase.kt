package org.vlang.integration.resolve

import com.intellij.psi.PsiDirectory
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import org.vlang.configurations.VlangProjectSettingsState.Companion.projectSettings
import org.vlang.lang.psi.VlangNamedElement

abstract class IntegrationTestBase : BasePlatformTestCase() {
    override fun getTestDataPath() = "src/test/resources/integration"

    class IntegrationTestBaseContext(private val myFixture: CodeInsightTestFixture) {
        private fun moveCaretTo(offset: Int) {
            myFixture.editor.caretModel.moveToOffset(offset)
        }

        fun moveCaretToPos(index: Int) {
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

            check(resolved is VlangNamedElement) { "Expected VlangNamedElement but got $resolved" }

            val kind = resolved.toString()
            val resolvedName = if (!qualifier) resolved.name else resolved.getQualifiedName()
            val expected = "$kind $resolvedName"
            check(expected == name) { "Expected to resolve to $name, but got $expected" }
        }

        companion object {
            private val POS_MARKER_REGEX = "/\\*caret (\\d)\\*/".toRegex()
        }
    }

    fun doTest(cb: IntegrationTestBaseContext.() -> Unit) {
        copyStdlibToProject()

        val context = IntegrationTestBaseContext(myFixture)
        context.cb()
    }

    private fun copyStdlibToProject() {
        myFixture.project.projectSettings.customStdlibLocation = myFixture.testDataPath + "/vlib"
        myFixture.project.projectSettings.customModulesLocation = myFixture.testDataPath + "/modules"
    }
}
