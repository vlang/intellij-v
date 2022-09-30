package org.vlang.lang.psi.impl

import com.intellij.openapi.util.Conditions
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.RecursionManager
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.impl.source.resolve.ResolveCache
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.parentOfType
import com.intellij.util.ArrayUtil
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangPsiImplUtil.processNamedElements
import org.vlang.lang.stubs.index.VlangModulesIndex
import org.vlang.lang.stubs.index.VlangNamesIndex
import org.vlang.sdk.VlangSdkUtil

class VlangReference(el: VlangReferenceExpressionBase) :
    VlangReferenceBase<VlangReferenceExpressionBase>(
        el,
        TextRange.from(
            el.getIdentifier()?.startOffsetInParent ?: 0,
            el.getIdentifier()?.textLength ?: el.textLength
        )
    ) {

    companion object {
        private val POINTER = Key.create<Any>("POINTER")

        private val MY_RESOLVER: ResolveCache.PolyVariantResolver<VlangReference> =
            ResolveCache.PolyVariantResolver { r: VlangReference, _: Boolean -> r.resolveInner() }

        private fun getContextElement(state: ResolveState?): PsiElement? {
            val context = state?.get(VlangPsiImplUtil.CONTEXT)
            return context?.element
        }

        fun getContextFile(state: ResolveState): PsiFile? {
            return getContextElement(state)?.containingFile
        }
    }

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

//        if (target is VlangImportSpec) {
//            if (target.isCImport()) return processor.execute(myElement, state)
//            target = target.getImportString().resolve()
//        }
//        if (target is PsiDirectory && !processDirectory(target, file, null, processor, state, false)) return false
//        if (target is VlangTypeOwner) {
//            val type = typeOrParameterType(target, createContextOnElement(myElement))
//
//            if (type != null) {
//                if (!processType(type, processor, state)) return false
////                val ref = getTypeRefExpression(type)
////                if (ref != null && ref.resolve() === ref) return processor.execute(
////                    myElement,
////                    state
////                ) // a bit hacky resolve for: var a C.foo; a.b
//            }
//        }
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
            val moduleDir = moduleFiles.first().parent
            if (!processDirectory(moduleDir, null, null, processor, state, false)) {
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
        val myFile = getContextFile(state) ?: myElement.containingFile
        if (myFile !is VlangFile) {
            return true
        }
        val localResolve = true //isLocalResolve(myFile, file)
//        val parent: VlangTypeSpec = getTypeSpecSafe(type)
//        val canProcessMethods = state.get<Any?>(com.Vlangide.psi.impl.VlangReference.DONT_PROCESS_METHODS) == null
//        if (canProcessMethods && parent != null && !processNamedElements(
//                processor,
//                state,
//                parent.getMethods(),
//                localResolve,
//                true
//            )
//        ) return false
//        if (type is VlangSpecType) {
//            type = type.getUnderlyingType()
//        }

        val typ = type.resolveType()

        if (typ is VlangPointerType) {
            val baseType = typ.type
            if (baseType != null) {
                return processType(baseType, processor, state)
            }
        }

        if (typ is VlangNullableType) {
            val baseType = typ.type
            if (baseType != null) {
                return processType(baseType, processor, state)
            }
        }

        if (typ is VlangNotNullableType) {
            val baseType = typ.type
            if (baseType != null) {
                return processType(baseType, processor, state)
            }
        }

        if (typ is VlangStructType) {
            val delegate = createDelegate(processor)
            typ.processDeclarations(delegate, ResolveState.initial(), null, myElement)
            val interfaceRefs = mutableListOf<VlangTypeReferenceExpression>()
            val structRefs = mutableListOf<VlangTypeReferenceExpression>()

            val groups = typ.fieldsGroupList
            val fields = groups.flatMap { it.fieldDeclarationList }.flatMap { it.fieldDefinitionList }

            if (!processNamedElements(processor, state, fields, localResolve)) return false

//            val method = VlangNamesIndex.find("main", "", myElement.project, myElement.resolveScope, null)

            val fqn = (typ.parent as VlangStructDeclaration).getQualifiedName()
//            val methodName = fqn + "." + identifier!!.text

            VlangNamesIndex.processPrefix("$fqn.", myElement.project, GlobalSearchScope.allScope(myElement.project), null) {
                if (!processor.execute(it, state)) return@processPrefix false
                true
            }

            if (!processCollectedRefs(
                    interfaceRefs,
                    processor,
                    state.put(POINTER, null)
                )
            ) return false
            if (!processCollectedRefs(structRefs, processor, state)) return false
        }

        if (typ is VlangEnumType) {
            val fields = typ.enumFields?.enumFieldDeclarationList?.map { it.enumFieldDefinition } ?: emptyList()
            if (!processNamedElements(processor, state, fields, localResolve)) return false
        }

        if (typ is VlangArrayOrSliceType) {
            val builtin = VlangSdkUtil.findBuiltinDir(typ)
            val arrayVirtualFile = builtin?.findChild("array.v") ?: return false
            val arrayFile = PsiManager.getInstance(typ.project).findFile(arrayVirtualFile) as? VlangFile ?: return false
            val arrayStruct = arrayFile.getStructs()
                .firstOrNull { it.name == "array" } ?: return false
            return processExistingType(arrayStruct.structType, processor, state)
        }

//        else if (state.get<Any?>(com.Vlangide.psi.impl.VlangReference.POINTER) == null && type is VlangInterfaceType) {
//            if (!processNamedElements(processor, state, (type as VlangInterfaceType).getMethods(), localResolve, true)) return false
//            if (!processCollectedRefs((type as VlangInterfaceType).getBaseTypesReferences(), processor, state)) return false
//        } else if (type is VlangFunctionType) {
//            val signature: VlangSignature = (type as VlangFunctionType).getSignature()
//            val result: VlangResult? = if (signature != null) signature.getResult() else null
//            val resultType: VlangType? = if (result != null) result.getType() else null
//            if (resultType != null && !processVlangType(resultType, processor, state)) return false
//        }
        return true
    }

    private fun processCollectedRefs(
        refs: List<VlangTypeReferenceExpression>,
        processor: VlangScopeProcessor,
        state: ResolveState,
    ): Boolean {
        for (ref in refs) {
            if (!processInTypeRef(ref, processor, state)) {
                return false
            }
        }
        return true
    }

    private fun processTypeRef(type: VlangType?, processor: VlangScopeProcessor, state: ResolveState): Boolean {
        if (type == null) {
            return true
        }
        return processInTypeRef(type.typeReferenceExpressionList.firstOrNull(), processor, state)
    }

    private fun processInTypeRef(e: VlangTypeReferenceExpression?, processor: VlangScopeProcessor, state: ResolveState): Boolean {
        val resolve = e?.resolve()
        if (resolve is VlangTypeOwner) {
            val type = resolve.getType(state) ?: return true
            if (!processType(type, processor, state)) return false

//            if (type is VlangSpecType) {
//                val inner: VlangType = (type as VlangSpecType).getType()
//                if (inner is VlangPointerType && state.get<Any?>(com.Vlangide.psi.impl.VlangReference.POINTER) != null) return true
//                if (inner != null && !processVlangType(
//                        inner,
//                        processor,
//                        state.put<Any>(com.Vlangide.psi.impl.VlangReference.DONT_PROCESS_METHODS, true)
//                    )
//                ) return false
//            }
            return true
        }
        return true
    }

    private fun typeOrParameterType(resolve: VlangTypeOwner, context: ResolveState?): VlangType? {
        return resolve.getType(context)
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
        if (identifier!!.text == "err" && identifier!!.parentOfType<VlangOrBlockExpr>() != null) {
            return !processBuiltin( processor, state.put(SEARCH_NAME, "IError"), myElement)
        }

        when (val parent = myElement.parent) {
            is VlangEnumFetch -> {
                if (!handleEnumFetch(parent, file, processor, state)) return false
            }

            is VlangFieldName -> {
                val initExpr = parent.parentOfType<VlangLiteralValueExpression>()
                val type = initExpr?.type ?: return true
                return processType(type, processor, state)
            }
        }

        when (val grand = myElement.parent.parent) {
            is VlangImportSpec -> {
                val importPath = grand.importPath
                return processQualifierExpression(file, importPath, processor, state)
            }
        }

        if (!processBlock(processor, state, true)) return false
        if (!processModules(file, processor, state, true)) return false
        if (!processImports(file, processor, state, myElement)) return false
        if (!processFileEntities(file, processor, state, true)) return false
        if (!processDirectory(file.originalFile.parent, file, file.packageName, processor, state, true)) return false

        return processBuiltin(processor, state, myElement)
    }

    private fun handleEnumFetch(
        fetch: VlangEnumFetch,
        file: VlangFile,
        processor: VlangScopeProcessor,
        state: ResolveState,
    ): Boolean {
        if (fetch.parent is VlangMatchArm) {
            val parentMatch = fetch.parentOfType<VlangMatchExpression>()
            if (parentMatch != null) {
                val matchExpression = parentMatch.expression as? VlangReferenceExpression ?: return true
                return processQualifierExpression(file, matchExpression, processor, state)
            }
        }
        if (fetch.parent is VlangDefaultFieldValue) {
            val fieldDeclaration = fetch.parent.parent
            if (fieldDeclaration is VlangFieldDeclaration) {
                // TODO: support multi fields
                val fieldDefinitionType = fieldDeclaration.type?.typeReferenceExpressionList?.firstOrNull() ?: return true
                return processQualifierExpression(file, fieldDefinitionType, processor, state)
            }
        }
        if (fetch.parent is VlangBinaryExpr) {
            val binaryExpr = fetch.parent as VlangBinaryExpr
            if (binaryExpr.right != null && binaryExpr.right!!.isEquivalentTo(fetch)) {
                val left = binaryExpr.left ?: return true
                return processQualifierExpression(file, left, processor, state)
            }
        }

        val parentAssign = fetch.parentOfType<VlangAssignmentStatement>()
        if (parentAssign != null) {
            // TODO: support multi assign
            val assignExpression = parentAssign.leftHandExprList.expressionList.firstOrNull() as? VlangReferenceExpression ?: return true
            return processQualifierExpression(file, assignExpression, processor, state)
        }

        return true
    }

    protected fun processDirectory(
        dir: PsiDirectory?,
        file: VlangFile?,
        packageName: String?,
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
            if (packageName != null && packageName != f.packageName) {
                continue
            }
            if (!processFileEntities(f, processor, state, localProcessing)) {
                return false
            }
        }

        return true
    }

    private fun processBuiltin(processor: VlangScopeProcessor, state: ResolveState, element: VlangReferenceExpressionBase?): Boolean {
        val builtin = VlangSdkUtil.findBuiltinDir(element!!) ?: return true
        val psiManager = PsiManager.getInstance(element.project)
        builtin.children
            .map { psiManager.findFile(it) }
            .filterIsInstance<VlangFile>()
            .filter { !it.isTestFile() }
            .forEach {
                val res = processFileEntities(it, processor, state, true)
                if (!res)
                    return false
            }

        return true
    }

    // TODO: redone
    protected fun processImports(
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

    // TODO: redone
    private fun processModules(file: VlangFile, processor: VlangScopeProcessor, state: ResolveState, localResolve: Boolean): Boolean {
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

    protected fun processFileEntities(
        file: VlangFile,
        processor: VlangScopeProcessor,
        state: ResolveState,
        localProcessing: Boolean,
    ): Boolean {

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
                file.getUnions(),
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
            file.getFunctions(),
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

    protected fun createResolveProcessor(
        result: MutableCollection<ResolveResult>,
        reference: VlangReferenceExpressionBase,
    ): VlangScopeProcessor {
        return object : VlangScopeProcessor() {
            override fun execute(element: PsiElement, state: ResolveState): Boolean {
                // TODO: only public
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

    override fun isReferenceTo(element: PsiElement) = couldBeReferenceTo(element, myElement) && super.isReferenceTo(element);

    private fun couldBeReferenceTo(definition: PsiElement, reference: PsiElement): Boolean {
        if (definition is PsiDirectory && reference is VlangReferenceExpressionBase) return true
        if (reference is VlangLabelRef && definition !is VlangLabelDefinition) return false

        val definitionFile = definition.containingFile
        val referenceFile = reference.containingFile

        val inSameFile = definitionFile.isEquivalentTo(referenceFile)
        if (inSameFile) return true
        return if (inSamePackage(referenceFile, definitionFile))
            true
        else
            reference !is VlangNamedElement || !reference.isPublic()
    }

    private fun inSamePackage(firstFile: PsiFile, secondFile: PsiFile): Boolean {
        val containingDirectory = firstFile.containingDirectory
        if (containingDirectory == null || containingDirectory != secondFile.containingDirectory) {
            return false
        }
        if (firstFile is VlangFile && secondFile is VlangFile) {
            val referencePackage = firstFile.packageName
            val definitionPackage = secondFile.packageName
            return referencePackage != null && referencePackage == definitionPackage
        }
        return true
    }

    private val identifier: PsiElement?
        get() = myElement?.getIdentifier()

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
