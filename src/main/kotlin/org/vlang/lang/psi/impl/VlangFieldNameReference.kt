package org.vlang.lang.psi.impl

import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import org.vlang.lang.psi.*

class VlangFieldNameReference(element: VlangReferenceExpressionBase) :
    VlangCachedReference<VlangReferenceExpressionBase>(element) {

    override fun processResolveVariants(processor: VlangScopeProcessor): Boolean {
        val fieldProcessor =
            if (processor is VlangFieldProcessor)
                processor
            else
                object : VlangFieldProcessor(myElement) {
                    override fun execute(e: PsiElement, state: ResolveState): Boolean {
                        return super.execute(e, state) && processor.execute(e, state)
                    }
                }

        val key = myElement.parentOfType<VlangKey>()
        val value = myElement.parentOfType<VlangValue>()
        if (key == null && (value == null || PsiTreeUtil.getPrevSiblingOfType(value, VlangKey::class.java) != null))
            return true

        val type = myElement.parentOfType<VlangLiteralValueExpression>()?.getType(null)?.resolveType()

        return if (!processStructType(fieldProcessor, type))
            false
        else !(type is VlangPointerType && !processStructType(fieldProcessor, type.getType()))
    }

    private fun processStructType(fieldProcessor: VlangScopeProcessor, type: VlangType?): Boolean {
        return !(type is VlangStructType && !type.processDeclarations(fieldProcessor, ResolveState.initial(), null, myElement))
    }

    fun inStructTypeKey(): Boolean {
        val type = myElement.parentOfType<VlangLiteralValueExpression>()?.getType(null)
        return VlangPsiImplUtil.getParentVlangValue(myElement) == null &&
                type is VlangStructType
    }

    override fun resolveInner(): PsiElement? {
        val p = VlangFieldProcessor(myElement)
        processResolveVariants(p)
        return p.getResult()
    }

    private open class VlangFieldProcessor(element: PsiElement) : VlangScopeProcessorBase(element) {
        private val myModule: Module?

        init {
            val containingFile = origin.containingFile
            myModule = ModuleUtilCore.findModuleForPsiElement(containingFile.originalFile)
        }

        override fun crossOff(e: PsiElement): Boolean {
            if (e !is VlangFieldDefinition && e !is VlangAnonymousFieldDefinition)
                return true
            val named = e as VlangNamedElement
            val myFile = origin.containingFile
            val file = e.containingFile
//            if (myFile !is VlangFile || !VlangPsiImplUtil.allowed(file, myFile, myModule)) return true
//            val localResolve: Boolean = VlangReference.isLocalResolve(myFile, file)
            val localResolve = false
            return !e.isValid || !(named.isPublic() || localResolve)
        }
    }
}
