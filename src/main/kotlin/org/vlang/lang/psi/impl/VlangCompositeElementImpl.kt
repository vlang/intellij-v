package org.vlang.lang.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor
import com.intellij.psi.util.PsiTreeUtil
import org.vlang.lang.psi.*

open class VlangCompositeElementImpl(node: ASTNode) : ASTWrapperPsiElement(node), VlangCompositeElement {
    override fun toString(): String {
        return node.elementType.toString()
    }

    override fun processDeclarations(
        processor: PsiScopeProcessor,
        state: ResolveState,
        lastParent: PsiElement?,
        place: PsiElement,
    ): Boolean {
        return processDeclarationsDefault(this, processor, state, lastParent, place)
    }

    companion object {
        fun processDeclarationsDefault(
            o: VlangCompositeElement,
            processor: PsiScopeProcessor,
            state: ResolveState,
            lastParent: PsiElement?,
            place: PsiElement,
        ): Boolean {
            if (o is VlangExpression) return true
            if (!processor.execute(o, state)) return false
            if ((o is VlangIfStatement || o is VlangForStatement || o is VlangBlock)
                && processor is VlangScopeProcessorBase
            ) {
                if (!PsiTreeUtil.isAncestor(o, processor.origin, false)) {
                    return true
                }
            }
            return if (o is VlangBlock)
                processBlock(o, processor, state, lastParent, place)
            else
                ResolveUtil.processChildren(o, processor, state, lastParent, place)
        }

        private fun processBlock(
            o: VlangBlock,
            processor: PsiScopeProcessor,
            state: ResolveState,
            lastParent: PsiElement?, place: PsiElement,
        ): Boolean {
            return ResolveUtil.processChildrenFromTop(o, processor, state, lastParent, place) &&
                    processParameters(o, processor) && processReceiver(o, processor)
        }

        private fun processParameters(b: VlangBlock, processor: PsiScopeProcessor): Boolean {
            if (processor !is VlangScopeProcessorBase || b.parent !is VlangSignatureOwner) {
                return true
            }

            return VlangPsiImplUtil.processSignatureOwner(
                b.parent as VlangSignatureOwner,
                processor
            )
        }

        private fun processReceiver(b: VlangBlock, processor: PsiScopeProcessor): Boolean {
            if (processor !is VlangScopeProcessorBase || b.parent !is VlangMethodDeclaration) {
                return true
            }

            val receiver = (b.parent as VlangMethodDeclaration).receiver

            return VlangPsiImplUtil.processNamedElements(
                processor,
                ResolveState.initial(),
                listOf(receiver),
                true
            )
        }
    }
}

