package org.vlang.lang.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor
import org.vlang.lang.psi.VlangCompositeElement

open class VlangCompositeElementImpl(node: ASTNode) : ASTWrapperPsiElement(node), VlangCompositeElement {
    override fun toString(): String {
        return node.elementType.toString()
    }

    override fun processDeclarations(
        processor: PsiScopeProcessor,
        state: ResolveState,
        lastParent: PsiElement?,
        place: PsiElement
    ): Boolean {
        return processDeclarationsDefault(this, processor, state, lastParent, place)
    }

    companion object {
        fun processDeclarationsDefault(
            o: VlangCompositeElement,
            processor: PsiScopeProcessor,
            state: ResolveState,
            lastParent: PsiElement?,
            place: PsiElement
        ): Boolean {
//            if (o is GoLeftHandExprList || o is GoExpression) return true
//            if (!o.shouldGoDeeper()) return processor.execute(o, state)
            if (!processor.execute(o, state)) return false
//            if ((o is GoSwitchStatement ||
//                        o is GoIfStatement ||
//                        o is GoForStatement ||
//                        o is GoCommClause ||
//                        o is GoBlock ||
//                        o is GoCaseClause)
//                && processor is GoScopeProcessorBase
//            ) {
//                if (!PsiTreeUtil.isAncestor(o, (processor as GoScopeProcessorBase).myOrigin, false)) return true
//            }
//            return if (o is GoBlock) processBlock(
//                o as GoBlock,
//                processor,
//                state,
//                lastParent,
//                place
//            ) else ResolveUtil.processChildren(o, processor, state, lastParent, place)

            return false
        }

//        private fun processBlock(
//            o: GoBlock,
//            processor: PsiScopeProcessor,
//            state: ResolveState,
//            lastParent: PsiElement?, place: PsiElement
//        ): Boolean {
//            return ResolveUtil.processChildrenFromTop(o, processor, state, lastParent, place) && processParameters(
//                o,
//                processor
//            )
//        }
//
//        private fun processParameters(b: GoBlock, processor: PsiScopeProcessor): Boolean {
//            return if (processor is GoScopeProcessorBase && b.getParent() is GoSignatureOwner) {
//                GoPsiImplUtil.processSignatureOwner(
//                    b.getParent() as GoSignatureOwner,
//                    processor as GoScopeProcessorBase
//                )
//            } else true
//        }
    }
}