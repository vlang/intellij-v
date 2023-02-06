package org.vlang.lang.annotator

import com.intellij.codeHighlighting.DirtyScopeTrackingHighlightingPassFactory
import com.intellij.codeHighlighting.TextEditorHighlightingPass
import com.intellij.codeHighlighting.TextEditorHighlightingPassFactoryRegistrar
import com.intellij.codeHighlighting.TextEditorHighlightingPassRegistrar
import com.intellij.codeInsight.daemon.impl.FileStatusMap
import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.codeInsight.daemon.impl.UpdateHighlightersUtil
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiRecursiveElementWalkingVisitor
import org.vlang.lang.psi.VlangFile
import java.util.function.Consumer

class VlangHighlightingPassFactory : TextEditorHighlightingPassFactoryRegistrar, DirtyScopeTrackingHighlightingPassFactory, DumbAware {
    private var myPassId = 0

    override fun createHighlightingPass(file: PsiFile, editor: Editor): TextEditorHighlightingPass? {
        if (file is VlangFile) {
            val textRange = FileStatusMap.getDirtyTextRange(editor, passId)
            return if (textRange != null) VlangHighlightingPass(file, editor.document) else null
        }
        return null
    }

    override fun getPassId(): Int {
        return myPassId
    }

    private class VlangHighlightingPass(file: VlangFile, document: Document) :
        TextEditorHighlightingPass(file.project, document, false), DumbAware {
        private val myFile: VlangFile
        private val myVisitor: VlangHighlightInfoCollectingVisitor
        private val myInfos: MutableList<HighlightInfo> = ArrayList()

        init {
            myVisitor = VlangHighlightInfoCollectingVisitor()
            myVisitor.myHighlightInfoSink = Consumer { myInfos.add(it) }
            myFile = file
        }

        override fun doCollectInformation(progress: ProgressIndicator) {
            myInfos.clear()
            myFile.accept(object : PsiRecursiveElementWalkingVisitor() {
                override fun visitElement(element: PsiElement) {
                    element.accept(myVisitor)
                    super.visitElement(element)
                }
            })
        }

        override fun doApplyInformationToEditor() {
            UpdateHighlightersUtil.setHighlightersToEditor(myProject, myDocument, 0, myDocument.textLength, myInfos, null, id)
        }
    }

    override fun registerHighlightingPassFactory(registrar: TextEditorHighlightingPassRegistrar, project: Project) {
        myPassId = registrar.registerTextEditorHighlightingPass(this, TextEditorHighlightingPassRegistrar.Anchor.FIRST, -1, false, false)
    }
}
