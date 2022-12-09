package org.vlang.lang.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.ResolveState
import org.vlang.configurations.VlangConfiguration
import org.vlang.lang.psi.VlangAttributeIdentifier
import org.vlang.lang.psi.VlangFile

class VlangAttributeReference(element: VlangAttributeIdentifier) : VlangCachedReference<VlangAttributeIdentifier>(element) {
    override fun resolveInner(): PsiElement? {
        val p = VlangVarProcessor(myElement, false)
        processResolveVariants(p)
        return p.getResult()
    }

    override fun processResolveVariants(processor: VlangScopeProcessor): Boolean {
        val name = myElement.text
        if (name.isBlank()) {
            return false
        }

        val structName = convertSnakeCaseToPascalCase(name)
        val fileName = "$structName.v"

        val stubsDir = VlangConfiguration.getInstance(myElement.project).stubsLocation ?: return true
        val psiManager = PsiManager.getInstance(myElement.project)
        val attributesDir = stubsDir.findChild("attributes") ?: return true
        val virtualFile = attributesDir.findChild(fileName) ?: return true
        val psiFile = psiManager.findFile(virtualFile) as? VlangFile ?: return true

        val struct = psiFile.getStructs().find { it.name == structName } ?: return true

        return processor.execute(struct, ResolveState.initial().put(VlangReferenceBase.SEARCH_NAME, name))
    }

    companion object {
        fun convertSnakeCaseToPascalCase(name: String): String {
            val parts = name.split("_")
            return parts[0].replaceFirstChar { char -> char.titlecaseChar() } + parts.subList(1, parts.size)
                .joinToString("") { it.replaceFirstChar { char -> char.titlecaseChar() } }
        }

        fun convertPascalCaseToSnakeCase(name: String): String {
            return name.replace("([A-Z])".toRegex()) { "_${it.value}" }.trim('_').lowercase()
        }
    }
}
