package org.vlang.ide.codeInsight

import org.vlang.lang.psi.VlangCompositeElement
import org.vlang.lang.psi.VlangFile
import org.vlang.lang.psi.VlangNamedElement

object VlangCodeInsightUtil {
    private const val BUILTIN_MODULE = "builtin"

    fun insideBuiltinModule(element: VlangCompositeElement): Boolean {
        val file = element.containingFile as VlangFile
        return file.getModuleQualifiedName() == BUILTIN_MODULE
    }

    fun insideTranslatedFile(element: VlangCompositeElement): Boolean {
        val file = element.containingFile as VlangFile
        return file.isTranslatedFile()
    }

    fun nonVlangName(name: String): Boolean {
        return name.startsWith("JS.") || name.startsWith("C.")
    }

    fun getQualifiedName(context: VlangCompositeElement, element: VlangNamedElement): String {
        val name = element.getQualifiedName() ?: return element.name ?: ""

        val contextFile = context.containingFile as VlangFile
        val contextModule = contextFile.getModuleQualifiedName()

        val elementFile = element.containingFile as VlangFile
        val elementModule = elementFile.getModuleQualifiedName()

        if (contextModule == elementModule) {
            return element.name ?: ""
        }

        if (name.startsWith("$contextModule.")) {
            return name.removePrefix("$contextModule.")
        }

        if (name.count { it == '.' } == 1) {
            if (name.startsWith("$BUILTIN_MODULE.")) {
                return name.removePrefix("$BUILTIN_MODULE.")
            }
            return name
        }

        val parts = name.split('.')
        return parts[parts.size - 2] + "." + parts[parts.size - 1]
    }

    fun sameModule(first: VlangCompositeElement, second: VlangCompositeElement): Boolean {
        val firstFile = first.containingFile as VlangFile
        val firstModule = firstFile.getModuleQualifiedName()

        val secondFile = second.containingFile as VlangFile
        val secondModule = secondFile.getModuleQualifiedName()

        return firstModule == secondModule
    }
}
