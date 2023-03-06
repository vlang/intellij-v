package org.vlang.ide.inspections.namingConventions

import com.intellij.DynamicBundle
import com.intellij.codeInsight.daemon.impl.quickfix.RenameElementFix
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import com.intellij.util.containers.ContainerUtil
import org.vlang.ide.refactoring.rename.VlangRenameReceiverProcessor
import org.vlang.lang.psi.VlangMethodDeclaration
import org.vlang.lang.psi.VlangReceiver
import org.vlang.lang.psi.VlangStructDeclaration
import org.vlang.lang.psi.VlangVisitor
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.unwrapPointer
import org.vlang.lang.psi.types.VlangResolvableTypeEx
import org.vlang.lang.psi.types.VlangTypeEx
import java.util.*
import java.util.function.Function

class VlangReceiverNamesInspection : VlangNamingConventionInspectionBase() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitReceiver(receiver: VlangReceiver) {
                val name = receiver.name
                if (name in GENERIC_NAMES) {
                    holder.registerProblem(receiver.getIdentifier(), "Receiver has a generic name")
                }
            }

            override fun visitMethodDeclaration(method: VlangMethodDeclaration) {
                val containingFile = method.containingFile
                val type = method.receiver.getType(null)?.unwrapPointer() as? VlangResolvableTypeEx<*> ?: return
                val resolved = type.resolve(method.project) ?: return
                // skip method if it's from the same file as the struct/interface
                if (resolved.containingFile.isEquivalentTo(containingFile)) return

                val (_, hasMultipleNames) = hasMultipleReceiverNames(method.project, type)
                if (hasMultipleNames) {
                    registerProblem(method)
                }
            }

            override fun visitStructDeclaration(struct: VlangStructDeclaration) {
                val containingFile = struct.containingFile
                val type = struct.structType.toEx()
                val (methods, hasMultipleNames) = hasMultipleReceiverNames(struct.project, type)
                if (hasMultipleNames) {
                    methods
                        .filter { it.isValid && it.containingFile.isEquivalentTo(containingFile) }
                        .forEach { registerProblem(it) }
                }
            }

            private fun registerProblem(method: VlangMethodDeclaration) {
                val receiver = method.receiver
                val identifier = receiver.getIdentifier()
                holder.registerProblem(
                    identifier, "Receiver names are different",
                    RenameAllReceiversFix(receiver, identifier.text)
                )
            }

            private fun hasMultipleReceiverNames(
                project: Project,
                type: VlangTypeEx,
            ): Pair<List<VlangMethodDeclaration>, Boolean> {
                val methods = type.ownMethodsList(project)
                val receiverNames = methods.mapNotNull { it.receiver.name }.filter { it != "_" }
                val hasMultipleNames = receiverNames.distinct().size > 1
                return Pair(methods, hasMultipleNames)
            }
        }
    }

    companion object {
        val GENERIC_NAMES = listOf("me", "this", "self")

        private class RenameAllReceiversFix(receiver: VlangReceiver, newName: String) :
            RenameElementFix(receiver, newName, fromCache(newName) { "Rename all receivers to '$it'" }) {

            override fun getFamilyName(): String = "Rename all receivers"

            override fun invoke(project: Project, file: PsiFile, editor: Editor?, startElement: PsiElement, endElement: PsiElement) {
                VlangRenameReceiverProcessor.renameAllReceivers()
                super.invoke(project, file, editor, startElement, endElement)
            }

            override fun invoke(project: Project, file: PsiFile, startElement: PsiElement, endElement: PsiElement) {
                VlangRenameReceiverProcessor.renameAllReceivers()
                super.invoke(project, file, startElement, endElement)
            }

            companion object {
                private val TEXTS = ContainerUtil.createWeakKeyWeakValueMap<Locale, MutableMap<String, String>>()

                private fun fromCache(newName: String, textProducer: Function<String, String>): String {
                    return TEXTS.computeIfAbsent(DynamicBundle.getLocale()) { ContainerUtil.createWeakKeyWeakValueMap<String, String>() }
                        .computeIfAbsent(newName, textProducer)
                }
            }
        }
    }
}
