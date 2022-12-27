package org.vlang.lang.search

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.IdeActions
import com.intellij.openapi.progress.ProgressManager
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.util.CommonProcessors
import org.vlang.lang.VlangTypes
import org.vlang.lang.psi.*
import java.awt.event.MouseEvent

class VlangSuperMarkerProvider : LineMarkerProviderDescriptor() {
    override fun getName() = "Go to Interfaces"

    override fun getIcon() = GO_TO_SUPER_ICON

    override fun isEnabledByDefault() = true

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? = null

    override fun collectSlowLineMarkers(elements: List<PsiElement>, result: MutableCollection<in LineMarkerInfo<*>?>) {
        for (element in elements) {
            ProgressManager.checkCanceled()

            if (element !is LeafPsiElement || element.elementType != VlangTypes.IDENTIFIER) continue

            val parent = element.parent
            val grand = parent.parent
            if (grand is VlangStructDeclaration) {
                if (hasSuperType(grand)) {
                    result.add(
                        VlangGotoUtil.createInfo(
                            element,
                            { e, identifier -> showSuperTypePopup(e, identifier) },
                            "Go to Interfaces",
                            GO_TO_SUPER_ICON,
                            IdeActions.ACTION_GOTO_SUPER
                        )
                    )
                }
            }

            if (parent is VlangMethodName) {
                val grandDecl = grand as VlangMethodDeclaration
                if (hasSuperMethod(grandDecl)) {
                    result.add(
                        VlangGotoUtil.createInfo(
                            element,
                            { e, identifier -> showSuperMethodPopup(e, identifier) },
                            "Go to Method Specifications",
                            GO_TO_SUPER_ICON,
                            IdeActions.ACTION_GOTO_SUPER,
                        )
                    )
                }
            }

            if (parent is VlangFieldDefinition) {
                val owner = parent.getOwner()
                if (owner is VlangStructDeclaration && hasSuperField(parent)) {
                    result.add(
                        VlangGotoUtil.createInfo(
                            element,
                            { e, identifier -> showSuperFieldPopup(e, identifier) },
                            "Go to Field Specifications",
                            GO_TO_SUPER_ICON,
                            IdeActions.ACTION_GOTO_SUPER,
                        )
                    )
                }
            }
        }
    }

    companion object {
        val GO_TO_SUPER_ICON = AllIcons.Gutter.ImplementingMethod

        private fun showSuperTypePopup(e: MouseEvent?, identifier: PsiElement) {
            val parent = identifier.parent ?: return
            val struct = parent.parent as? VlangStructDeclaration ?: return
            VlangGotoSuperHandler.showPopup(e, struct)
        }

        private fun showSuperMethodPopup(e: MouseEvent?, identifier: PsiElement) {
            val methodName = identifier.parent as? VlangMethodName ?: return
            val method = methodName.parent as? VlangMethodDeclaration ?: return
            VlangGotoSuperHandler.showPopup(e, method)
        }

        private fun showSuperFieldPopup(e: MouseEvent?, identifier: PsiElement) {
            val field = identifier.parent as? VlangFieldDefinition ?: return
            VlangGotoSuperHandler.showPopup(e, field)
        }

        private fun hasSuperType(spec: VlangNamedElement): Boolean {
            val processor = CommonProcessors.FindFirstProcessor<VlangNamedElement>()
            VlangSuperSearch.processQuery(VlangGotoUtil.param(spec), processor)
            return processor.isFound
        }

        fun hasSuperMethod(method: VlangMethodDeclaration): Boolean {
            val processor = CommonProcessors.FindFirstProcessor<VlangInterfaceMethodDefinition>()
            VlangSuperMethodSearch.processQuery(VlangGotoUtil.param(method), processor)
            return processor.isFound
        }

        fun hasSuperField(field: VlangFieldDefinition): Boolean {
            val processor = CommonProcessors.FindFirstProcessor<VlangFieldDefinition>()
            VlangSuperFieldSearch.processQuery(VlangGotoUtil.param(field), processor)
            return processor.isFound
        }
    }
}
