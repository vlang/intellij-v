package io.vlang.ide.inspections

import com.intellij.codeInspection.*
import com.intellij.psi.PsiElement
import io.vlang.ide.inspections.suppression.VlangInspectionSuppressor

abstract class VlangBaseInspection : LocalInspectionTool(), CustomSuppressableInspectionTool {
    override fun getSuppressActions(element: PsiElement?): Array<SuppressIntentionAction?> {
        return SuppressIntentionActionFromFix.convertBatchToSuppressIntentionActions(
            VlangInspectionSuppressor().getSuppressActions(element, id)
        )
    }
}
