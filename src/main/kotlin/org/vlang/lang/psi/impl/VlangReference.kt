package org.vlang.lang.psi.impl

import com.intellij.codeInsight.completion.CompletionUtil
import com.intellij.openapi.util.Conditions
import com.intellij.openapi.util.RecursionManager
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.impl.source.resolve.ResolveCache
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.parentOfType
import com.intellij.util.ArrayUtil
import org.vlang.configurations.VlangConfiguration
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.ide.codeInsight.VlangTypeInferenceUtil
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangPsiImplUtil.processNamedElements
import org.vlang.lang.sql.VlangSqlUtil
import org.vlang.lang.stubs.index.VlangModulesFingerprintIndex
import org.vlang.lang.stubs.index.VlangModulesIndex

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

        fun isLocalResolve(origin: VlangFile, external: VlangFile): Boolean {
            val originModule = origin.getModuleQualifiedName()
            val externalModule = external.getModuleQualifiedName()
            return originModule == externalModule
        }
    }

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
        val state = createContextOnElement(myElement)
        val qualifier = myElement.getQualifier()
        return if (qualifier != null)
            processQualifierExpression(file, qualifier, processor, state)
        else
            processUnqualifiedResolve(file, processor, state)
    }

    private fun processQualifierExpression(
        file: VlangFile,
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
                val importSpec = when (val resolved = qualifier.resolve()) {
                    is VlangImportAlias -> resolved.parent
                    is VlangImportName  -> resolved.parent.parent
                    else                -> null
                }

                if (importSpec == null) {
                    val name = qualifier.getIdentifier().text
                    val moduleName = file.getModuleName()
                    if (name == moduleName) {
                        val moduleDir = file.parent
                        if (!processDirectory(moduleDir, null, null, processor, state, true)) {
                            return false
                        }

                        return true
                    }
                }

                if (importSpec is VlangImportSpec) {
                    if (processImportPath(importSpec.importPath, processor, state)) return false
                }
            }
        }

        if (qualifier is VlangImportPath) {
            if (processImportPath(qualifier, processor, state)) return false
        }

        if (qualifier is VlangTypeReferenceExpression) {
            val importSpec = when (val resolved = qualifier.resolve()) {
                is VlangImportAlias -> resolved.parent
                is VlangImportName  -> resolved.parent.parent
                else                -> null
            }

            if (importSpec is VlangImportSpec) {
                if (processImportPath(importSpec.importPath, processor, state)) return false
            }
        }

        return false
    }

    private fun processImportPath(
        importPath: VlangImportPath,
        processor: VlangScopeProcessor,
        state: ResolveState,
    ): Boolean {
        val moduleName = importPath.qualifiedName
        val moduleFiles =
            VlangModulesIndex.find(moduleName, myElement.project, GlobalSearchScope.allScope(myElement.project), null)

        if (moduleFiles.isNotEmpty()) {
            val moduleFile = moduleFiles.first()
            val moduleDir = moduleFile.parent
            val newState = state
                .put(MODULE_NAME, moduleFile.getModuleQualifiedName())
                .put(NEED_QUALIFIER_NAME, false)
            if (!processDirectory(moduleDir, null, null, processor, newState, false)) {
                return true
            }
        }
        return false
    }

    private fun processType(type: VlangType, processor: VlangScopeProcessor, state: ResolveState): Boolean {
        val result = RecursionManager.doPreventingRecursion(type, true) {
            if (!processExistingType(type, processor, state)) return@doPreventingRecursion false

            processTypeRef(type, processor, state)
        }
        return result == true
    }

    private fun processExistingType(type: VlangType, processor: VlangScopeProcessor, state: ResolveState): Boolean {
        val file = type.containingFile as? VlangFile ?: return true
        val contextFile = getContextFile(state) ?: myElement.containingFile
        if (contextFile !is VlangFile) {
            return true
        }

        val localResolve = isLocalResolve(contextFile, file)
        val newState = state.put(LOCAL_RESOLVE, localResolve)

        val typ = type.resolveType()

        if (typ is VlangAliasType) {
            if (!processMethods(typ, processor, newState, localResolve)) return false

            val types = typ.typeUnionList?.typeList ?: return true
            for (unionType in types) {
                if (!processType(unionType, processor, newState)) {
                    return false
                }
            }
        }

        if (typ is VlangPointerType) {
            val baseType = typ.type
            if (baseType != null) {
                return processType(baseType, processor, newState)
            }
        }

        if (typ is VlangNullableType) {
            val baseType = typ.type
            if (baseType != null) {
                return processType(baseType, processor, newState)
            }
        }

        if (typ is VlangNotNullableType) {
            val baseType = typ.type
            if (baseType != null) {
                return processType(baseType, processor, newState)
            }
        }

        if (typ is VlangStructType) {
            val isMethodRef = element.parent is VlangCallExpr

            if (!isMethodRef && !processNamedElements(processor, newState, typ.getFieldList(), localResolve)) return false
            if (!processMethods(typ, processor, newState, localResolve)) return false

            typ.embeddedStructList.forEach {
                if (!processType(it.type, processor, newState)) return false
            }

            val embedded = typ.embeddedStructList.mapNotNull { it.type.typeReferenceExpression?.resolve() as? VlangNamedElement }
            if (!processNamedElements(processor, newState, embedded, localResolve)) return false
        }

        if (typ is VlangInterfaceType) {
            val isMethodRef = element.parent is VlangCallExpr

            if (!isMethodRef && !processNamedElements(processor, newState, typ.getFieldList(), localResolve)) return false
            if (!processNamedElements(processor, newState, typ.methodList, localResolve)) return false
            if (!processMethods(typ, processor, newState, localResolve)) return false
        }

        if (typ is VlangEnumType) {
            if (!processNamedElements(processor, newState, typ.fieldList, localResolve)) return false
        }

        if (typ is VlangArrayType) {
            if (!processMethods(typ, processor, newState, localResolve)) return false

            val builtin = VlangConfiguration.getInstance(type.project).builtinLocation
            val arrayVirtualFile = builtin?.findChild("array.v") ?: return false
            val arrayFile = PsiManager.getInstance(typ.project).findFile(arrayVirtualFile) as? VlangFile ?: return false
            val arrayStruct = arrayFile.getStructs()
                .firstOrNull { it.name == "array" } ?: return false
            return processExistingType(arrayStruct.structType, processor, newState)
        }

        if (typ is VlangMapType) {
            if (!processMethods(typ, processor, newState, localResolve)) return false
        }

        return true
    }

    private fun processMethods(type: VlangType, processor: VlangScopeProcessor, state: ResolveState, localResolve: Boolean): Boolean {
        return processNamedElements(processor, state, VlangLangUtil.getMethodList(type), localResolve)
    }

    private fun processTypeRef(type: VlangType?, processor: VlangScopeProcessor, state: ResolveState): Boolean {
        if (type == null) {
            return true
        }
        return processInTypeRef(type.typeReferenceExpression, processor, state)
    }

    private fun processInTypeRef(e: VlangTypeReferenceExpression?, processor: VlangScopeProcessor, state: ResolveState): Boolean {
        val resolve = e?.resolve()
        if (resolve is VlangTypeOwner) {
            val type = resolve.getType(state) ?: return true
            if (!processType(type, processor, state)) return false
            return true
        }
        return true
    }

    private fun processUnqualifiedResolve(
        file: VlangFile,
        processor: VlangScopeProcessor,
        state: ResolveState,
    ): Boolean {
        if (identifier!!.textMatches("_")) {
            return processor.execute(myElement, state)
        }

        // expr or { err }
        if (identifier!!.text == "err" &&
            (VlangCodeInsightUtil.insideOrGuard(identifier!!) || VlangCodeInsightUtil.insideElseBlockIfGuard(identifier!!))
        ) {
            return !processBuiltin(processor, state.put(SEARCH_NAME, "IError"))
        }

        if (VlangSqlUtil.insideSql(identifier!!) && VlangSqlUtil.fieldReference(identifier!!)) {
            val resolved = VlangSqlUtil.getTable(identifier!!)?.typeReferenceExpression?.resolve() as? VlangStructDeclaration
            if (resolved != null) {
                return processType(resolved.structType, processor, state)
            }
        }

        when (val parent = myElement.parent) {
            is VlangEnumFetch -> {
                return processEnumFetch(parent, processor, state)
            }

            is VlangFieldName -> {
                if (!processTrailingStructParams(processor, state)) return false
                if (!processLiteralValueField(processor, state)) return false

                return true
            }
        }

        when (val grand = myElement.parent.parent) {
            is VlangImportSpec -> {
                val importPath = grand.importPath
                return processQualifierExpression(file, importPath, processor, state)
            }
        }

        if (element.parentOfType<VlangArgumentList>() != null) {
            if (!processPseudoParams(processor, state)) return false
        }

        if (!processBlock(processor, state, true)) return false
        if (!processBuiltin(processor, state)) return false
        if (!processImportModules(file, processor, state)) return false
        if (!processImportedModules(file, processor, state)) return false
        if (!processImports(file, processor, state, myElement)) return false
        if (!processFileEntities(file, processor, state, true)) return false
        if (!processDirectory(file.originalFile.parent, file, file.getModuleQualifiedName(), processor, state, true)) return false

        return processModulesEntities(file, processor, state)
    }

    private fun processEnumFetch(
        fetch: VlangEnumFetch,
        processor: VlangScopeProcessor,
        state: ResolveState,
    ): Boolean {
        val contextType = VlangTypeInferenceUtil.getContextType(fetch) ?: return true
        return processType(contextType, processor, state)
    }

    private fun processDirectory(
        dir: PsiDirectory?,
        file: VlangFile?,
        moduleName: String?,
        processor: VlangScopeProcessor,
        state: ResolveState,
        localProcessing: Boolean,
    ): Boolean {
        if (dir == null) {
            return true
        }

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

    // TODO: redone
    private fun processImports(
        file: VlangFile,
        processor: VlangScopeProcessor,
        state: ResolveState,
        element: VlangCompositeElement,
    ): Boolean {
        if (element.parentOfType<VlangImportSpec>() != null) {
            return true
        }

        val searchName = identifier!!.text
        val currentModuleName = file.getModuleName()
        if (searchName == currentModuleName) {
            val dir = file.parent ?: return true
            return !processor.execute(dir, state.put(ACTUAL_NAME, searchName))
        }

        val (name, import, isSelectiveImport) = file.resolveImportNameAndSpec(searchName)
        if (import == null) {
            return true
        }

        if (isSelectiveImport) {
            return !processQualifierExpression(file, import.importPath, processor, state)
        }

        if (searchName == name) {
            return !processor.execute(import.lastPartPsi, state)
        }

        if (import.importAlias != null) {
            return processor.execute(import.importAlias!!, state.put(ACTUAL_NAME, searchName))
        }

        return !processor.execute(import.lastPartPsi, state.put(ACTUAL_NAME, searchName))
    }

    private fun processModulesEntities(file: VlangFile, processor: VlangScopeProcessor, state: ResolveState): Boolean {
        if (!processor.isCompletion()) {
            // This method is only for autocompletion when a user writes
            // a symbol (from another module) name, and we want to import
            // the symbol, and the module that contains it.
            return true
        }

        if (identifier?.textMatches(CompletionUtil.DUMMY_IDENTIFIER_TRIMMED) == true) {
            return true
        }

        val currentModule = file.getModuleName()
        val modules = VlangModulesIndex.getAll(element.project)
        for (moduleFile in modules) {
            val moduleName = moduleFile.getModuleName()
            if (moduleName == currentModule || moduleName == VlangCodeInsightUtil.BUILTIN_MODULE) {
                continue
            }
            if (!processFileEntities(moduleFile, processor, state.put(MODULE_NAME, moduleFile.getModuleQualifiedName()), false)) {
                return false
            }
        }

        return true
    }

    private fun processImportedModules(file: VlangFile, processor: VlangScopeProcessor, state: ResolveState): Boolean {
        if (!processor.isCompletion()) {
            // This method is only for autocompletion when a user writes
            // a symbol (from another module) name, and we want to import
            // the symbol, and the module that contains it.
            return true
        }

        val currentModule = file.getModuleQualifiedName()
        file.getImports()
            .filter { it.importAlias == null }
            .map { it.importPath.qualifiedName }
            .flatMap { VlangModulesFingerprintIndex.find(it, element.project, null) }
            .forEach {
                if (it.getModuleQualifiedName() == currentModule) return@forEach
                if (!processor.execute(it, state)) return false
            }

        file.getImports()
            .mapNotNull { it.importAlias }
            .forEach {
                if (!processor.execute(it, state)) return false
            }

        return true
    }

    // TODO: redone
    private fun processImportModules(file: VlangFile, processor: VlangScopeProcessor, state: ResolveState): Boolean {
        if (identifier?.parentOfType<VlangImportSpec>() == null) {
            return true
        }

        val fqn = file.resolveImportName(identifier!!.text) ?: return false

        val modules = VlangModulesIndex.find(fqn, myElement.project, GlobalSearchScope.allScope(file.project), null)

        return modules.any {
            val module = it.getModule()
            val name = module?.name ?: return@any false
            val dir = it.parent ?: return@any false

            !processor.execute(dir, state.put(ACTUAL_NAME, name))
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

    private fun processBlock(processor: VlangScopeProcessor, state: ResolveState, localResolve: Boolean): Boolean {
        val delegate = createDelegate(processor)
        ResolveUtil.treeWalkUp(myElement, delegate)
        return processNamedElements(processor, state, delegate.getVariants(), localResolve)
    }

    private fun processPseudoParams(processor: VlangScopeProcessor, state: ResolveState): Boolean {
        if (identifier == null || !identifier!!.textMatches("it")) {
            return true
        }

        val callExpr = VlangCodeInsightUtil.getCallExpr(myElement) ?: return true
        val resolved = callExpr.resolve() as? VlangSignatureOwner ?: return true
        val params = resolved.getSignature()?.parameters?.paramDefinitionList ?: return true

        val paramType = VlangTypeInferenceUtil.getContextType(myElement)
        val functionType = paramType as? VlangFunctionType ?: return true
        val lambdaParams = functionType.getSignature()?.parameters?.paramDefinitionList ?: return true
        if (lambdaParams.size == 1) {
            val param = params.first { it.type is VlangFunctionType }
            val functionTypeParam = (param.type as VlangFunctionType).getSignature()?.parameters?.paramDefinitionList?.firstOrNull() ?: return true

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

        val paramTypes = params.map { it.type.resolveType() }
        if (!VlangCodeInsightUtil.isAllowedParamsForTrailingStruct(params, paramTypes)) return true

        val structType = paramTypes.last()
        val index = callExpr.paramIndexOf(element)
        if (index == -1) return true

        if (params.size > 1 && index < params.size - 1) return true

        return processType(structType, processor, state)
    }

    private fun processLiteralValueField(processor: VlangScopeProcessor, state: ResolveState): Boolean {
        val initExpr = element.parentOfType<VlangLiteralValueExpression>()
        val type = initExpr?.type ?: return true
        return processType(type, processor, state)
    }

    private fun createDelegate(processor: VlangScopeProcessor): VlangVarProcessor {
        return object : VlangVarProcessor(identifier!!, myElement, processor.isCompletion(), true) {
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
                    is PsiNamedElement   -> element.name
                    is VlangModuleClause -> element.name
                    else                 -> null
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

        val definitionFile = definition.containingFile
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
        identifier?.replace(VlangElementFactory.createIdentifierFromText(myElement.project, newElementName))
        return myElement
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VlangReference

        if (element != other.element) return false

        return true
    }

    override fun hashCode(): Int = element.hashCode()
}
