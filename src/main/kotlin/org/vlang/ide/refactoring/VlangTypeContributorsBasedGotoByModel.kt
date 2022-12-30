package org.vlang.ide.refactoring

import com.intellij.ide.util.gotoByName.ContributorsBasedGotoByModel
import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.psi.PsiElement
import org.jetbrains.annotations.Nls
import org.vlang.ide.navigation.goto.VlangGotoClassLikeContributor
import org.vlang.lang.psi.VlangNamedElement

class VlangTypeContributorsBasedGotoByModel(
    project: Project,
    contributor: VlangGotoClassLikeContributor,
    private val promptText: @Nls(capitalization = Nls.Capitalization.Sentence) String,
) : ContributorsBasedGotoByModel(project, listOf(contributor)), Disposable {

    override fun getPromptText(): String = promptText
    override fun getNotInMessage(): String = ""
    override fun getNotFoundMessage(): String = ""
    override fun getCheckBoxName(): String? = null

    override fun loadInitialCheckBoxState(): Boolean = false
    override fun saveInitialCheckBoxState(state: Boolean) {
    }

    override fun getSeparators(): Array<String> = arrayOf(".")

    override fun getFullName(element: Any): String? {
        if (element is PsiElement && !element.isValid) return null
        if (element is VlangNamedElement) return element.getQualifiedName()
        return getElementName(element)
    }

    override fun willOpenEditor(): Boolean = false

    override fun dispose() {
        for (contributor in contributors) {
            if (contributor is Disposable) {
                Disposer.dispose((contributor as Disposable))
            }
        }
    }
}
