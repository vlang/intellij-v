package org.vlang.lang.psi.impl

import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangReferenceBase.Companion.LOCAL_RESOLVE
import org.vlang.lang.psi.types.VlangArrayTypeEx
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.unwrapAlias
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.unwrapGenericInstantiation
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.unwrapPointer
import org.vlang.lang.psi.types.VlangChannelTypeEx
import org.vlang.lang.psi.types.VlangStructTypeEx
import org.vlang.lang.psi.types.VlangTypeEx

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
        if (key == null && (value == null || PsiTreeUtil.getPrevSiblingOfType(value, VlangKey::class.java) != null)) {
            return true
        }

        var type = myElement.parentOfType<VlangLiteralValueExpression>()?.getType(null)
        if (type == null) {
            val callExpr = VlangCodeInsightUtil.getCallExpr(element)
            val paramTypes = VlangCodeInsightUtil.getCalledParams(callExpr)
            type = paramTypes?.lastOrNull { it.unwrapPointer().unwrapAlias().unwrapGenericInstantiation() is VlangStructTypeEx }?: return true
        }

        val typeToProcess = type.unwrapPointer().unwrapAlias().unwrapGenericInstantiation()
        if (typeToProcess is VlangArrayTypeEx) {
            return processStructType(fieldProcessor, VlangStructTypeEx.ArrayInit, false)
        }

        if (typeToProcess is VlangChannelTypeEx) {
            return processStructType(fieldProcessor, VlangStructTypeEx.ChanInit, false)
        }

        val typeFile = type.anchor()?.containingFile as? VlangFile
        val originFile = element.containingFile as VlangFile
        val localResolve = typeFile == null || VlangReference.isLocalResolve(typeFile, originFile)

        return processStructType(fieldProcessor, typeToProcess, localResolve)
    }

    private fun processStructType(fieldProcessor: VlangScopeProcessor, type: VlangTypeEx?, localResolve: Boolean): Boolean {
        if (type !is VlangStructTypeEx) return true

        val state = if (localResolve) ResolveState.initial().put(LOCAL_RESOLVE, true) else ResolveState.initial()
        val declaration = type.resolve(project) ?: return true
        val structType = declaration.structType

        val fields = structType.fieldList
        for (field in fields) {
            if (!fieldProcessor.execute(field, state)) return false
        }

        structType.embeddedStructList.forEach {
            if (!processStructType(fieldProcessor, it.type.toEx(), localResolve)) return false
        }

        return true
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
            if (e !is VlangFieldDefinition && e !is VlangEmbeddedDefinition)
                return true
            val named = e as VlangNamedElement
            val originFile = origin.containingFile as VlangFile
            val file = e.containingFile as VlangFile
//            if (myFile !is VlangFile || !VlangPsiImplUtil.allowed(file, myFile, myModule)) return true
            val localResolve = VlangReference.isLocalResolve(originFile, file)
            return !e.isValid || !(named.isPublic() || localResolve)
        }
    }
}
