package org.vlang.lang.psi.impl

import com.intellij.codeInsight.completion.CompletionUtil
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Conditions
import com.intellij.openapi.util.RecursionManager
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.impl.source.resolve.ResolveCache
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.childrenOfType
import com.intellij.psi.util.parentOfType
import com.intellij.util.ArrayUtil
import org.vlang.configurations.VlangConfiguration
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.ide.codeInsight.VlangTypeInferenceUtil
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangPsiImplUtil.processNamedElements
import org.vlang.lang.psi.types.*
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import org.vlang.lang.sql.VlangSqlUtil
import org.vlang.lang.stubs.index.VlangGlobalVariablesIndex
import org.vlang.lang.stubs.index.VlangModulesFingerprintIndex
import org.vlang.lang.stubs.index.VlangModulesIndex
import org.vlang.utils.inside

class VlangReference(el: VlangReferenceExpressionBase, val forTypes: Boolean = false) :
    VlangReferenceBase<VlangReferenceExpressionBase>(
        el,
        TextRange.from(
            el.getIdentifier()?.startOffsetInParent ?: 0,
            el.getIdentifier()?.textLength ?: el.textLength
        )
    ) {

    companion object {
        private val MY_RESOLVER: ResolveCache.PolyVariantResolver<VlangReference> =
            ResolveCache.PolyVariantResolver { ref, _ -> ref.resolveInner() }

        private fun getContextElement(state: ResolveState?): PsiElement? {
            val context = state?.get(VlangPsiImplUtil.CONTEXT)
            return context?.element
        }

        fun getContextFile(state: ResolveState): PsiFile? {
            return getContextElement(state)?.containingFile
        }

        fun isLocalResolve(origin: VlangFile, external: VlangFile?): Boolean {
            if (external == null) return true

            val originModule = origin.getModuleQualifiedName()
            val externalModule = external.getModuleQualifiedName()
            return originModule == externalModule
        }
    }

    private val stubsManager = project.service<VlangStubsManager>()

    private val identifier: PsiElement?
        get() = myElement?.getIdentifier()

    private fun resolveInner(): Array<ResolveResult> {
        if (!myElement.isValid) return ResolveResult.EMPTY_ARRAY
        val result = mutableSetOf<ResolveResult>()
        processResolveVariants(createResolveProcessor(result, myElement))
        return result.toTypedArray()
    }

    fun processResolveVariants(processor: VlangScopeProcessor): Boolean {
        val file = myElement.containingFile as? VlangFile ?: return false

        val state = if (file is VlangCodeFragment) {
            createContextOnElement(myElement).put(PROCESS_PRIVATE_MEMBERS, true)
        } else {
            createContextOnElement(myElement)
        }

        val qualifier = myElement.getQualifier()
        return if (qualifier != null)
            processQualifierExpression(qualifier, processor, state)
        else
            processUnqualifiedResolve(file, processor, state)
    }

    private fun processQualifierExpression(
        qualifier: VlangCompositeElement,
        processor: VlangScopeProcessor,
        state: ResolveState,
    ): Boolean {
        if (qualifier is VlangExpression) {
            val type = qualifier.getType(null)
            if (type != null) {
                if (!processType(type, processor, state)) {
                    return false
                }
            }

            if (qualifier is VlangReferenceExpression) {
                if (!processReferenceQualifier(qualifier, processor, state)) return false
            }
        }

        if (qualifier is VlangImportPath) {
            if (processImportPath(qualifier, processor, state)) return false
        }

        if (qualifier is VlangTypeReferenceExpression) {
            if (!processReferenceQualifier(qualifier, processor, state)) return false
        }

        return true
    }

    private fun processReferenceQualifier(
        qualifier: VlangReferenceExpressionBase,
        processor: VlangScopeProcessor,
        state: ResolveState,
    ): Boolean {
        val resolved = qualifier.resolve()

        if (resolved is VlangImportAlias) {
            val importSpec = resolved.parent as VlangImportSpec
            val modules = importSpec.resolve()
            for (module in modules) {
                if (!processModule(module.name, processor, state)) return false
            }
        }

        if (resolved is VlangModule.VlangPomTargetPsiElement) {
            val target = resolved.target

            val containingFile = qualifier.containingFile as VlangFile
            val containingModule = containingFile.getModuleName()
            if (containingModule != null && qualifier.textMatches(containingModule)) {
                if (!processFileEntities(containingFile, processor, state, true)) return false
            }

            if (!processModule(target.name, processor, state)) return false
        }

        return true
    }

    private fun processImportPath(
        importPath: VlangImportPath,
        processor: VlangScopeProcessor,
        state: ResolveState,
    ): Boolean {
        val moduleName = importPath.qualifiedName
        return processModule(moduleName, processor, state)
    }

    private fun processModule(moduleName: String, processor: VlangScopeProcessor, state: ResolveState): Boolean {
        val moduleFiles =
            VlangModulesIndex.find(moduleName, myElement.project, GlobalSearchScope.allScope(myElement.project), null)

        if (moduleFiles.isNotEmpty()) {
            val moduleFile = moduleFiles.first()
            val moduleDir = moduleFile.parent
            val newState = state
                .put(MODULE_NAME, moduleFile.getModuleQualifiedName())
                .put(NEED_QUALIFIER_NAME, false)
            if (!processDirectory(moduleDir, null, processor, newState, false)) {
                return true
            }
        }
        return false
    }

    private fun processType(type: VlangTypeEx, processor: VlangScopeProcessor, state: ResolveState): Boolean {
        val result = RecursionManager.doPreventingRecursion(type, true) {
            if (!processExistingType(type, processor, state)) return@doPreventingRecursion false

            true
        }
        return result == true
    }

    private fun processExistingType(typ: VlangTypeEx, processor: VlangScopeProcessor, state: ResolveState): Boolean {
        val file = typ.anchor()?.containingFile as? VlangFile
        val contextFile = getContextFile(state) ?: myElement.containingFile
        if (contextFile !is VlangFile) {
            return true
        }

        val localResolve = isLocalResolve(contextFile, file)
        val newState = state.put(LOCAL_RESOLVE, localResolve)

        if (VlangCodeInsightUtil.insideCompileTimeFor(identifier) && typ.qualifiedName() != "stubs.CompileTimeTypeInfo") {
            if (!processCompileTimeTypeInfo(processor, newState)) return false
        }

        if (typ is VlangAliasTypeEx) {
            if (!processMethods(typ, processor, newState, localResolve)) return false
            if (!processType(typ.inner, processor, newState)) return false
            if (!processAnyType(contextFile, processor, newState, localResolve)) return false
            return true
        }

        if (typ is VlangSumTypeEx) {
            if (!processMethods(typ, processor, newState, localResolve)) return false
            if (!processSymTypeFields(typ, processor, newState)) return false
            if (!processAnyType(contextFile, processor, newState, localResolve)) return false
            return true
        }

        if (typ is VlangPointerTypeEx) {
            return processType(typ.inner, processor, newState)
        }

        if (typ is VlangOptionTypeEx) {
            val baseType = typ.inner
            if (baseType != null) {
                return processType(baseType, processor, newState)
            }
            return true
        }

        if (typ is VlangResultTypeEx) {
            val baseType = typ.inner
            if (baseType != null) {
                return processType(baseType, processor, newState)
            }
            return true
        }

        if (typ is VlangAnonStructTypeEx) {
            val anonStruct = typ.resolve() ?: return true
            return processNamedElements(processor, newState, anonStruct.fieldList, true)
        }

        if (typ is VlangStructTypeEx) {
            val isMethodRef = element.parent is VlangCallExpr

            val declaration = typ.resolve(project) ?: return true
            val structType = declaration.structType

            // if it some struct from C code, we can't resolve it
            // so we just resolve it to UnknownCDeclaration struct
            if (typ.qualifiedName() == VlangStructTypeEx.UnknownCDeclarationStruct.qualifiedName()) {
                if (isMethodRef && !processMethods(typ, processor, state.put(SEARCH_NAME, "unknown_method"), localResolve)) return false
                return processNamedElements(
                    processor,
                    state.put(SEARCH_NAME, "unknown_field"),
                    structType.fieldList,
                    localResolve
                )
            }

            // If it is a call, then most likely it is a method call, but it
            // could be a function call that is stored in a structure field.
            if (isMethodRef) {
                if (!processMethods(typ, processor, newState, localResolve)) return false
                if (!processNamedElements(processor, newState, structType.fieldList, localResolve)) return false
            } else {
                if (!processNamedElements(processor, newState, structType.fieldList, localResolve)) return false
                if (!processMethods(typ, processor, newState, localResolve)) return false
            }

            val embedded = structType.embeddedStructList
            if (!processNamedElements(processor, newState, embedded, localResolve)) return false

            if (!processAnyType(contextFile, processor, newState, localResolve)) return false
            return true
        }

        if (typ is VlangInterfaceTypeEx) {
            val isMethodRef = element.parent is VlangCallExpr

            val declaration = typ.resolve(project) ?: return false
            val interfaceType = declaration.interfaceType

            if (!isMethodRef && !processNamedElements(processor, newState, interfaceType.fieldList, localResolve)) return false
            if (!processNamedElements(processor, newState, interfaceType.methodList, localResolve)) return false
            if (!processMethods(typ, processor, newState, localResolve)) return false

            interfaceType.embeddedInterfacesList.forEach {
                if (!processType(it.type.toEx(), processor, newState)) return false
            }

            if (!processAnyType(contextFile, processor, newState, localResolve)) return false
            return true
        }

        if (typ is VlangEnumTypeEx) {
            val declaration = typ.resolve(project) ?: return false
            val enumType = declaration.enumType

            if (!processNamedElements(processor, newState, enumType.fieldList, localResolve)) return false
            if (!processMethods(typ, processor, newState, localResolve)) return false
            if (!processAnyType(contextFile, processor, newState, localResolve)) return false

            // don't add this methods if enum without flag attribute
            // TODO: add inspection
            if (!processor.isCompletion() || declaration.isFlag) {
                if (!processType(VlangEnumTypeEx.FLAG_ENUM, processor, newState)) return false
            }
            return true
        }

        if (typ is VlangArrayTypeEx) {
            if (typ.inner is VlangThreadTypeEx) {
                return processThreadPoolMethods(processor, newState)
            }

            if (!processMethods(typ, processor, newState, localResolve)) return false
            if (!processAnyType(contextFile, processor, newState, localResolve)) return false
            return processBuiltinTypeMethods(project, "array", processor, newState)
        }

        if (typ is VlangMapTypeEx) {
            if (!processMethods(typ, processor, newState, localResolve)) return false
            if (!processAnyType(contextFile, processor, newState, localResolve)) return false
            return processBuiltinTypeMethods(project, "map", processor, newState)
        }

        if (typ is VlangChannelTypeEx) {
            if (!processMethods(typ, processor, newState, localResolve)) return false
            if (!processAnyType(contextFile, processor, newState, localResolve)) return false
            return processBuiltinChannelTypeMethods(project, processor, newState)
        }

        if (typ is VlangThreadTypeEx) {
            if (!processMethods(typ, processor, newState, localResolve)) return false
            if (!processAnyType(contextFile, processor, newState, localResolve)) return false
            return processThreadTypeMethods(processor, newState)
        }

        if (typ is VlangGenericInstantiationEx) {
            if (!processType(typ.inner, processor, newState)) return false
            if (!processAnyType(contextFile, processor, newState, localResolve)) return false
            return true
        }

        if (typ is VlangGenericTypeEx) {
            if (!processTypeInfoFields(processor, newState)) return false
            if (!processCompileTimeTypeInfo(processor, newState)) return false
            return true
        }

        if (typ is VlangSharedTypeEx) {
            if (!processType(typ.inner, processor, newState)) return false
            return true
        }

        if (typ == VlangPrimitiveTypeEx.BYTE) {
            if (!processMethods(VlangPrimitiveTypeEx.U8, processor, newState, localResolve)) return false
        }

        if (typ == VlangPrimitiveTypeEx.I32) {
            if (!processMethods(VlangPrimitiveTypeEx.INT, processor, newState, localResolve)) return false
        }

        if (!processAnyType(contextFile, processor, newState, localResolve)) return false
        if (!processMethods(typ, processor, newState, localResolve)) return false

        return true
    }

    private fun processSymTypeFields(typ: VlangSumTypeEx, processor: VlangScopeProcessor, newState: ResolveState): Boolean {
        val fields = typ.getFields(project)
        return processNamedElements(processor, newState, fields, true)
    }

    private fun processAnyType(
        contextFile: PsiFile,
        processor: VlangScopeProcessor,
        newState: ResolveState,
        localResolve: Boolean,
    ) = processMethods(VlangAliasTypeEx.anyType(contextFile), processor, newState, localResolve)

    private fun processThreadPoolMethods(processor: VlangScopeProcessor, state: ResolveState): Boolean {
        val type = stubsManager.findStructType("threads.v", "ThreadPool") ?: return true
        return processExistingType(type, processor, state)
    }

    private fun processThreadTypeMethods(processor: VlangScopeProcessor, state: ResolveState): Boolean {
        val type = stubsManager.findStructType("threads.v", "Thread") ?: return true
        return processExistingType(type, processor, state)
    }

    private fun processBuiltinTypeMethods(
        project: Project,
        name: String,
        processor: VlangScopeProcessor,
        state: ResolveState,
    ): Boolean {
        val builtin = VlangConfiguration.getInstance(project).builtinLocation
        val virtualFile = builtin?.findChild("$name.v") ?: return true
        val psiFile = PsiManager.getInstance(project).findFile(virtualFile) as? VlangFile ?: return true
        val struct = psiFile.getStructs()
            .firstOrNull { it.name == name } ?: return true
        return processExistingType(struct.structType.toEx(), processor, state)
    }

    private fun processBuiltinChannelTypeMethods(
        project: Project,
        processor: VlangScopeProcessor,
        state: ResolveState,
    ): Boolean {
        val vlib = VlangConfiguration.getInstance(project).builtinLocation?.parent
        val syncDir = vlib?.findChild("sync") ?: return true
        val virtualFile = syncDir.findChild("channels.c.v") ?: return true
        val psiFile = PsiManager.getInstance(project).findFile(virtualFile) as? VlangFile ?: return true
        val struct = psiFile.getStructs()
            .firstOrNull { it.name == "Channel" } ?: return true
        return processExistingType(struct.structType.toEx(), processor, state)
    }

    private fun processTypeInfoFields(processor: VlangScopeProcessor, state: ResolveState): Boolean {
        val type = stubsManager.findStructType("builtin_compile_time.v", "TypeInfo") ?: return true
        return processExistingType(type, processor, state)
    }

    private fun processCompileTimeTypeInfo(processor: VlangScopeProcessor, state: ResolveState): Boolean {
        val type = stubsManager.findStructType("compile_time_reflection.v", "CompileTimeTypeInfo") ?: return true
        return processExistingType(type, processor, state)
    }

    private fun processMethods(type: VlangTypeEx, processor: VlangScopeProcessor, state: ResolveState, localResolve: Boolean): Boolean {
        if (state.get(NOT_PROCESS_METHODS) == true) return true
        return processNamedElements(processor, state, type.methodsList(project), localResolve)
    }

    private fun processUnqualifiedResolve(
        file: VlangFile,
        processor: VlangScopeProcessor,
        state: ResolveState,
    ): Boolean {
        if (identifier!!.textMatches("_")) {
            return processor.execute(myElement, state)
        }

        if (VlangCodeInsightUtil.insideArrayCreation(identifier!!)) {
            // []int{init: index}
            //             ^^^^^
            val needResolve = VlangCodeInsightUtil.isIndexVariable(identifier!!) ||
                    VlangCodeInsightUtil.isItVariable(identifier!!) // TODO: remove it variable when it will be removed from V
            if (needResolve) {
                val struct = getArrayInitStruct() ?: return false
                val fields = struct.structType.fieldList
                val field = fields.find { it.name == "index" } ?: return true
                return processor.execute(field, state.put(SEARCH_NAME, "index").put(ACTUAL_NAME, "index"))
            }
        }

        // expr or { err }
        if (VlangCodeInsightUtil.insideOrGuard(identifier!!) || VlangCodeInsightUtil.insideElseBlockIfGuard(identifier!!)) {
            val errVariable = VlangLangUtil.getErrVariableDefinition(project) ?: return false
            if (!processor.execute(errVariable, state)) return false
        }

        // sql {
        //    select from Users where id = id
        //                            ^^
        // }
        if (VlangSqlUtil.insideSql(identifier!!) && VlangSqlUtil.fieldReference(identifier!!)) {
            val resolved = VlangSqlUtil.getTable(identifier!!)?.typeReferenceExpression?.resolve() as? VlangStructDeclaration
            if (resolved != null) {
                return processType(resolved.structType.toEx(), processor, state.put(NOT_PROCESS_METHODS, true))
            }
        }

        // .red
        if (myElement is VlangEnumFetch) {
            return processEnumFetch(myElement as VlangEnumFetch, processor, state)
        }

        when (myElement.parent) {
            is VlangFieldName -> {
                if (!processTrailingStructParams(processor, state)) return false
                if (!processLiteralValueField(processor, state)) return false

                return true
            }
        }

        when (val grand = myElement.parent.parent) {
            is VlangImportSpec -> {
                val importPath = grand.importPath
                return processQualifierExpression(importPath, processor, state)
            }
        }

        val parentMethod = element.parentOfType<VlangMethodDeclaration>()
        if (needResolveGenericParameterForMethod(parentMethod)) {
            if (!processOwnerGenericTs(parentMethod!!, processor, state)) return false
        }

        if (element.inside<VlangArgumentList>()) {
            if (!processPseudoParams(processor, state)) return false
        }

        if (element.inside<VlangOffsetOfCallExpr>()) {
            if (!processOffsetOfCall(processor, state)) return false
        }

        if (!processBlock(file, processor, state, true)) return false
        if (!processBuiltin(processor, state)) return false
        if (!processOsModule(file, processor, state)) return false
        if (!processStubs(processor, state)) return false
        if (!processImportSpec(file, processor, state)) return false
        if (!processImportedModulesForCompletion(file, processor, state)) return false
        if (!processImportedModules(file, processor, state, myElement)) return false
        if (!processFileEntities(file, processor, state, true)) return false
        if (!processDirectory(file.originalFile.parent, file, processor, state, true)) return false
        if (!processModulesEntities(file, processor, state)) return false
        if (!processGlobalVariables(file, processor, state)) return false
        if (!processCompileTimeConstants(processor, state)) return false
        if (!processIfUnknownCDeclaration(processor, state)) return false

        return true
    }

    private fun processOffsetOfCall(processor: VlangScopeProcessor, state: ResolveState): Boolean {
        val call = element.parentOfType<VlangOffsetOfCallExpr>() ?: return false
        if (PsiTreeUtil.isAncestor(call.referenceExpression, element, false)) {
            return processType(call.type.toEx(), processor, state.put(NOT_PROCESS_METHODS, true))
        }
        return true
    }

    private fun getArrayInitStruct(): VlangStructDeclaration? {
        val stubFile = stubsManager.findFile("arrays.v") ?: return null
        return stubFile.getStructs().firstOrNull { it.name == "ArrayInit" }
    }

    private fun processIfUnknownCDeclaration(processor: VlangScopeProcessor, state: ResolveState): Boolean {
        if (identifier == null || !identifier!!.text.startsWith("C.")) {
            return true
        }

        return processStubFile("c_decl.v", processor, state.put(SEARCH_NAME, "UnknownCDeclaration"))
    }

    private fun needResolveGenericParameterForMethod(parentMethod: VlangMethodDeclaration?): Boolean {
        if (parentMethod == null || identifier?.text?.length != 1) {
            return false
        }

        return !PsiTreeUtil.isAncestor(parentMethod.receiver, identifier!!, false)
                || VlangPsiImplUtil.prevAngleParen(identifier!!)
                || VlangPsiImplUtil.prevLeftBracket(identifier!!)
                || VlangPsiImplUtil.prevComma(identifier!!)
    }

    private fun processOwnerGenericTs(
        parentMethod: VlangMethodDeclaration,
        processor: VlangScopeProcessor,
        state: ResolveState,
    ): Boolean {
        var receiverType = parentMethod.receiverType.toEx()
        if (receiverType is VlangPointerTypeEx) {
            receiverType = receiverType.inner
        }

        if (receiverType is VlangGenericInstantiationEx && receiverType.inner is VlangResolvableTypeEx<*>) {
            val resolved = (receiverType.inner as VlangResolvableTypeEx<*>).resolve(project) ?: return false
            val genericParametersOwner = resolved.childrenOfType<VlangGenericParametersOwner>().firstOrNull() ?: return false
            val genericParameters = genericParametersOwner.genericParameters?.parameters
                ?: return false
            if (!processNamedElements(processor, state, genericParameters, false)) return false
        }

        return true
    }

    private fun processEnumFetch(
        fetch: VlangEnumFetch,
        processor: VlangScopeProcessor,
        state: ResolveState,
    ): Boolean {
        val contextType = VlangTypeInferenceUtil.getContextType(fetch) ?: return true
        return processType(contextType, processor, state.put(NOT_PROCESS_METHODS, true))
    }

    private fun processDirectory(
        dir: PsiDirectory?,
        file: VlangFile?,
        processor: VlangScopeProcessor,
        state: ResolveState,
        localProcessing: Boolean,
    ): Boolean {
        if (dir == null) {
            return true
        }

        val moduleName = file?.getModuleQualifiedName()
        val filePath = getPath(file)

        for (f in dir.files) {
            if (f !is VlangFile || getPath(f) == filePath) {
                continue
            }
            if (moduleName != null && moduleName != f.getModuleQualifiedName()) {
                continue
            }
            if (!processFileEntities(f, processor, state, localProcessing)) {
                return false
            }
        }

        return true
    }

    private fun processBuiltin(processor: VlangScopeProcessor, state: ResolveState): Boolean {
        val builtin = VlangConfiguration.getInstance(myElement.project).builtinLocation ?: return true
        val psiManager = PsiManager.getInstance(myElement.project)
        builtin.children
            .map { psiManager.findFile(it) }
            .filterIsInstance<VlangFile>()
            .filter { !it.isTestFile() }
            .forEach {
                if (!processFileEntities(it, processor, state, false))
                    return false
            }

        return true
    }

    private fun processOsModule(file: VlangFile, processor: VlangScopeProcessor, state: ResolveState): Boolean {
        if (!file.isShellScript()) return true

        val stdlib = VlangConfiguration.getInstance(myElement.project).stdlibLocation ?: return true
        val osModule = stdlib.findChild("os") ?: return true
        val psiManager = PsiManager.getInstance(myElement.project)
        osModule.children
            .map { psiManager.findFile(it) }
            .filterIsInstance<VlangFile>()
            .filter { !it.isTestFile() && !it.isJSFile() }
            .forEach {
                if (!processFileEntities(it, processor, state, false))
                    return false
            }

        return true
    }

    private fun processStubs(processor: VlangScopeProcessor, state: ResolveState): Boolean {
        if (!processStubFile("primitives.v", processor, state)) return false
        if (!processStubFile("compile_time.v", processor, state)) return false
        if (!processStubFile("compile_time_reflection.v", processor, state)) return false
        if (!processStubFile("vweb.v", processor, state)) return false
        return true
    }

    private fun processStubFile(name: String, processor: VlangScopeProcessor, state: ResolveState): Boolean {
        val file = stubsManager.findFile(name) ?: return true
        return processFileEntities(file, processor, state, false)
    }

    // TODO: redone
    private fun processImportedModules(
        file: VlangFile,
        processor: VlangScopeProcessor,
        state: ResolveState,
        element: VlangCompositeElement,
    ): Boolean {
        if (element.parentOfType<VlangImportSpec>() != null) {
            return true
        }

        val searchName = identifier!!.text
        val spec = file.resolveImportSpec(searchName)
        if (spec == null) {
            val parentReferenceExpression = element.parent as? VlangReferenceExpressionBase
            val qualifier = parentReferenceExpression?.getQualifier()

            // element: cat
            // parentReferenceExpression: cat.foo
            if (qualifier != null && PsiTreeUtil.isAncestor(qualifier, element, false)) {
                // when use `cat.new()` in `cat` module.
                val currentModule = file.getModuleName()
                if (currentModule != null && searchName == currentModule && file.containingDirectory != null) {
                    val module = VlangModule.fromDirectory(file.containingDirectory!!)
                    if (!processor.execute(module.toPsi(), state.put(ACTUAL_NAME, searchName))) return false
                }
            }

            return true
        }

        if (spec.selectiveImportList != null) {
            if (!processQualifierExpression(spec.importPath, processor, state)) return false
        }

        if (spec.importAlias != null && spec.aliasName == searchName) {
            return processor.execute(spec.importAlias!!, state.put(ACTUAL_NAME, searchName))
        }

        val resolved = spec.resolve()
        return resolved.any { module ->
            processor.execute(module.toPsi(), state.put(ACTUAL_NAME, searchName))
        }
    }

    private fun processModulesEntities(file: VlangFile, processor: VlangScopeProcessor, state: ResolveState): Boolean {
        if (!processor.isCompletion() || file is VlangDebuggerExpressionCodeFragment) {
            // This method is only for autocompletion when a user writes
            // a symbol (from another module) name, and we want to import
            // the symbol, and the module that contains it.
            return true
        }

        if (identifier?.textMatches(CompletionUtil.DUMMY_IDENTIFIER_TRIMMED) == true) {
            return true
        }

        val isShellScript = file.isShellScript()
        val currentModule = file.getModuleName()
        val modules = VlangModulesIndex.getAll(element.project)
        for (moduleFile in modules) {
            val moduleName = moduleFile.getModuleName()
            if (moduleName == currentModule || moduleName == VlangCodeInsightUtil.BUILTIN_MODULE) {
                continue
            }

            // already handled in `processOsModule`
            if (moduleName == "os" && isShellScript) {
                continue
            }

            // already handled in `processStubs`
            if (moduleName == "stubs") {
                continue
            }

            if (!processFileEntities(moduleFile, processor, state.put(MODULE_NAME, moduleFile.getModuleQualifiedName()), false)) {
                return false
            }
        }

        return true
    }

    private fun processGlobalVariables(file: VlangFile, processor: VlangScopeProcessor, state: ResolveState): Boolean {
        val imports = file.getImports()
        val importedModules = imports.map { it.importPath.qualifiedName }
        val globals = VlangGlobalVariablesIndex.getAll(project) { globalName ->
            importedModules.any { module -> globalName.startsWith("$module.") }
        }

        for (global in globals) {
            if (!processor.execute(global, state)) return false
        }

        return true
    }

    private fun processCompileTimeConstants(processor: VlangScopeProcessor, state: ResolveState): Boolean {
        if (!VlangCodeInsightUtil.insideCompileTime(identifier)) return true
        return processStubFile("compile_time_constants.v", processor, state)
    }

    private fun processImportedModulesForCompletion(file: VlangFile, processor: VlangScopeProcessor, state: ResolveState): Boolean {
        if (!processor.isCompletion()) {
            // This method is only for autocompletion when a user writes
            // a symbol (from another module) name, and we want to import
            // the symbol, and the module that contains it.
            return true
        }

        val currentModule = file.getModuleQualifiedName()
        val imports = file.getImports()

        imports
            .filter { it.importAlias == null }
            .map { it.importPath.lastPart }
            .flatMap { VlangModulesFingerprintIndex.find(it, element.project, null) }
            .forEach {
                if (it.getModuleQualifiedName() == currentModule) return@forEach
                if (!processor.execute(it, state)) return false
            }

        imports
            .mapNotNull { it.importAlias }
            .forEach {
                if (!processor.execute(it, state)) return false
            }

        imports
            .mapNotNull { it.selectiveImportList?.referenceExpressionList }
            .flatten()
            .mapNotNull { it.resolve() }
            .forEach {
                if (!processor.execute(it, state)) return false
            }

        return true
    }

    // TODO: redone
    private fun processImportSpec(file: VlangFile, processor: VlangScopeProcessor, state: ResolveState): Boolean {
        if (identifier?.parentOfType<VlangImportSpec>() == null) {
            return true
        }

        val spec = file.resolveImportSpec(identifier!!.text) ?: return false
        val resolved = spec.resolve()

        return resolved.any { module ->
            processor.execute(module.toPsi(), state.put(ACTUAL_NAME, module.name))
        }
    }

    private fun processFileEntities(
        file: VlangFile,
        processor: VlangScopeProcessor,
        state: ResolveState,
        localProcessing: Boolean,
    ): Boolean {

        if (!processNamedElements(
                processor,
                state,
                file.getFunctions(),
                Conditions.alwaysTrue(),
                localProcessing,
                false
            )
        ) return false

        if (!processNamedElements(
                processor,
                state,
                file.getStructs(),
                Conditions.alwaysTrue(),
                localProcessing,
                false
            )
        ) return false

        if (!processNamedElements(
                processor,
                state,
                file.getTypes(),
                Conditions.alwaysTrue(),
                localProcessing,
                false
            )
        ) return false

        if (!processNamedElements(
                processor,
                state,
                file.getEnums(),
                Conditions.alwaysTrue(),
                localProcessing,
                false
            )
        ) return false

        if (!processNamedElements(
                processor,
                state,
                file.getInterfaces(),
                Conditions.alwaysTrue(),
                localProcessing,
                false
            )
        ) return false

        if (!processNamedElements(
                processor,
                state,
                file.getConstants(),
                Conditions.alwaysTrue(),
                localProcessing,
                false
            )
        ) return false

        return processNamedElements(
            processor,
            state,
            file.getGlobalVariables(),
            Conditions.alwaysTrue(),
            localProcessing,
            false
        )
    }

    private fun processBlock(file: VlangFile, processor: VlangScopeProcessor, state: ResolveState, localResolve: Boolean): Boolean {
        val context = if (file is VlangCodeFragment) file.context else myElement

        val newState = if (file is VlangCodeFragment)
            state.put(PROCESS_PRIVATE_MEMBERS, true)
        else
            state

        val delegate = createDelegate(processor, file is VlangCodeFragment)
        ResolveUtil.treeWalkUp(context, delegate)
        return processNamedElements(processor, newState.put(NOT_PROCESS_EMBEDDED_DEFINITION, true), delegate.getVariants(), localResolve)
    }

    private fun processPseudoParams(processor: VlangScopeProcessor, state: ResolveState): Boolean {
        if (identifier == null) {
            return true
        }

        val isItVariable = VlangCodeInsightUtil.isItVariable(identifier!!)
        val isSortABVariable = VlangCodeInsightUtil.isSortABVariable(identifier!!)

        if (!isItVariable && !isSortABVariable) {
            return true
        }

        var callExpr = VlangCodeInsightUtil.getCallExpr(myElement) ?: return true
        while (PsiTreeUtil.isAncestor(callExpr.expression, myElement, false)) {
            callExpr = VlangCodeInsightUtil.getCallExpr(callExpr) ?: return true
        }

        val methodsName = if (isItVariable) arrayOf("filter", "map", "any", "all") else arrayOf("sort")
        while (!VlangCodeInsightUtil.isArrayMethodCall(callExpr, *methodsName)) {
            callExpr = VlangCodeInsightUtil.getCallExpr(callExpr) ?: return true
        }

        val resolved = callExpr.resolve() as? VlangSignatureOwner ?: return true
        val params = resolved.getSignature()?.parameters?.paramDefinitionList ?: return true

        val arguments = callExpr.argumentList.elementList
        val argument = arguments.find { PsiTreeUtil.isAncestor(it, myElement, false) }
        val index = arguments.indexOf(argument)
        val paramType = params.getOrNull(index)?.type?.toEx() ?: return true

        val functionType = paramType as? VlangFunctionTypeEx ?: return true
        val lambdaParams = functionType.signature.parameters.paramDefinitionList

        if (lambdaParams.size == 1 && isItVariable || lambdaParams.size == 2 && isSortABVariable) {
            val param = params.first { it.type.toEx() is VlangFunctionTypeEx }
            val functionTypeParam =
                (param.type.toEx() as VlangFunctionTypeEx).signature.parameters.paramDefinitionList.firstOrNull()
                    ?: return true

            val searchName = functionTypeParam.name ?: ""
            val newState = state.put(SEARCH_NAME, searchName).put(ACTUAL_NAME, searchName)

            return processor.execute(functionTypeParam, newState)
        }

        return true
    }

    private fun processTrailingStructParams(processor: VlangScopeProcessor, state: ResolveState): Boolean {
        val callExpr = VlangCodeInsightUtil.getCallExpr(myElement) ?: return true
        val resolved = callExpr.resolve() as? VlangSignatureOwner ?: return true
        val params = resolved.getSignature()?.parameters?.paramDefinitionList ?: return true

        val paramTypes = params.map { it.type.toEx() }
        if (!VlangCodeInsightUtil.isAllowedParamsForTrailingStruct(params, paramTypes)) return true

        val structType = paramTypes.last()
        val index = callExpr.paramIndexOf(element)
        if (index == -1) return true

        if (params.size > 1 && index < params.size - 1) return true

        return processType(structType, processor, state)
    }

    private fun processLiteralValueField(processor: VlangScopeProcessor, state: ResolveState): Boolean {
        val initExpr = element.parentOfType<VlangLiteralValueExpression>()
        val type = initExpr?.type?.toEx() ?: return true
        if (type is VlangArrayTypeEx) {
            return processType(VlangStructTypeEx.ArrayInit, processor, state)
        }
        if (type is VlangChannelTypeEx) {
            return processType(VlangStructTypeEx.ChanInit, processor, state)
        }
        return processType(type, processor, state)
    }

    private fun createDelegate(processor: VlangScopeProcessor, isCodeFragment: Boolean = false): VlangVarProcessor {
        return object : VlangVarProcessor(identifier!!, myElement, processor.isCompletion(), isCodeFragment) {
            override fun crossOff(e: PsiElement): Boolean {
                return if (e is VlangFieldDeclaration)
                    true
                else
                    super.crossOff(e)
            }
        }
    }

    private fun createContextOnElement(element: PsiElement): ResolveState {
        return ResolveState.initial().put(
            VlangPsiImplUtil.CONTEXT,
            SmartPointerManager.getInstance(element.project).createSmartPsiElementPointer(element)
        )
    }

    private fun createResolveProcessor(
        result: MutableCollection<ResolveResult>,
        reference: VlangReferenceExpressionBase,
    ): VlangScopeProcessor {
        return object : VlangScopeProcessor() {
            override fun execute(element: PsiElement, state: ResolveState): Boolean {
                if (element == reference) {
                    return !result.add(PsiElementResolveResult(element))
                }

                val name = state.get(ACTUAL_NAME) ?: when (element) {
                    is PsiNamedElement -> element.name
                    else               -> null
                }

                val ident = state.get(SEARCH_NAME) ?: reference.getIdentifier()?.text ?: return true

                if (name != null && ident == name) {
                    result.add(PsiElementResolveResult(element))
                    return false
                }
                return true
            }
        }
    }

    override fun isReferenceTo(element: PsiElement) = couldBeReferenceTo(element, myElement) && super.isReferenceTo(element)

    private fun couldBeReferenceTo(definition: PsiElement, reference: PsiElement): Boolean {
        if (definition is PsiDirectory && reference is VlangReferenceExpressionBase) return true
        if (reference is VlangLabelRef && definition !is VlangLabelDefinition) return false

        val definitionFile = definition.containingFile ?: return true
        val referenceFile = reference.containingFile

        val inSameFile = definitionFile.isEquivalentTo(referenceFile)
        if (inSameFile) return true
        return if (VlangCodeInsightUtil.sameModule(referenceFile, definitionFile))
            true
        else
            reference !is VlangNamedElement || !reference.isPublic()
    }

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult?> {
        return if (!myElement.isValid)
            ResolveResult.EMPTY_ARRAY
        else
            ResolveCache.getInstance(myElement.project)
                .resolveWithCaching(this, MY_RESOLVER, false, false)
    }

    override fun getVariants(): Array<Any> = ArrayUtil.EMPTY_OBJECT_ARRAY

    override fun handleElementRename(newElementName: String): PsiElement? {
        identifier?.replace(VlangElementFactory.createIdentifier(myElement.project, newElementName))
        return myElement
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VlangReference

        return element == other.element
    }

    override fun hashCode(): Int = element.hashCode()
}
