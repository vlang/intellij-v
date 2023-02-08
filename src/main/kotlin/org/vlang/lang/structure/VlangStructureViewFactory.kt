package org.vlang.lang.structure

import com.intellij.ide.structureView.*
import com.intellij.ide.structureView.StructureViewModel.ElementInfoProvider
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase
import com.intellij.ide.util.treeView.smartTree.Sorter
import com.intellij.lang.PsiStructureViewFactory
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.IndexNotReadyException
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangLangUtil
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx

class VlangStructureViewFactory : PsiStructureViewFactory {
    override fun getStructureViewBuilder(psiFile: PsiFile): StructureViewBuilder {
        return object : TreeBasedStructureViewBuilder() {
            override fun createStructureViewModel(editor: Editor?): StructureViewModel = Model(psiFile, editor)

            override fun isRootNodeShown() = false
        }
    }

    class Model(file: PsiFile, editor: Editor?) : StructureViewModelBase(file, editor, Element(file)), ElementInfoProvider {
        init {
            withSuitableClasses(VlangFile::class.java, VlangNamedElement::class.java)
                .withSorters(VlangExportabilitySorter.INSTANCE, Sorter.ALPHA_SORTER)
        }

        override fun getFilters() = arrayOf(VlangPrivateMembersFilter())

        override fun isAlwaysShowsPlus(element: StructureViewTreeElement) = false

        override fun isAlwaysLeaf(element: StructureViewTreeElement) = false
    }

    class Element(element: PsiElement) : PsiTreeElementBase<PsiElement>(element) {
        override fun getChildrenBase(): MutableCollection<StructureViewTreeElement> {
            val result = mutableListOf<StructureViewTreeElement>()
            val element = element

            if (element is VlangFile) {
                element.getFunctions().forEach { result.add(Element(it)) }
                element.getStructs().forEach { result.add(Element(it)) }
                element.getInterfaces().forEach { result.add(Element(it)) }
                element.getEnums().forEach { result.add(Element(it)) }
                element.getTypes().forEach { result.add(Element(it)) }
                element.getConstants().forEach { result.add(Element(it)) }
                element.getGlobalVariables().forEach { result.add(Element(it)) }
            }

            val type = when (element) {
                is VlangTypeAliasDeclaration -> element.aliasType
                is VlangStructDeclaration    -> element.structType
                is VlangInterfaceDeclaration -> element.interfaceType
                is VlangEnumDeclaration      -> element.enumType
                else                         -> null
            }?.toEx()

            if (type != null) {
                VlangLangUtil.getMethodList(element!!.project, type).forEach { result.add(Element(it)) }

                if (type is VlangFieldListOwner) {
                    type.fieldList.forEach { result.add(Element(it)) }
                }

                if (type is VlangEnumType) {
                    type.fieldList.forEach { result.add(Element(it)) }
                }
            }

            return result
        }

        override fun getPresentableText(): String? {
            val element = element

            if (element is VlangFile) {
                return element.name
            }

            if (element is VlangNamedElement && element is VlangSignatureOwner) {
                val name = element.name
                val signature = element.getSignature() ?: return null
                return name + signature.text
            }

            if (element is VlangTypeAliasDeclaration) {
                val name = element.name
                val type = element.aliasType?.typeUnionList?.typeList?.firstOrNull()?.toEx()?.readableName(element)
                return "$name = $type"
            }

            if (element is VlangConstDefinition) {
                val type = try {
                    element.getType(null)
                } catch (ignored: IndexNotReadyException) {
                    null
                }
                val typeText = if (type == null || element is VlangEmbeddedDefinition)
                    ""
                else
                    ": " + type.readableName(element)

                return element.name + typeText
            }

            if (element is VlangType) {
                return element.toEx().readableName(element)
            }

            if (element is VlangNamedElement) {
                return element.name
            }

            return null
        }
    }
}
