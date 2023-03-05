package org.vlang.ide.refactoring.rename

import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.util.parentOfType
import com.intellij.refactoring.rename.RenamePsiElementProcessor
import com.intellij.util.containers.MultiMap
import org.vlang.lang.psi.*
import org.vlang.lang.search.*

class VlangRenameProcessor : RenamePsiElementProcessor() {
    override fun canProcessElement(element: PsiElement): Boolean = element is VlangNamedElement

    override fun findExistingNameConflicts(element: PsiElement, newName: String, conflicts: MultiMap<PsiElement, String>) {
        val function = element.parentOfType<VlangFunctionOrMethodDeclaration>() ?: return
        function.processDeclarations({ e, _ ->
            if (e != element && e is VlangNamedElement && e.name == newName) {
                val kind = if (e is VlangVarDefinition) "Variable" else if (e is VlangParamDefinition) "Parameter" else "Element"
                conflicts.putValue(e, "$kind '$newName' is already declared in function '${function.name}'")
            }
            true
        }, ResolveState.initial(), null, element)
    }

    override fun prepareRenaming(element: PsiElement, newName: String, allRenames: MutableMap<PsiElement, String>) {
        super.prepareRenaming(element, newName, allRenames)

        // We need to rename all super methods as well as all implementations of this super method
        if (element is VlangMethodDeclaration) {
            val param = VlangGotoUtil.param(element)
            VlangSuperMethodSearch.execute(param) { superMethod ->
                allRenames[superMethod] = newName

                val interfaceMethodParam = VlangGotoUtil.param(superMethod)
                VlangMethodInheritorsSearch.execute(interfaceMethodParam) { childrenMethod ->
                    allRenames[childrenMethod] = newName
                    true
                }

                true
            }
        }

        // We need to rename all implementations of this interface method
        if (element is VlangInterfaceMethodDefinition) {
            val param = VlangGotoUtil.param(element)
            VlangMethodInheritorsSearch.execute(param) { childrenMethod ->
                allRenames[childrenMethod] = newName
                true
            }
        }

        if (element is VlangFieldDefinition) {
            val owner = element.getOwner()
            // We need to rename all super fields as well as all implementations of this super field
            if (owner is VlangStructDeclaration) {
                val param = VlangGotoUtil.param(element)
                VlangSuperFieldSearch.execute(param) { superField ->
                    allRenames[superField] = newName

                    val interfaceFieldParam = VlangGotoUtil.param(superField)
                    VlangFieldInheritorsSearch.execute(interfaceFieldParam) { childrenField ->
                        allRenames[childrenField] = newName
                        true
                    }

                    true
                }
            }

            // We need to rename all implementations of interface field
            if (owner is VlangInterfaceDeclaration) {
                val param = VlangGotoUtil.param(element)
                VlangFieldInheritorsSearch.execute(param) { childrenField ->
                    allRenames[childrenField] = newName
                    true
                }
            }
        }

        if (element is VlangModuleClause) {
            val directory = element.containingFile?.containingDirectory ?: return
            allRenames[directory] = newName
        }
    }
}
