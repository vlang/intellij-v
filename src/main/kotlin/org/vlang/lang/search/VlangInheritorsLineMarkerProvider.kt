package org.vlang.lang.search

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor
import com.intellij.codeInsight.daemon.NavigateAction
import com.intellij.codeInsight.daemon.impl.PsiElementListNavigator
import com.intellij.codeInsight.navigation.GotoImplementationHandler
import com.intellij.icons.AllIcons
import com.intellij.ide.util.DefaultPsiElementCellRenderer
import com.intellij.ide.util.PsiElementListCellRenderer
import com.intellij.openapi.actionSystem.IdeActions
import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.openapi.progress.ProgressManager
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.search.PsiElementProcessor
import com.intellij.psi.search.searches.DefinitionsScopedSearch
import com.intellij.psi.util.parentOfType
import com.intellij.util.CommonProcessors
import com.intellij.util.FunctionUtil
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.*
import java.awt.event.MouseEvent
import javax.swing.Icon
import javax.swing.JComponent

class VlangInheritorsLineMarkerProvider : LineMarkerProviderDescriptor() {
    override fun getName() = "Go to Implementations"

    override fun getIcon() = AllIcons.Gutter.ImplementedMethod

    override fun getLineMarkerInfo(element: PsiElement) = null

    override fun collectSlowLineMarkers(
        elements: List<PsiElement>,
        result: MutableCollection<in LineMarkerInfo<*>>,
    ) {
        val allSpecs = ReferenceOpenHashSet<VlangNamedElement?>()
        val specsWithImplementations = ReferenceOpenHashSet<VlangNamedElement?>()
        for (o in elements) {
            ProgressManager.checkCanceled()

            if (o is LeafPsiElement && o.elementType === VlangTypes.IDENTIFIER) {
                val parent = o.getParent()

                if (parent is VlangInterfaceType) {
                    val decl = parent.parent as VlangInterfaceDeclaration
                    allSpecs.add(decl)

                    if (hasImplementations(decl, VlangInheritorsSearch())) {
                        specsWithImplementations.add(decl)
                        result.add(createTypeSpecInfo(o, decl))
                    }
                }

                if (parent is VlangInterfaceMethodDefinition) {
                    val parentInterface = parent.parentOfType<VlangInterfaceDeclaration>()
                    if (specsWithImplementations.contains(parentInterface) && hasImplementations(parent, VlangMethodInheritorsSearch())) {
                        specsWithImplementations.add(parentInterface)
                        result.add(createMethodSpecInfo(o, parent))
                    }
                    allSpecs.add(parentInterface)
                }

                if (parent is VlangFieldDefinition) {
                    val parentInterface = parent.parentOfType<VlangInterfaceDeclaration>()
                    if (specsWithImplementations.contains(parentInterface) && hasImplementations(parent, VlangFieldInheritorsSearch())) {
                        specsWithImplementations.add(parentInterface)
                        result.add(createFieldSpecInfo(o, parent))
                    }
                    allSpecs.add(parentInterface)
                }
            }
        }
    }

    private fun <T : PsiElement> hasImplementations(
        element: T,
        search: QueryExecutorBase<T, DefinitionsScopedSearch.SearchParameters>,
    ): Boolean {
        val processor = CommonProcessors.FindFirstProcessor<T>()
        search.processQuery(DefinitionsScopedSearch.SearchParameters(element), processor)
        return processor.isFound
    }

    private fun createTypeSpecInfo(anchor: PsiElement, spec: VlangNamedElement): LineMarkerInfo<PsiElement> {
        val name = spec.name ?: "anon"
        return createInfo(anchor, "Go to Implementations", AllIcons.Gutter.ImplementedMethod) { event, element ->
            val named = element.parent.parent as? VlangNamedElement ?: return@createInfo

            showPopup(
                "Implementations of $name",
                { size -> "Type $name implemented in $size types"},
                event,
                DefinitionsScopedSearch.SearchParameters(named),
                getDefaultRenderer(named),
                VlangInheritorsSearch(),
            )
        }
    }

    private fun createMethodSpecInfo(anchor: PsiElement, spec: VlangInterfaceMethodDefinition): LineMarkerInfo<PsiElement> {
        val name = spec.name ?: "anon"
        return createInfo(anchor, "Go to Implementations", AllIcons.Gutter.ImplementedMethod) { event, element ->
            val named = element.parent as? VlangNamedElement ?: return@createInfo

            showPopup(
                "Implementations of $name",
                { size -> "Method $name implemented in $size types"},
                event,
                DefinitionsScopedSearch.SearchParameters(named),
                getDefaultRenderer(named),
                VlangMethodInheritorsSearch(),
            )
        }
    }

    private fun createFieldSpecInfo(anchor: PsiElement, spec: VlangFieldDefinition): LineMarkerInfo<PsiElement> {
        val name = spec.name ?: "anon"
        return createInfo(anchor, "Go to Implementations", AllIcons.Gutter.ImplementedMethod) { event, element ->
            val named = element.parent as? VlangNamedElement ?: return@createInfo

            showPopup(
                "Implementations of $name",
                { size -> "Field $name implemented in $size types"},
                event,
                DefinitionsScopedSearch.SearchParameters(named),
                getDefaultRenderer(named),
                VlangFieldInheritorsSearch(),
            )
        }
    }

    private fun getDefaultRenderer(context: PsiElement?): DefaultPsiElementCellRenderer {
        return if (context == null) DefaultPsiElementCellRenderer() else object : DefaultPsiElementCellRenderer() {
            override fun getComparator(): Comparator<PsiElement> {
                return GotoImplementationHandler.projectElementsFirst(context.project).thenComparing(super.getComparator())
            }
        }
    }

    private fun <T : PsiElement> showPopup(
        findUsagesTitle: String,
        popupTitleCb: (Int) -> String,
        event: MouseEvent?,
        param: DefinitionsScopedSearch.SearchParameters,
        renderer: PsiElementListCellRenderer<PsiElement>,
        search: QueryExecutorBase<T, in DefinitionsScopedSearch.SearchParameters>,
    ) {
        val collector = PsiElementProcessor.CollectElementsWithLimit(2, ReferenceOpenHashSet())
        val progressTitle = "Searching for $findUsagesTitle..."
        val component = event?.component as? JComponent

        if (!ProgressManager.getInstance().runProcessWithProgressSynchronously({
                search.execute(param) {
                    collector.execute(it)
                }
            }, progressTitle, true, param.project, component)) {
            return
        }
        val elements = collector.collection
        val popupTitle = popupTitleCb(elements.size)
        val targets = elements.filterIsInstance<NavigatablePsiElement>().toTypedArray()
        if (event != null) {
            PsiElementListNavigator.openTargets(event, targets, popupTitle, findUsagesTitle, renderer, null)
        }
    }

    private fun <T : PsiElement> createInfo(
        element: T,
        text: String,
        icon: Icon,
        handler: GutterIconNavigationHandler<T>,
    ): LineMarkerInfo<T> {
        val tooltip = "" // TODO

        val info = LineMarkerInfo(
            element, element.textRange, icon,
            FunctionUtil.constant(tooltip),
            handler,
            GutterIconRenderer.Alignment.RIGHT
        ) { tooltip }

        return NavigateAction.setNavigateAction(info, text, IdeActions.ACTION_GOTO_IMPLEMENTATION)
    }
}
