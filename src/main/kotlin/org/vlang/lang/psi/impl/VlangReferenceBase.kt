package org.vlang.lang.psi.impl

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiPolyVariantReferenceBase
import org.vlang.lang.psi.VlangReferenceExpressionBase

abstract class VlangReferenceBase<T : VlangReferenceExpressionBase?>(element: T, range: TextRange?) :
    PsiPolyVariantReferenceBase<T>(element, range) {

//    protected fun processDirectory(
//        dir: PsiDirectory?,
//        file: VlangFile?,
//        packageName: String?,
//        processor: VlangScopeProcessor,
//        state: ResolveState,
//        localProcessing: Boolean
//    ): Boolean {
//        if (dir == null) return true
//        val filePath = getPath(file)
//        val module: Module? = if (file != null) ModuleUtilCore.findModuleForPsiElement(file) else null
//        for (f in dir.files) {
//            if (f !is VlangFile || Comparing.equal(getPath(f), filePath)) continue
//            if (packageName != null && packageName != (f as VlangFile).getPackageName()) continue
//            if (!allowed(f, file, module)) continue
//            if (!processFileEntities(f as VlangFile, processor, state, localProcessing)) return false
//        }
//        return true
//    }
//
//    protected fun processBuiltin(
//        processor: VlangScopeProcessor,
//        state: ResolveState,
//        element: VlangCompositeElement
//    ): Boolean {
//        val builtin: VlangFile = VlangSdkUtil.findBuiltinFile(element)
//        return builtin == null || processFileEntities(builtin, processor, state, true)
//    }
//
//    protected fun processImports(
//        file: VlangFile,
//        processor: VlangScopeProcessor,
//        state: ResolveState,
//        element: VlangCompositeElement
//    ): Boolean {
//        for ((key, value) in file.getImportMap().entrySet()) {
//            for (o in value) {
//                if (o.isForSideEffects()) continue
//                val importString: VlangImportString = o.getImportString()
//                if (o.isDot()) {
//                    val implicitDir: PsiDirectory = importString.resolve()
//                    val resolved = !processDirectory(implicitDir, file, null, processor, state, false)
//                    if (resolved && !processor.isCompletion()) {
//                        putIfAbsent(o, element)
//                    }
//                    if (resolved) return false
//                } else {
//                    if (o.getAlias() == null) {
//                        val resolve: PsiDirectory = importString.resolve()
//                        if (resolve != null && !processor.execute(resolve, state.put(ACTUAL_NAME, key))) return false
//                    }
//                    // todo: multi-resolve into appropriate package clauses
//                    if (!processor.execute(o, state.put(ACTUAL_NAME, key))) return false
//                }
//            }
//        }
//        return true
//    }
//
//    protected fun createResolveProcessor(
//        result: MutableCollection<ResolveResult?>,
//        o: VlangReferenceExpressionBase
//    ): VlangScopeProcessor {
//        return object : VlangScopeProcessor() {
//            fun execute(element: PsiElement, state: ResolveState): Boolean {
//                if (element == o) return !result.add(PsiElementResolveResult(element))
//                val name = ObjectUtils.chooseNotNull(
//                    state.get(ACTUAL_NAME),
//                    if (element is PsiNamedElement) element.name else null
//                )
//                if (name != null && o.getIdentifier().textMatches(name)) {
//                    result.add(PsiElementResolveResult(element))
//                    return false
//                }
//                return true
//            }
//        }
//    }
//
//    protected abstract fun processFileEntities(
//        file: VlangFile,
//        processor: VlangScopeProcessor,
//        state: ResolveState,
//        localProcessing: Boolean
//    ): Boolean
//
//    companion object {
//        val IMPORT_USERS = Key.create<List<PsiElement>>("IMPORT_USERS")
//        val ACTUAL_NAME = Key.create<String>("ACTUAL_NAME")
//        protected fun getPath(file: PsiFile?): String? {
//            if (file == null) return null
//            val virtualFile = file.originalFile.virtualFile
//            return virtualFile?.path
//        }
//
//        private fun putIfAbsent(importSpec: VlangImportSpec, usage: PsiElement) {
//            synchronized(importSpec) {
//                val newUsages = ContainerUtil.newSmartList<PsiElement>(usage)
//                newUsages.addAll(IMPORT_USERS[importSpec, ContainerUtil.emptyList()])
//                importSpec.putUserData(IMPORT_USERS, newUsages)
//            }
//        }
//    }
}