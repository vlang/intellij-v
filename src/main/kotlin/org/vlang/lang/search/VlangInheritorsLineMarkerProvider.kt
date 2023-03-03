package org.vlang.lang.search

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.IdeActions
import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.openapi.progress.ProgressManager
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.search.searches.DefinitionsScopedSearch
import com.intellij.util.CommonProcessors
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.*
import java.awt.event.MouseEvent

class VlangInheritorsLineMarkerProvider : LineMarkerProviderDescriptor() {
    override fun getName() = "Go to Implementations"

    override fun getIcon() = AllIcons.Gutter.ImplementedMethod

    override fun getLineMarkerInfo(element: PsiElement) = null

    override fun collectSlowLineMarkers(elements: List<PsiElement>, result: MutableCollection<in LineMarkerInfo<*>>) {
        val allTypes = ReferenceOpenHashSet<VlangNamedElement?>()
        val specsWithImplementations = ReferenceOpenHashSet<VlangNamedElement?>()

        for (element in elements) {
            ProgressManager.checkCanceled()

            if (element !is LeafPsiElement || element.elementType != VlangTypes.IDENTIFIER) continue
            val parent = element.getParent()

            if (parent is VlangInterfaceType) {
                val iface = parent.parent as? VlangInterfaceDeclaration ?: continue
                if (hasImplementations(iface, VlangInheritorsSearch)) {
                    specsWithImplementations.add(iface)
                    result.add(createTypeSpecInfo(element, iface))
                }
                allTypes.add(iface)
            }

            if (parent is VlangInterfaceMethodDefinition) {
                val iface = parent.getOwner()
                if (specsWithImplementations.contains(iface) && hasImplementations(parent, VlangMethodInheritorsSearch)) {
                    specsWithImplementations.add(iface)
                    result.add(createMethodSpecInfo(element, parent))
                }
                allTypes.add(iface)
            }

            if (parent is VlangFieldDefinition) {
                val iface = parent.getOwner() as? VlangInterfaceDeclaration ?: continue
                if (specsWithImplementations.contains(iface) && hasImplementations(parent, VlangFieldInheritorsSearch)) {
                    specsWithImplementations.add(iface)
                    result.add(createFieldSpecInfo(element, parent))
                }
                allTypes.add(iface)
            }
        }
    }

    private fun createTypeSpecInfo(anchor: PsiElement, spec: VlangNamedElement): LineMarkerInfo<PsiElement> {
        val name = spec.name ?: "anon"
        return VlangGotoUtil.createInfo(anchor, { event, element ->
            val named = element.parent.parent as? VlangNamedElement ?: return@createInfo

            showImplementationPopup(name, event, named)
        }, "Go to Implementations", AllIcons.Gutter.ImplementedMethod, IdeActions.ACTION_GOTO_IMPLEMENTATION)
    }

    private fun createMethodSpecInfo(anchor: PsiElement, spec: VlangInterfaceMethodDefinition): LineMarkerInfo<PsiElement> {
        val name = spec.name ?: "anon"
        return VlangGotoUtil.createInfo(anchor, { event, element ->
            val named = element.parent as? VlangNamedElement ?: return@createInfo

            VlangGotoUtil.showPopup(
                "Implementations of $name",
                { size -> "Method '$name' implemented in $size types" },
                event,
                VlangGotoUtil.param(named),
                VlangGotoUtil.getMethodRenderer(named),
                VlangMethodInheritorsSearch,
            )
        }, "Go to Implementations", AllIcons.Gutter.ImplementedMethod, IdeActions.ACTION_GOTO_IMPLEMENTATION)
    }

    private fun createFieldSpecInfo(anchor: PsiElement, spec: VlangFieldDefinition): LineMarkerInfo<PsiElement> {
        val name = spec.name ?: "anon"
        return VlangGotoUtil.createInfo(anchor, { event, element ->
            val named = element.parent as? VlangNamedElement ?: return@createInfo

            VlangGotoUtil.showPopup(
                "Implementations of $name",
                { size -> "Field '$name' implemented in $size types" },
                event,
                VlangGotoUtil.param(named),
                VlangGotoUtil.getFieldRenderer(named),
                VlangFieldInheritorsSearch,
            )
        }, "Go to Implementations", AllIcons.Gutter.ImplementedMethod, IdeActions.ACTION_GOTO_IMPLEMENTATION)
    }

    companion object {
        fun <T : PsiElement> hasImplementations(
            element: T,
            search: QueryExecutorBase<T, DefinitionsScopedSearch.SearchParameters>,
        ): Boolean {
            val processor = CommonProcessors.FindFirstProcessor<T>()
            search.processQuery(VlangGotoUtil.param(element), processor)
            return processor.isFound
        }

        fun showImplementationPopup(
            name: String,
            event: MouseEvent?,
            named: VlangNamedElement,
        ) {
            VlangGotoUtil.showPopup(
                "Implementations of $name",
                { size -> "Type '$name' implemented in $size types" },
                event,
                VlangGotoUtil.param(named),
                VlangGotoUtil.getDefaultRenderer(named),
                VlangInheritorsSearch,
            )
        }
    }
}
