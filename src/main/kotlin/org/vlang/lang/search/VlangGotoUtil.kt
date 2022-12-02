package org.vlang.lang.search

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.NavigateAction
import com.intellij.codeInsight.daemon.impl.PsiElementListNavigator
import com.intellij.codeInsight.navigation.GotoImplementationHandler
import com.intellij.ide.util.DefaultPsiElementCellRenderer
import com.intellij.ide.util.PsiElementListCellRenderer
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.openapi.keymap.KeymapUtil
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Condition
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.ProjectScope
import com.intellij.psi.search.PsiElementProcessor
import com.intellij.psi.search.searches.DefinitionsScopedSearch
import com.intellij.util.FunctionUtil
import com.intellij.util.Processor
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet
import org.vlang.lang.psi.VlangFieldDefinition
import org.vlang.lang.psi.VlangMethodDeclaration
import org.vlang.lang.psi.VlangNamedElement
import java.awt.event.MouseEvent
import javax.swing.Icon
import javax.swing.JComponent

object VlangGotoUtil {
    fun <T : PsiElement> createInfo(
        element: T,
        navigationHandler: GutterIconNavigationHandler<T>,
        text: String,
        icon: Icon,
        actionName: String,
    ): LineMarkerInfo<T> {
        val tooltip = buildTooltipText(text, actionName)

        val info = LineMarkerInfo(
            element, element.textRange, icon,
            FunctionUtil.constant(tooltip),
            navigationHandler,
            GutterIconRenderer.Alignment.RIGHT
        ) { tooltip }

        return NavigateAction.setNavigateAction(info, text, actionName)
    }

    fun <T : PsiElement> showPopup(
        findUsagesTitle: String,
        popupTitleCb: (Int) -> String,
        event: MouseEvent?,
        param: DefinitionsScopedSearch.SearchParameters,
        renderer: PsiElementListCellRenderer<PsiElement>,
        search: QueryExecutorBase<T, in DefinitionsScopedSearch.SearchParameters>,
    ) {
        val collector = PsiElementProcessor.CollectElementsWithLimit(5, ReferenceOpenHashSet())
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

    fun getDefaultRenderer(context: PsiElement?): DefaultPsiElementCellRenderer {
        return if (context == null) DefaultPsiElementCellRenderer() else object : DefaultPsiElementCellRenderer() {
            override fun getComparator(): Comparator<PsiElement> {
                return GotoImplementationHandler.projectElementsFirst(context.project).thenComparing(super.getComparator())
            }
        }
    }

    fun getMethodRenderer(context: PsiElement?): DefaultPsiElementCellRenderer {
        return if (context == null) DefaultPsiElementCellRenderer() else object : DefaultPsiElementCellRenderer() {
            override fun getComparator(): Comparator<PsiElement> {
                return GotoImplementationHandler.projectElementsFirst(context.project).thenComparing(super.getComparator())
            }

            override fun getElementText(element: PsiElement?): String {
                val method = element as? VlangMethodDeclaration
                return method?.receiverType?.text ?: super.getElementText(element)
            }
        }
    }

    fun getFieldRenderer(context: PsiElement?): DefaultPsiElementCellRenderer {
        return if (context == null) DefaultPsiElementCellRenderer() else object : DefaultPsiElementCellRenderer() {
            override fun getComparator(): Comparator<PsiElement> {
                return GotoImplementationHandler.projectElementsFirst(context.project).thenComparing(super.getComparator())
            }

            override fun getElementText(element: PsiElement?): String {
                val field = element as? VlangFieldDefinition
                return field?.getOwner()?.name ?: super.getElementText(element)
            }
        }
    }

    fun searchInContentFirst(
        project: Project,
        scope: GlobalSearchScope,
        searcher: Processor<in GlobalSearchScope>,
    ) {
        val projectContentScope = ProjectScope.getContentScope(project)
        if (!searcher.process(scope.intersectWith(projectContentScope))) {
            return
        }
        searcher.process(scope.intersectWith(GlobalSearchScope.notScope(projectContentScope)))
    }

    fun createCandidatesProcessor(
        processor: Processor<in VlangNamedElement>,
        condition: Condition<VlangNamedElement>,
    ): Processor<VlangNamedElement?> {
        val visitedSpecs = ReferenceOpenHashSet<VlangNamedElement>()

        return Processor<VlangNamedElement?> { candidate ->
            ProgressManager.checkCanceled()
            if (!visitedSpecs.add(candidate)) return@Processor true
            if (condition.value(candidate) && !processor.process(candidate)) return@Processor false

            true
        }
    }

    fun param(element: PsiElement): DefinitionsScopedSearch.SearchParameters {
        return DefinitionsScopedSearch.SearchParameters(element)
    }

    private fun buildTooltipText(title: String, actionId: String): String {
        val shortcut = ActionManager.getInstance().getAction(actionId).shortcutSet.shortcuts.firstOrNull()
        val shortcutText = if (shortcut == null) "" else " or press ${KeymapUtil.getShortcutText(shortcut)}"
        return """
            <html>$title<br>
                <div style='margin-top: 5px'>
                    <font size='2'>Click $shortcutText to navigate</font>
                </div
            ></html>
        """.trimIndent()
    }
}
