package org.vlang.lang.psi.impl

import com.intellij.openapi.diagnostic.Logger
import org.vlang.lang.psi.VlangReferenceExpression

object VlangPsiImplUtil {
    private val LOG = Logger.getInstance(
        VlangPsiImplUtil::class.java
    )
//    private val CONTEXT = Key.create<SmartPsiElementPointer<PsiElement>>("CONTEXT")
//    fun goTraverser(): SyntaxTraverser<PsiElement> {
//        return SyntaxTraverser.psiTraverser()
//            .forceDisregardTypes(Conditions.equalTo(GeneratedParserUtilBase.DUMMY_BLOCK))
//    }
//
//    fun builtin(resolve: PsiElement?): Boolean {
//        return resolve != null && isBuiltinFile(resolve.containingFile)
//    }
//
//    fun isConversionExpression(expression: VlangExpression?): Boolean {
//        if (expression is VlangConversionExpr) {
//            return true
//        }
//        var referenceExpression: VlangReferenceExpression? = null
//        if (expression is VlangCallExpr) {
//            referenceExpression =
//                ObjectUtils.tryCast((expression as VlangCallExpr?).getExpression(), VlangReferenceExpression::class.java)
//        } else if (expression is VlangBuiltinCallExpr) {
//            referenceExpression = (expression as VlangBuiltinCallExpr?).getReferenceExpression()
//        }
//        return referenceExpression != null && referenceExpression.resolve() is VlangTypeSpec
//    }
//
//    fun isPanic(o: VlangCallExpr): Boolean {
//        return stdLibCall(o, "panic")
//    }
//
//    fun isRecover(o: VlangCallExpr): Boolean {
//        return stdLibCall(o, "recover")
//    }
//
//    private fun stdLibCall(o: VlangCallExpr, name: String): Boolean {
//        val e: VlangExpression = o.getExpression()
//        if (e.textMatches(name) && e is VlangReferenceExpression) {
//            val reference: PsiReference = e.getReference()
//            val resolve = if (reference != null) reference.resolve() else null
//            return if (resolve !is VlangFunctionDeclaration) false else isBuiltinFile(resolve!!.containingFile)
//        }
//        return false
//    }
//
//    fun isBuiltinFile(file: PsiFile?): Boolean {
//        return (file is VlangFile
//                && VlangConstants.BUILTIN_PACKAGE_NAME.equals((file as VlangFile?).getPackageName())
//                && VlangConstants.BUILTIN_PACKAGE_NAME.equals((file as VlangFile?).getImportPath(false))
//                && VlangConstants.BUILTIN_FILE_NAME.equals(file!!.name))
//    }
//
//    fun getTopLevelDeclaration(startElement: PsiElement?): VlangTopLevelDeclaration? {
//        val declaration: VlangTopLevelDeclaration =
//            PsiTreeUtil.getTopmostParentOfType(startElement, VlangTopLevelDeclaration::class.java)
//        return if (declaration == null || declaration.getParent() !is VlangFile) null else declaration
//    }
//
//    fun getArity(s: VlangSignature?): Int {
//        return if (s == null) -1 else s.getParameters().getParameterDeclarationList().size()
//    }
//
//    fun getContextElement(state: ResolveState?): PsiElement? {
//        val context = state?.get(CONTEXT)
//        return context?.element
//    }
//
//    fun createContextOnElement(element: PsiElement): ResolveState {
//        return ResolveState.initial()
//            .put(CONTEXT, SmartPointerManager.getInstance(element.project).createSmartPsiElementPointer(element))
//    }
//
//    fun getQualifier(o: VlangTypeReferenceExpression): VlangTypeReferenceExpression? {
//        return PsiTreeUtil.getChildOfType(o, VlangTypeReferenceExpression::class.java)
//    }
//
//    fun resolve(importString: VlangImportString): PsiDirectory? {
//        val references: Array<PsiReference> = importString.getReferences()
//        for (reference in references) {
//            if (reference is FileReferenceOwner) {
//                val lastFileReference = (reference as FileReferenceOwner).lastFileReference
//                val result = lastFileReference?.resolve()
//                return if (result is PsiDirectory) result else null
//            }
//        }
//        return null
//    }
//
//    fun getReference(o: VlangTypeReferenceExpression): PsiReference {
//        return VlangTypeReference(o)
//    }
//
//    fun getReference(o: VlangLabelRef): PsiReference {
//        return VlangLabelReference(o)
//    }
//
//    fun getReference(o: VlangVarDefinition): PsiReference? {
//        val shortDeclaration: VlangShortVarDeclaration = PsiTreeUtil.getParentOfType(o, VlangShortVarDeclaration::class.java)
//        val createRef = PsiTreeUtil.getParentOfType(
//            shortDeclaration, VlangBlock::class.java, VlangForStatement::class.java, VlangIfStatement::class.java,
//            VlangSwitchStatement::class.java, VlangSelectStatement::class.java
//        ) is VlangBlock
//        return if (createRef) VlangVarReference(o) else null
//    }
//
    @JvmStatic
    fun getReference(o: VlangReferenceExpression): VlangReference {
        return VlangReference(o)
    }

//
//    fun getReference(o: VlangFieldName): PsiReference {
//        val field = VlangFieldNameReference(o)
//        val ordinal = VlangReference(o)
//        return object : PsiMultiReference(arrayOf(field, ordinal), o) {
//            override fun resolve(): PsiElement? {
//                val resolve: PsiElement = field.resolve()
//                return resolve ?: if (field.inStructTypeKey()) null else ordinal.resolve()
//            }
//
//            override fun isReferenceTo(element: PsiElement): Boolean {
//                return VlangUtil.couldBeReferenceTo(element, getElement()) && getElement().manager.areElementsEquivalent(
//                    resolve(),
//                    element
//                )
//            }
//        }
//    }
//
//    fun getQualifier(o: VlangFieldName): VlangReferenceExpression? {
//        return null
//    }
//
//    fun getReferences(o: VlangImportString): Array<PsiReference> {
//        return if (o.getTextLength() < 2) PsiReference.EMPTY_ARRAY else VlangImportReferenceSet(o).getAllReferences()
//    }
//
//    fun getQualifier(o: VlangReferenceExpression): VlangReferenceExpression? {
//        return PsiTreeUtil.getChildOfType(o, VlangReferenceExpression::class.java)
//    }
//
//    fun processDeclarations(
//        o: VlangCompositeElement,
//        processor: PsiScopeProcessor,
//        state: ResolveState,
//        lastParent: PsiElement?,
//        place: PsiElement
//    ): Boolean {
//        val isAncestor = PsiTreeUtil.isAncestor(o, place, false)
//        if (o is VlangVarSpec) {
//            return isAncestor || VlangCompositeElementImpl.processDeclarationsDefault(
//                o,
//                processor,
//                state,
//                lastParent,
//                place
//            )
//        }
//        if (isAncestor) return VlangCompositeElementImpl.processDeclarationsDefault(o, processor, state, lastParent, place)
//        return if (o is VlangBlock ||
//            o is VlangIfStatement ||
//            o is VlangForStatement ||
//            o is VlangCommClause ||
//            o is VlangFunctionLit ||
//            o is VlangTypeCaseClause ||
//            o is VlangExprCaseClause
//        ) {
//            processor.execute(o, state)
//        } else VlangCompositeElementImpl.processDeclarationsDefault(o, processor, state, lastParent, place)
//    }
//
//    fun getVlangTypeInner(o: VlangReceiver, context: ResolveState?): VlangType? {
//        return o.getType()
//    }
//
//    fun getIdentifier(o: VlangAnonymousFieldDefinition): PsiElement? {
//        val expression: VlangTypeReferenceExpression = o.getTypeReferenceExpression()
//        return if (expression != null) expression.getIdentifier() else null
//    }
//
//    fun getName(packageClause: VlangPackageClause): String? {
//        val stub: VlangPackageClauseStub = packageClause.getStub()
//        if (stub != null) return stub.getName()
//        val packageIdentifier: PsiElement = packageClause.getIdentifier()
//        return if (packageIdentifier != null) packageIdentifier.text.trim { it <= ' ' } else null
//    }
//
//    fun getName(o: VlangAnonymousFieldDefinition): String? {
//        val identifier: PsiElement = o.getIdentifier()
//        return if (identifier != null) identifier.text else null
//    }
//
//    fun getTypeReferenceExpression(o: VlangAnonymousFieldDefinition): VlangTypeReferenceExpression? {
//        return getTypeRefExpression(o.getType())
//    }
//
//    fun getVlangTypeInner(
//        o: VlangAnonymousFieldDefinition,
//        context: ResolveState?
//    ): VlangType? {
//        return o.getType()
//    }
//
//    fun getName(o: VlangMethodSpec): String? {
//        val stub: VlangNamedStub<VlangMethodSpec> = o.getStub()
//        if (stub != null) {
//            return stub.getName()
//        }
//        val identifier: PsiElement = o.getIdentifier()
//        if (identifier != null) return identifier.text
//        val typeRef: VlangTypeReferenceExpression = o.getTypeReferenceExpression()
//        return if (typeRef != null) typeRef.getIdentifier().getText() else null
//    }
//
//    fun getReceiverType(o: VlangMethodDeclaration): VlangType? {
//        val receiver: VlangReceiver = o.getReceiver()
//        return if (receiver == null) null else receiver.getType()
//    }
//
//    // todo: merge with {@link this#getTypeRefExpression}
//    fun getTypeReference(o: VlangType?): VlangTypeReferenceExpression? {
//        if (o is VlangPointerType) {
//            return PsiTreeUtil.findChildOfAnyType(o, VlangTypeReferenceExpression::class.java)
//        }
//        return if (o != null) o.getTypeReferenceExpression() else null
//    }
//
//    // todo: merge with {@link this#getTypeReference}
//    fun getTypeRefExpression(type: VlangType?): VlangTypeReferenceExpression? {
//        val unwrap: VlangType? = unwrapPointerIfNeeded(type)
//        return if (unwrap != null) unwrap.getTypeReferenceExpression() else null
//    }
//
//    fun getVlangTypeInner(o: VlangConstDefinition, context: ResolveState?): VlangType? {
//        val fromSpec: VlangType? = findTypeInConstSpec(o)
//        return if (fromSpec != null) fromSpec else RecursionManager.doPreventingRecursion(
//            o,
//            true,
//            label@ NullableComputable<VlangType> {
//                var prev: VlangConstSpec? = PsiTreeUtil.getPrevSiblingOfType(o.getParent(), VlangConstSpec::class.java)
//                while (prev != null) {
//                    val type: VlangType = prev.getType()
//                    if (type != null) return@label type
//                    val expr: VlangExpression =
//                        ContainerUtil.getFirstItem(prev.getExpressionList()) // not sure about first
//                    if (expr != null) return@label expr.getVlangType(context)
//                    prev = PsiTreeUtil.getPrevSiblingOfType(prev, VlangConstSpec::class.java)
//                }
//                null
//            } as NullableComputable<VlangType?>?)
//        // todo: stubs
//    }
//
//    private fun findTypeInConstSpec(o: VlangConstDefinition): VlangType? {
//        val stub: VlangConstDefinitionStub = o.getStub()
//        val parent = PsiTreeUtil.getStubOrPsiParent(o) as? VlangConstSpec ?: return null
//        val spec: VlangConstSpec = parent as VlangConstSpec
//        val commonType: VlangType = spec.getType()
//        if (commonType != null) return commonType
//        val varList: List<VlangConstDefinition> = spec.getConstDefinitionList()
//        val i = Math.max(varList.indexOf(o), 0)
//        if (stub != null) return null
//        val specStub: VlangConstSpecStub = spec.getStub()
//        val es: List<VlangExpression> =
//            if (specStub != null) specStub.getExpressionList() else spec.getExpressionList() // todo: move to constant spec
//        return if (es.size <= i) null else es[i].getVlangType(null)
//    }
//
//    private fun unwrapParType(o: VlangExpression, c: ResolveState?): VlangType? {
//        val inner: VlangType = getVlangTypeInner(o, c)
//        return if (inner is VlangParType) (inner as VlangParType).getActualType() else inner
//    }
//
//    fun getVlangType(o: VlangExpression, context: ResolveState?): VlangType? {
//        return RecursionManager.doPreventingRecursion(o, true, Computable<T> {
//            if (context != null) return@doPreventingRecursion unwrapParType(o, context)
//            CachedValuesManager.getCachedValue(o) {
//                CachedValueProvider.Result
//                    .create(unwrapParType(o, createContextOnElement(o)), PsiModificationTracker.MODIFICATION_COUNT)
//            }
//        })
//    }
//
//    private fun getVlangTypeInner(o: VlangExpression, context: ResolveState?): VlangType? {
//        if (o is VlangUnaryExpr) {
//            val u: VlangUnaryExpr = o as VlangUnaryExpr
//            val e: VlangExpression = u.getExpression() ?: return null
//            val type: VlangType = e.getVlangType(context)
//            val base: VlangType =
//                if (type == null || type.getTypeReferenceExpression() == null) type else type.getUnderlyingType()
//            if (u.getBitAnd() != null) return if (type != null) LightPointerType(type) else null
//            if (u.getSendChannel() != null) return if (base is VlangChannelType) (base as VlangChannelType).getType() else type
//            return if (u.getMul() != null) if (base is VlangPointerType) (base as VlangPointerType).getType() else type else type
//        }
//        if (o is VlangAddExpr) {
//            return (o as VlangAddExpr).getLeft().getVlangType(context)
//        }
//        if (o is VlangMulExpr) {
//            val left: VlangExpression = (o as VlangMulExpr).getLeft()
//            if (left !is VlangLiteral) return left.getVlangType(context)
//            val right: VlangExpression = (o as VlangBinaryExpr).getRight()
//            if (right != null) return right.getVlangType(context)
//        } else if (o is VlangCompositeLit) {
//            val type: VlangType = (o as VlangCompositeLit).getType()
//            if (type != null) return type
//            val expression: VlangTypeReferenceExpression = (o as VlangCompositeLit).getTypeReferenceExpression()
//            return if (expression != null) expression.resolveType() else null
//        } else if (o is VlangFunctionLit) {
//            return LightFunctionType(o as VlangFunctionLit)
//        } else if (o is VlangBuiltinCallExpr) {
//            val text: String = (o as VlangBuiltinCallExpr).getReferenceExpression().getText()
//            val isNew = "new" == text
//            val isMake = "make" == text
//            if (isNew || isMake) {
//                val args: VlangBuiltinArgumentList = (o as VlangBuiltinCallExpr).getBuiltinArgumentList()
//                val type: VlangType? = if (args != null) args.getType() else null
//                return if (isNew) if (type == null) null else LightPointerType(type) else type
//            }
//        } else if (o is VlangCallExpr) {
//            val e: VlangExpression = (o as VlangCallExpr).getExpression()
//            if (e is VlangReferenceExpression) { // todo: unify Type processing
//                val ref: PsiReference = e.getReference()
//                val resolve = if (ref != null) ref.resolve() else null
//                if ((e as VlangReferenceExpression).getQualifier() == null && "append" == (e as VlangReferenceExpression).getIdentifier()
//                        .getText()
//                ) {
//                    if (resolve is VlangFunctionDeclaration && isBuiltinFile(resolve!!.containingFile)) {
//                        val l: List<VlangExpression> = (o as VlangCallExpr).getArgumentList().getExpressionList()
//                        val f: VlangExpression = ContainerUtil.getFirstItem(l)
//                        return if (f == null) null else getVlangType(f, context)
//                    }
//                } else if (resolve === e) { // C.call()
//                    return VlangCType(e)
//                }
//            }
//            val type: VlangType = (o as VlangCallExpr).getExpression().getVlangType(context)
//            if (type is VlangFunctionType) {
//                return funcType(type)
//            }
//            val byRef: VlangType? =
//                if (type != null && type.getTypeReferenceExpression() != null) type.getUnderlyingType() else null
//            return if (byRef is VlangFunctionType) {
//                funcType(byRef)
//            } else type
//        } else if (o is VlangReferenceExpression) {
//            val reference: PsiReference = o.getReference()
//            val resolve = if (reference != null) reference.resolve() else null
//            if (resolve is VlangTypeOwner) return typeOrParameterType(resolve as VlangTypeOwner?, context)
//        } else if (o is VlangParenthesesExpr) {
//            val expression: VlangExpression = (o as VlangParenthesesExpr).getExpression()
//            return if (expression != null) expression.getVlangType(context) else null
//        } else if (o is VlangSelectorExpr) {
//            val item: VlangExpression = ContainerUtil.getLastItem((o as VlangSelectorExpr).getExpressionList())
//            return if (item != null) item.getVlangType(context) else null
//        } else if (o is VlangIndexOrSliceExpr) {
//            val referenceType: VlangType? = getIndexedExpressionReferenceType(o as VlangIndexOrSliceExpr, context)
//            if (o.getNode()
//                    .findChildByType(VlangTypes.COLON) != null
//            ) return referenceType // means slice expression, todo: extract if needed
//            val type: VlangType? = if (referenceType != null) referenceType.getUnderlyingType() else null
//            if (type is VlangMapType) {
//                val list: List<VlangType> = (type as VlangMapType?).getTypeList()
//                if (list.size == 2) {
//                    return list[1]
//                }
//            } else if (type is VlangArrayOrSliceType) {
//                return (type as VlangArrayOrSliceType?).getType()
//            } else if (VlangTypeUtil.isString(type)) {
//                return getBuiltinType("byte", o)
//            }
//        } else if (o is VlangTypeAssertionExpr) {
//            return (o as VlangTypeAssertionExpr).getType()
//        } else if (o is VlangConversionExpr) {
//            return (o as VlangConversionExpr).getType()
//        } else if (o is VlangStringLiteral) {
//            return getBuiltinType("string", o)
//        } else if (o is VlangLiteral) {
//            val l: VlangLiteral = o as VlangLiteral
//            if (l.getChar() != null) return getBuiltinType("rune", o)
//            if (l.getInt() != null || l.getHex() != null || (o as VlangLiteral).getOct() != null) return getBuiltinType(
//                "int",
//                o
//            )
//            if (l.getFloat() != null) return getBuiltinType("float64", o)
//            if (l.getFloati() != null) return getBuiltinType("complex64", o)
//            if (l.getDecimali() != null) return getBuiltinType("complex128", o)
//        } else if (o is VlangConditionalExpr) {
//            return getBuiltinType("bool", o)
//        }
//        return null
//    }
//
//    fun getIndexedExpressionReferenceType(o: VlangIndexOrSliceExpr, context: ResolveState?): VlangType? {
//        val first: VlangExpression = ContainerUtil.getFirstItem(o.getExpressionList())
//        // todo: calculate type for indexed expressions only
//        // https://golang.org/ref/spec#Index_expressions – a[x] is shorthand for (*a)[x]
//        return unwrapPointerIfNeeded(if (first != null) first.getVlangType(context) else null)
//    }
//
//    fun unwrapPointerIfNeeded(type: VlangType?): VlangType? {
//        return if (type is VlangPointerType) (type as VlangPointerType?).getType() else type
//    }
//
//    fun getBuiltinType(name: String, context: PsiElement): VlangType? {
//        val builtin: VlangFile = VlangSdkUtil.findBuiltinFile(context)
//        if (builtin != null) {
//            val spec: VlangTypeSpec = ContainerUtil.find(builtin.getTypes()) { spec1 -> name == spec1.getName() }
//            if (spec != null) {
//                return spec.getSpecType().getType() // todo
//            }
//        }
//        return null
//    }
//
//    private fun typeFromRefOrType(t: VlangType?): VlangType? {
//        if (t == null) return null
//        val tr: VlangTypeReferenceExpression = t.getTypeReferenceExpression()
//        return if (tr != null) tr.resolveType() else t
//    }
//
//    fun typeOrParameterType(resolve: VlangTypeOwner, context: ResolveState?): VlangType? {
//        val type: VlangType = resolve.getVlangType(context)
//        if (resolve is VlangParamDefinition && (resolve as VlangParamDefinition).isVariadic()) {
//            return if (type == null) null else LightArrayType(type)
//        }
//        return if (resolve is VlangSignatureOwner) {
//            LightFunctionType(resolve as VlangSignatureOwner)
//        } else type
//    }
//
//    fun resolve(o: VlangReferenceExpression): PsiElement? { // todo: replace with default method in VlangReferenceExpressionBase
//        return o.getReference().resolve()
//    }
//
//    fun resolve(o: VlangTypeReferenceExpression): PsiElement? { // todo: replace with default method in VlangReferenceExpressionBase
//        return o.getReference().resolve()
//    }
//
//    fun resolve(o: VlangFieldName): PsiElement? { // todo: replace with default method in VlangReferenceExpressionBase
//        return o.getReference().resolve()
//    }
//
//    fun getLiteralType(context: PsiElement?, considerLiteralValue: Boolean): VlangType? {
//        val lit: VlangCompositeLit = PsiTreeUtil.getNonStrictParentOfType(context, VlangCompositeLit::class.java)
//            ?: return null
//        var type: VlangType? = lit.getType()
//        if (type == null) {
//            val ref: VlangTypeReferenceExpression = lit.getTypeReferenceExpression()
//            val resolve: VlangType? = if (ref != null) ref.resolveType() else null
//            type = if (resolve != null) resolve.getUnderlyingType() else null
//        }
//        if (!considerLiteralValue) {
//            return type
//        }
//        val parentVlangValue: VlangValue? = getParentVlangValue(context!!)
//        var literalValue: PsiElement? = PsiTreeUtil.getParentOfType(context, VlangLiteralValue::class.java)
//        while (literalValue != null) {
//            if (literalValue === lit) break
//            if (literalValue is VlangLiteralValue) {
//                type = calcLiteralType(parentVlangValue, type)
//            }
//            literalValue = literalValue.parent
//        }
//        return type
//    }
//
//    fun getParentVlangValue(element: PsiElement): VlangValue? {
//        var place = element
//        while (PsiTreeUtil.getParentOfType(place, VlangLiteralValue::class.java).also { place = it } != null) {
//            if (place.parent is VlangValue) {
//                return place.parent as VlangValue
//            }
//        }
//        return null
//    }
//
//    // todo: rethink and unify this algorithm
//    private fun calcLiteralType(parentVlangValue: VlangValue?, type: VlangType?): VlangType? {
//        var type: VlangType? = type ?: return null
//        type = findLiteralType(parentVlangValue, type)
//        if (type is VlangParType) {
//            type = (type as VlangParType?).getActualType()
//        }
//        if (type != null && type.getTypeReferenceExpression() != null) {
//            type = type.getUnderlyingType()
//        }
//        if (type is VlangPointerType) {
//            val inner: VlangType = (type as VlangPointerType?).getType()
//            if (inner != null && inner.getTypeReferenceExpression() != null) {
//                type = inner.getUnderlyingType()
//            }
//        }
//        return if (type is VlangSpecType) (type as VlangSpecType?).getType() else type
//    }
//
//    private fun findLiteralType(parentVlangValue: VlangValue?, type: VlangType?): VlangType? {
//        var type: VlangType? = type
//        val inValue = parentVlangValue != null
//        if (inValue && type is VlangArrayOrSliceType) {
//            type = (type as VlangArrayOrSliceType?).getType()
//        } else if (type is VlangMapType) {
//            type = if (inValue) (type as VlangMapType?).getValueType() else (type as VlangMapType?).getKeyType()
//        } else if (inValue && type is VlangSpecType) {
//            val inner: VlangType = (type as VlangSpecType?).getType()
//            if (inner is VlangArrayOrSliceType) {
//                type = (inner as VlangArrayOrSliceType).getType()
//            } else if (inner is VlangStructType) {
//                val key: VlangKey = PsiTreeUtil.getPrevSiblingOfType(parentVlangValue, VlangKey::class.java)
//                val field: VlangFieldName? = if (key != null) key.getFieldName() else null
//                val resolve: PsiElement? = if (field != null) field.resolve() else null
//                if (resolve is VlangFieldDefinition) {
//                    type = PsiTreeUtil.getNextSiblingOfType(resolve, VlangType::class.java)
//                }
//            }
//        }
//        return type
//    }
//
//    fun resolveType(expression: VlangTypeReferenceExpression): VlangType? {
//        val resolve: PsiElement = expression.resolve()
//        if (resolve is VlangTypeSpec) return (resolve as VlangTypeSpec).getSpecType()
//        // hacky C resolve
//        return if (resolve === expression) VlangCType(expression) else null
//    }
//
//    fun isVariadic(o: VlangParamDefinition): Boolean {
//        val parent: PsiElement = o.getParent()
//        return parent is VlangParameterDeclaration && (parent as VlangParameterDeclaration).isVariadic()
//    }
//
//    fun isVariadic(o: VlangParameterDeclaration): Boolean {
//        val stub: VlangParameterDeclarationStub = o.getStub()
//        return if (stub != null) stub.isVariadic() else o.getTripleDot() != null
//    }
//
//    fun hasVariadic(argumentList: VlangArgumentList): Boolean {
//        return argumentList.getTripleDot() != null
//    }
//
//    fun getVlangTypeInner(o: VlangTypeSpec, context: ResolveState?): VlangType? {
//        return o.getSpecType()
//    }
//
//    fun getVlangTypeInner(o: VlangVarDefinition, context: ResolveState?): VlangType? {
//        // see http://golang.org/ref/spec#RangeClause
//        val parent = PsiTreeUtil.getStubOrPsiParent(o)
//        if (parent is VlangRangeClause) {
//            return processRangeClause(o, parent as VlangRangeClause?, context)
//        }
//        if (parent is VlangVarSpec) {
//            return findTypeInVarSpec(o, context)
//        }
//        val literal: VlangCompositeLit = PsiTreeUtil.getNextSiblingOfType(o, VlangCompositeLit::class.java)
//        if (literal != null) {
//            return literal.getType()
//        }
//        val siblingType: VlangType = o.findSiblingType()
//        if (siblingType != null) return siblingType
//        if (parent is VlangTypeSwitchGuard) {
//            val switchStatement: VlangTypeSwitchStatement =
//                ObjectUtils.tryCast(parent!!.parent, VlangTypeSwitchStatement::class.java)
//            if (switchStatement != null) {
//                val typeCase: VlangTypeCaseClause? = getTypeCaseClause(getContextElement(context), switchStatement)
//                return if (typeCase != null) {
//                    if (typeCase.getDefault() != null) (parent as VlangTypeSwitchGuard?).getExpression()
//                        .getVlangType(context) else typeCase.getType()
//                } else (parent as VlangTypeSwitchGuard?).getExpression().getVlangType(null)
//            }
//        }
//        return null
//    }
//
//    fun isVoid(result: VlangResult): Boolean {
//        val type: VlangType = result.getType()
//        if (type != null) return false
//        val parameters: VlangParameters = result.getParameters()
//        return parameters == null || parameters.getParameterDeclarationList().isEmpty()
//    }
//
//    private fun getTypeCaseClause(context: PsiElement?, switchStatement: VlangTypeSwitchStatement): VlangTypeCaseClause? {
//        return SyntaxTraverser.psiApi().parents(context).takeWhile(Conditions.notEqualTo(switchStatement))
//            .filter(VlangTypeCaseClause::class.java).last()
//    }
//
//    private fun findTypeInVarSpec(o: VlangVarDefinition, context: ResolveState?): VlangType? {
//        val parent: VlangVarSpec = PsiTreeUtil.getStubOrPsiParent(o) as VlangVarSpec? ?: return null
//        val commonType: VlangType = parent.getType()
//        if (commonType != null) return commonType
//        val varList: List<VlangVarDefinition> = parent.getVarDefinitionList()
//        var i = varList.indexOf(o)
//        i = if (i == -1) 0 else i
//        val exprs: List<VlangExpression> = parent.getRightExpressionsList()
//        if (exprs.size == 1 && exprs[0] is VlangCallExpr) {
//            val call: VlangExpression = exprs[0]
//            val fromCall: VlangType = call.getVlangType(context)
//            val canDecouple = varList.size > 1
//            val underlyingType: VlangType =
//                if (canDecouple && fromCall is VlangSpecType) (fromCall as VlangSpecType).getType() else fromCall
//            val byRef: VlangType? = typeFromRefOrType(underlyingType)
//            val type: VlangType =
//                funcType(if (canDecouple && byRef is VlangSpecType) (byRef as VlangSpecType?).getType() else byRef)
//                    ?: return fromCall
//            if (type is VlangTypeList) {
//                if ((type as VlangTypeList).getTypeList().size() > i) {
//                    return (type as VlangTypeList).getTypeList().get(i)
//                }
//            }
//            return type
//        }
//        return if (exprs.size <= i) null else exprs[i].getVlangType(context)
//    }
//
//    private fun funcType(type: VlangType?): VlangType? {
//        if (type is VlangFunctionType) {
//            val signature: VlangSignature = (type as VlangFunctionType?).getSignature()
//            val result: VlangResult? = if (signature != null) signature.getResult() else null
//            if (result != null) {
//                val rt: VlangType = result.getType()
//                if (rt != null) return rt
//                val parameters: VlangParameters = result.getParameters()
//                if (parameters != null) {
//                    val list: List<VlangParameterDeclaration> = parameters.getParameterDeclarationList()
//                    val types: MutableList<VlangType> = ContainerUtil.newArrayListWithCapacity<VlangType>(list.size)
//                    for (declaration in list) {
//                        val declarationType: VlangType = declaration.getType()
//                        for (ignored in declaration.getParamDefinitionList()) {
//                            types.add(declarationType)
//                        }
//                    }
//                    if (!types.isEmpty()) {
//                        return if (types.size == 1) types[0] else LightTypeList(parameters, types)
//                    }
//                }
//            }
//            return null
//        }
//        return type
//    }
//
//    /**
//     * https://golang.org/ref/spec#RangeClause
//     */
//    private fun processRangeClause(o: VlangVarDefinition, parent: VlangRangeClause, context: ResolveState?): VlangType? {
//        val rangeExpression: VlangExpression = parent.getRangeExpression()
//        if (rangeExpression != null) {
//            val varList: List<VlangVarDefinition> = parent.getVarDefinitionList()
//            val type: VlangType? = unwrapIfNeeded(rangeExpression.getVlangType(context))
//            if (type is VlangChannelType) return (type as VlangChannelType).getType()
//            var i = varList.indexOf(o)
//            i = if (i == -1) 0 else i
//            if (type is VlangArrayOrSliceType && i == 1) return (type as VlangArrayOrSliceType).getType()
//            if (type is VlangMapType) {
//                val list: List<VlangType> = (type as VlangMapType).getTypeList()
//                if (i == 0) return ContainerUtil.getFirstItem(list)
//                if (i == 1) return ContainerUtil.getLastItem(list)
//            }
//            if (VlangTypeUtil.isString(type)) {
//                return getBuiltinType("int32", o)
//            }
//        }
//        return null
//    }
//
//    private fun unwrapIfNeeded(type: VlangType?): VlangType? {
//        var type: VlangType? = type
//        type = unwrapPointerIfNeeded(type)
//        return if (type != null) type.getUnderlyingType() else null
//    }
//
//    fun getActualType(o: VlangParType): VlangType {
//        return ObjectUtils.notNull(
//            SyntaxTraverser.psiTraverser(o).filter(
//                Conditions.notInstanceOf(
//                    VlangParType::class.java
//                )
//            )
//                .filter(VlangType::class.java).first(), o.getType()
//        )
//    }
//
//    fun getText(o: VlangType?): String {
//        if (o == null) return ""
//        if (o is VlangPointerType && (o as VlangPointerType).getType() is VlangSpecType) {
//            return "*" + getText((o as VlangPointerType).getType())
//        }
//        if (o is VlangSpecType) {
//            val fqn = getFqn(getTypeSpecSafe(o))
//            if (fqn != null) {
//                return fqn
//            }
//        } else if (o is VlangStructType) {
//            return if ((o as VlangStructType).getFieldDeclarationList().isEmpty()) "struct{}" else "struct {...}"
//        } else if (o is VlangInterfaceType) {
//            return if ((o as VlangInterfaceType).getMethodSpecList().isEmpty()) "interface{}" else "interface {...}"
//        }
//        val text: String = o.getText() ?: return ""
//        return text.replace("\\s+".toRegex(), " ")
//    }
//
//    private fun getFqn(typeSpec: VlangTypeSpec?): String? {
//        if (typeSpec == null) return null
//        val name: String = typeSpec.getName()
//        val file: VlangFile = typeSpec.getContainingFile()
//        val packageName: String = file.getPackageName()
//        return if (name != null) {
//            if (!isBuiltinFile(file)) getFqn(packageName, name) else name
//        } else null
//    }
//
//    fun getFqn(packageName: String?, elementName: String): String {
//        return if (StringUtil.isNotEmpty(packageName)) "$packageName.$elementName" else elementName
//    }
//
//    fun getMethods(o: VlangInterfaceType): List<VlangMethodSpec> {
//        return ContainerUtil.filter(o.getMethodSpecList()) { spec -> spec.getIdentifier() != null }
//    }
//
//    fun getBaseTypesReferences(o: VlangInterfaceType): List<VlangTypeReferenceExpression> {
//        val refs: List<VlangTypeReferenceExpression> = ContainerUtil.newArrayList<VlangTypeReferenceExpression>()
//        o.accept(object : VlangRecursiveVisitor() {
//            fun visitMethodSpec(o: VlangMethodSpec) {
//                ContainerUtil.addIfNotNull(refs, o.getTypeReferenceExpression())
//            }
//        })
//        return refs
//    }
//
//    fun getMethods(o: VlangTypeSpec): List<VlangMethodDeclaration> {
//        return CachedValuesManager.getCachedValue(o) {
//            CachedValueProvider.Result.create(
//                calcMethods(o),
//                PsiModificationTracker.MODIFICATION_COUNT
//            )
//        }
//    }
//
//    fun allowed(declarationFile: PsiFile, referenceFile: PsiFile?, contextModule: Module?): Boolean {
//        if (declarationFile !is VlangFile) {
//            return false
//        }
//        val referenceVirtualFile = referenceFile?.originalFile?.virtualFile
//        if (!allowed(declarationFile.virtualFile, referenceVirtualFile)) {
//            return false
//        }
//        return if (VlangConstants.DOCUMENTATION.equals((declarationFile as VlangFile).getPackageName())) {
//            false
//        } else VlangUtil.matchedForModuleBuildTarget(declarationFile, contextModule)
//    }
//
//    fun allowed(declarationFile: VirtualFile?, referenceFile: VirtualFile?): Boolean {
//        if (declarationFile == null) {
//            return true
//        }
//        return if (VlangUtil.fileToIgnore(declarationFile.name)) {
//            false
//        } else referenceFile == null || !VlangTestFinder.isTestFile(declarationFile) || VlangTestFinder.isTestFile(
//            referenceFile
//        ) && Comparing.equal(
//            referenceFile.parent,
//            declarationFile.parent
//        )
//        // it's not a test or context file is also test from the same package
//    }
//
//    fun processNamedElements(
//        processor: PsiScopeProcessor,
//        state: ResolveState,
//        elements: Collection<VlangNamedElement?>,
//        localResolve: Boolean
//    ): Boolean {
//        return processNamedElements(processor, state, elements, Condition.TRUE, localResolve, false)
//    }
//
//    fun processNamedElements(
//        processor: PsiScopeProcessor,
//        state: ResolveState,
//        elements: Collection<VlangNamedElement?>,
//        localResolve: Boolean,
//        checkContainingFile: Boolean
//    ): Boolean {
//        return processNamedElements(processor, state, elements, Condition.TRUE, localResolve, checkContainingFile)
//    }
//
//    fun processNamedElements(
//        processor: PsiScopeProcessor,
//        state: ResolveState,
//        elements: Collection<VlangNamedElement?>,
//        condition: Condition<VlangNamedElement?>,
//        localResolve: Boolean,
//        checkContainingFile: Boolean
//    ): Boolean {
//        val contextFile: PsiFile? = if (checkContainingFile) VlangReference.getContextFile(state) else null
//        val module: Module? = if (contextFile != null) ModuleUtilCore.findModuleForPsiElement(contextFile) else null
//        for (definition in elements) {
//            if (!condition.value(definition)) continue
//            if (!definition.isValid() || checkContainingFile && !allowed(
//                    definition.getContainingFile(),
//                    contextFile,
//                    module
//                )
//            ) continue
//            if ((localResolve || definition.isPublic()) && !processor.execute(definition, state)) return false
//        }
//        return true
//    }
//
//    fun processSignatureOwner(o: VlangSignatureOwner, processor: VlangScopeProcessorBase): Boolean {
//        val signature: VlangSignature = o.getSignature() ?: return true
//        if (!processParameters(processor, signature.getParameters())) return false
//        val result: VlangResult = signature.getResult()
//        val resultParameters: VlangParameters? = if (result != null) result.getParameters() else null
//        return resultParameters == null || processParameters(processor, resultParameters)
//    }
//
//    private fun processParameters(processor: VlangScopeProcessorBase, parameters: VlangParameters): Boolean {
//        for (declaration in parameters.getParameterDeclarationList()) {
//            if (!processNamedElements(
//                    processor,
//                    ResolveState.initial(),
//                    declaration.getParamDefinitionList(),
//                    true
//                )
//            ) return false
//        }
//        return true
//    }
//
//    fun joinPsiElementText(items: List<PsiElement?>?): String {
//        return StringUtil.join(items, { obj: PsiElement -> obj.text }, ", ")
//    }
//
//    fun getBreakStatementOwner(breakStatement: PsiElement): PsiElement? {
//        val owner: VlangCompositeElement = PsiTreeUtil.getParentOfType(
//            breakStatement, VlangSwitchStatement::class.java, VlangForStatement::class.java,
//            VlangSelectStatement::class.java, VlangFunctionLit::class.java
//        )
//        return if (owner is VlangFunctionLit) null else owner
//    }
//
//    private fun calcMethods(o: VlangTypeSpec): List<VlangMethodDeclaration> {
//        val file: PsiFile = o.getContainingFile().getOriginalFile()
//        if (file is VlangFile) {
//            val packageName: String = (file as VlangFile).getPackageName()
//            val typeName: String = o.getName()
//            if (StringUtil.isEmpty(packageName) || StringUtil.isEmpty(typeName)) return emptyList<VlangMethodDeclaration>()
//            val key = "$packageName.$typeName"
//            val project: Project = (file as VlangFile).getProject()
//            val scope: GlobalSearchScope = VlangPackageUtil.packageScope(file as VlangFile)
//            val declarations: Collection<VlangMethodDeclaration> =
//                VlangMethodIndex.find(key, project, scope, VlangIdFilter.getFilesFilter(scope))
//            return ContainerUtil.newArrayList(declarations)
//        }
//        return emptyList<VlangMethodDeclaration>()
//    }
//
//    fun getUnderlyingType(o: VlangType): VlangType {
//        val type: VlangType = RecursionManager.doPreventingRecursion(o, true, Computable<T> { getTypeInner(o) })
//        return ObjectUtils.notNull(type, o)
//    }
//
//    private fun getTypeInner(o: VlangType): VlangType {
//        if ((o is VlangArrayOrSliceType
//                    or o is VlangStructType
//            or o is VlangPointerType
//                    or o is VlangFunctionType
//                    or o is VlangInterfaceType
//                    or o is VlangMapType
//                    or o is VlangChannelType)){
//            return o
//        }
//        if (o is VlangParType) return (o as VlangParType).getActualType()
//        if (o is VlangSpecType) {
//            val type: VlangType = (o as VlangSpecType).getType()
//            return if (type != null) type.getUnderlyingType() else o
//        }
//        if (builtin(o)) return o
//        val e: VlangTypeReferenceExpression = o.getTypeReferenceExpression()
//        val byRef: VlangType? = if (e == null) null else e.resolveType()
//        return if (byRef != null) {
//            byRef.getUnderlyingType()
//        } else o
//    }
//
//    fun getVlangTypeInner(o: VlangSignatureOwner, context: ResolveState?): VlangType? {
//        val signature: VlangSignature = o.getSignature()
//        val result: VlangResult? = if (signature != null) signature.getResult() else null
//        if (result != null) {
//            val type: VlangType = result.getType()
//            if (type is VlangTypeList && (type as VlangTypeList).getTypeList().size() === 1) {
//                return (type as VlangTypeList).getTypeList().get(0)
//            }
//            if (type != null) return type
//            val parameters: VlangParameters = result.getParameters()
//            if (parameters != null) {
//                val parametersType: VlangType = parameters.getType()
//                if (parametersType != null) return parametersType
//                val composite: MutableList<VlangType> = ContainerUtil.newArrayList<VlangType>()
//                for (p in parameters.getParameterDeclarationList()) {
//                    for (definition in p.getParamDefinitionList()) {
//                        composite.add(definition.getVlangType(context))
//                    }
//                }
//                return if (composite.size == 1) composite[0] else LightTypeList(parameters, composite)
//            }
//        }
//        return null
//    }
//
//    fun addImport(importList: VlangImportList, packagePath: String, alias: String?): VlangImportSpec {
//        val project: Project = importList.getProject()
//        val newDeclaration: VlangImportDeclaration =
//            VlangElementFactory.createImportDeclaration(project, packagePath, alias, false)
//        val existingImports: List<VlangImportDeclaration> = importList.getImportDeclarationList()
//        for (i in existingImports.indices.reversed()) {
//            val existingImport: VlangImportDeclaration = existingImports[i]
//            val importSpecList: List<VlangImportSpec> = existingImport.getImportSpecList()
//            if (importSpecList.isEmpty()) {
//                continue
//            }
//            if (existingImport.getRparen() == null && importSpecList[0].isCImport()) {
//                continue
//            }
//            return existingImport.addImportSpec(packagePath, alias)
//        }
//        return addImportDeclaration(importList, newDeclaration)
//    }
//
//    private fun addImportDeclaration(
//        importList: VlangImportList,
//        newImportDeclaration: VlangImportDeclaration
//    ): VlangImportSpec {
//        val lastImport: VlangImportDeclaration =
//            ContainerUtil.getLastItem(importList.getImportDeclarationList())
//        val importDeclaration: VlangImportDeclaration =
//            importList.addAfter(newImportDeclaration, lastImport) as VlangImportDeclaration
//        val importListNextSibling: PsiElement = importList.getNextSibling()
//        if (importListNextSibling !is PsiWhiteSpace) {
//            importList.addAfter(VlangElementFactory.createNewLine(importList.getProject()), importDeclaration)
//            if (importListNextSibling != null) {
//                // double new line if there is something valuable after import list
//                importList.addAfter(VlangElementFactory.createNewLine(importList.getProject()), importDeclaration)
//            }
//        }
//        importList.addBefore(VlangElementFactory.createNewLine(importList.getProject()), importDeclaration)
//        return ContainerUtil.getFirstItem(importDeclaration.getImportSpecList())!!
//    }
//
//    fun addImportSpec(declaration: VlangImportDeclaration, packagePath: String, alias: String?): VlangImportSpec {
//        var declaration: VlangImportDeclaration = declaration
//        val rParen: PsiElement = declaration.getRparen()
//        if (rParen == null) {
//            val newDeclaration: VlangImportDeclaration =
//                VlangElementFactory.createEmptyImportDeclaration(declaration.getProject())
//            for (spec in declaration.getImportSpecList()) {
//                newDeclaration.addImportSpec(spec.getPath(), spec.getAlias())
//            }
//            declaration = declaration.replace(newDeclaration) as VlangImportDeclaration
//            LOG.assertTrue(declaration.getRparen() != null)
//            return declaration.addImportSpec(packagePath, alias)
//        }
//        declaration.addBefore(VlangElementFactory.createNewLine(declaration.getProject()), rParen)
//        val newImportSpace: VlangImportSpec =
//            VlangElementFactory.createImportSpec(declaration.getProject(), packagePath, alias)
//        val spec: VlangImportSpec = declaration.addBefore(newImportSpace, rParen) as VlangImportSpec
//        declaration.addBefore(VlangElementFactory.createNewLine(declaration.getProject()), rParen)
//        return spec
//    }
//
//    fun getLocalPackageName(importPath: String): String {
//        val fileName = if (!StringUtil.endsWithChar(importPath, '/') && !StringUtil.endsWithChar(
//                importPath,
//                '\\'
//            )
//        ) PathUtil.getFileName(importPath) else ""
//        var name: StringBuilder? = null
//        for (i in 0 until fileName.length) {
//            val c = fileName[i]
//            if (!(Character.isLetter(c) || c == '_' || i != 0 && Character.isDigit(c))) {
//                if (name == null) {
//                    name = StringBuilder(fileName.length)
//                    name.append(fileName, 0, i)
//                }
//                name.append('_')
//            } else name?.append(c)
//        }
//        return name?.toString() ?: fileName
//    }
//
//    fun getLocalPackageName(importSpec: VlangImportSpec): String {
//        return getLocalPackageName(importSpec.getPath())
//    }
//
//    fun isCImport(importSpec: VlangImportSpec): Boolean {
//        return VlangConstants.C_PATH.equals(importSpec.getPath())
//    }
//
//    fun isDot(importSpec: VlangImportSpec): Boolean {
//        val stub: VlangImportSpecStub = importSpec.getStub()
//        return if (stub != null) stub.isDot() else importSpec.getDot() != null
//    }
//
//    fun getPath(importSpec: VlangImportSpec): String {
//        val stub: VlangImportSpecStub = importSpec.getStub()
//        return if (stub != null) stub.getPath() else importSpec.getImportString().getPath()
//    }
//
//    fun getName(importSpec: VlangImportSpec): String? {
//        return getAlias(importSpec)
//    }
//
//    fun getAlias(importSpec: VlangImportSpec): String? {
//        val stub: VlangImportSpecStub = importSpec.getStub()
//        if (stub != null) {
//            return stub.getAlias()
//        }
//        val identifier: PsiElement = importSpec.getIdentifier()
//        if (identifier != null) {
//            return identifier.text
//        }
//        return if (importSpec.isDot()) "." else null
//    }
//
//    fun getImportQualifierToUseInFile(importSpec: VlangImportSpec?, defaultValue: String?): String? {
//        if (importSpec == null || importSpec.isForSideEffects()) {
//            return null
//        }
//        if (importSpec.isDot()) {
//            return ""
//        }
//        val alias: String = importSpec.getAlias()
//        return alias ?: (defaultValue ?: importSpec.getLocalPackageName())
//    }
//
//    fun shouldVlangDeeper(o: VlangImportSpec?): Boolean {
//        return false
//    }
//
//    fun shouldVlangDeeper(o: VlangTypeSpec?): Boolean {
//        return false
//    }
//
//    fun shouldVlangDeeper(o: VlangType): Boolean {
//        return o is VlangInterfaceType || o is VlangStructType
//    }
//
//    fun isForSideEffects(o: VlangImportSpec): Boolean {
//        return "_" == o.getAlias()
//    }
//
//    fun getPath(o: VlangImportString): String {
//        return o.getStringLiteral().getDecodedText()
//    }
//
//    fun unquote(s: String?): String {
//        if (StringUtil.isEmpty(s)) return ""
//        val quote = s!![0]
//        val startOffset = if (isQuote(quote)) 1 else 0
//        var endOffset = s.length
//        if (s.length > 1) {
//            val lastChar = s[s.length - 1]
//            if (isQuote(quote) && lastChar == quote) {
//                endOffset = s.length - 1
//            }
//            if (!isQuote(quote) && isQuote(lastChar)) {
//                endOffset = s.length - 1
//            }
//        }
//        return s.substring(startOffset, endOffset)
//    }
//
//    fun getPathTextRange(importString: VlangImportString): TextRange {
//        val text: String = importString.getText()
//        return if (!text.isEmpty() && isQuote(text[0])) TextRange.create(1, text.length - 1) else TextRange.EMPTY_RANGE
//    }
//
//    fun isQuotedImportString(s: String): Boolean {
//        return s.length > 1 && isQuote(s[0]) && s[0] == s[s.length - 1]
//    }
//
//    private fun isQuote(ch: Char): Boolean {
//        return ch == '"' || ch == '\'' || ch == '`'
//    }
//
//    fun isValidHost(o: VlangStringLiteral): Boolean {
//        return PsiTreeUtil.getParentOfType(o, VlangImportString::class.java) == null
//    }
//
//    fun updateText(o: VlangStringLiteral, text: String): VlangStringLiteralImpl {
//        var text = text
//        if (text.length > 2) {
//            if (o.getString() != null) {
//                val outChars = StringBuilder()
//                VlangStringLiteralEscaper.escapeString(text.substring(1, text.length - 1), outChars)
//                outChars.insert(0, '"')
//                outChars.append('"')
//                text = outChars.toString()
//            }
//        }
//        val valueNode: ASTNode = o.getNode().getFirstChildNode()
//        assert(valueNode is LeafElement)
//        (valueNode as LeafElement).replaceWithText(text)
//        return o as VlangStringLiteralImpl
//    }
//
//    fun createLiteralTextEscaper(o: VlangStringLiteral): VlangStringLiteralEscaper {
//        return VlangStringLiteralEscaper(o)
//    }
//
//    fun prevDot(e: PsiElement?): Boolean {
//        val prev = if (e == null) null else PsiTreeUtil.prevVisibleLeaf(e)
//        return prev is LeafElement && (prev as LeafElement).elementType === VlangTypes.DOT
//    }
//
//    fun resolveCall(call: VlangExpression?): VlangSignatureOwner? {
//        return ObjectUtils.tryCast(resolveCallRaw(call), VlangSignatureOwner::class.java)
//    }
//
//    fun resolveCallRaw(call: VlangExpression?): PsiElement? {
//        if (call !is VlangCallExpr) return null
//        val e: VlangExpression = (call as VlangCallExpr?).getExpression()
//        if (e is VlangSelectorExpr) {
//            val right: VlangExpression = (e as VlangSelectorExpr).getRight()
//            val reference: PsiReference? = if (right is VlangReferenceExpression) right.getReference() else null
//            return reference?.resolve()
//        }
//        if (e is VlangCallExpr) {
//            val resolve: VlangSignatureOwner? = resolveCall(e)
//            if (resolve != null) {
//                val signature: VlangSignature = resolve.getSignature()
//                val result: VlangResult? = if (signature != null) signature.getResult() else null
//                return if (result != null) result.getType() else null
//            }
//            return null
//        }
//        if (e is VlangFunctionLit) {
//            return e
//        }
//        val r: VlangReferenceExpression =
//            if (e is VlangReferenceExpression) e as VlangReferenceExpression else PsiTreeUtil.getChildOfType(
//                e,
//                VlangReferenceExpression::class.java
//            )
//        val reference: PsiReference = (if (r != null) r else e).getReference()
//        return if (reference != null) reference.resolve() else null
//    }
//
//    fun isUnaryBitAndExpression(parent: PsiElement?): Boolean {
//        val grandParent = parent?.parent
//        return grandParent is VlangUnaryExpr && (grandParent as VlangUnaryExpr?).getBitAnd() != null
//    }
//
//    fun addSpec(
//        declaration: VlangVarDeclaration,
//        name: String,
//        type: String?,
//        value: String?,
//        specAnchor: VlangVarSpec?
//    ): VlangVarSpec {
//        var declaration: VlangVarDeclaration = declaration
//        var specAnchor: VlangVarSpec? = specAnchor
//        val project: Project = declaration.getProject()
//        val newSpec: VlangVarSpec = VlangElementFactory.createVarSpec(project, name, type, value)
//        var rParen: PsiElement = declaration.getRparen()
//        if (rParen == null) {
//            val item: VlangVarSpec = ContainerUtil.getFirstItem(declaration.getVarSpecList())!!
//            val updateAnchor = specAnchor === item
//            declaration = declaration.replace(
//                VlangElementFactory.createVarDeclaration(
//                    project,
//                    "(" + item.getText() + ")"
//                )
//            ) as VlangVarDeclaration
//            rParen = declaration.getRparen()
//            if (updateAnchor) {
//                specAnchor = ContainerUtil.getFirstItem(declaration.getVarSpecList())
//            }
//        }
//        assert(rParen != null)
//        val anchor = ObjectUtils.notNull<PsiElement>(specAnchor, rParen)
//        if (!hasNewLineBefore(anchor)) {
//            declaration.addBefore(VlangElementFactory.createNewLine(declaration.getProject()), anchor)
//        }
//        val spec: VlangVarSpec = declaration.addBefore(newSpec, anchor) as VlangVarSpec
//        declaration.addBefore(VlangElementFactory.createNewLine(declaration.getProject()), anchor)
//        return spec
//    }
//
//    fun addSpec(
//        declaration: VlangConstDeclaration,
//        name: String,
//        type: String?,
//        value: String?,
//        specAnchor: VlangConstSpec?
//    ): VlangConstSpec {
//        var declaration: VlangConstDeclaration = declaration
//        var specAnchor: VlangConstSpec? = specAnchor
//        val project: Project = declaration.getProject()
//        val newSpec: VlangConstSpec = VlangElementFactory.createConstSpec(project, name, type, value)
//        var rParen: PsiElement = declaration.getRparen()
//        if (rParen == null) {
//            val item: VlangConstSpec = ContainerUtil.getFirstItem(declaration.getConstSpecList())!!
//            val updateAnchor = specAnchor === item
//            declaration = declaration.replace(
//                VlangElementFactory.createConstDeclaration(
//                    project,
//                    "(" + item.getText() + ")"
//                )
//            ) as VlangConstDeclaration
//            rParen = declaration.getRparen()
//            if (updateAnchor) {
//                specAnchor = ContainerUtil.getFirstItem(declaration.getConstSpecList())
//            }
//        }
//        assert(rParen != null)
//        val anchor = ObjectUtils.notNull<PsiElement>(specAnchor, rParen)
//        if (!hasNewLineBefore(anchor)) {
//            declaration.addBefore(VlangElementFactory.createNewLine(declaration.getProject()), anchor)
//        }
//        val spec: VlangConstSpec = declaration.addBefore(newSpec, anchor) as VlangConstSpec
//        declaration.addBefore(VlangElementFactory.createNewLine(declaration.getProject()), anchor)
//        return spec
//    }
//
//    fun deleteSpec(declaration: VlangVarDeclaration, specToDelete: VlangVarSpec) {
//        val specList: List<VlangVarSpec> = declaration.getVarSpecList()
//        val index = specList.indexOf(specToDelete)
//        assert(index >= 0)
//        if (specList.size == 1) {
//            declaration.delete()
//            return
//        }
//        specToDelete.delete()
//    }
//
//    fun deleteSpec(declaration: VlangConstDeclaration, specToDelete: VlangConstSpec) {
//        val specList: List<VlangConstSpec> = declaration.getConstSpecList()
//        val index = specList.indexOf(specToDelete)
//        assert(index >= 0)
//        if (specList.size == 1) {
//            declaration.delete()
//            return
//        }
//        specToDelete.delete()
//    }
//
//    fun deleteExpressionFromAssignment(
//        assignment: VlangAssignmentStatement,
//        expressionToDelete: VlangExpression
//    ) {
//        val expressionValue: VlangExpression? = getRightExpression(assignment, expressionToDelete)
//        if (expressionValue != null) {
//            if (assignment.getExpressionList().size() === 1) {
//                assignment.delete()
//            } else {
//                deleteElementFromCommaSeparatedList(expressionToDelete)
//                deleteElementFromCommaSeparatedList(expressionValue)
//            }
//        }
//    }
//
//    fun deleteDefinition(spec: VlangVarSpec, definitionToDelete: VlangVarDefinition) {
//        val definitionList: List<VlangVarDefinition> = spec.getVarDefinitionList()
//        val index = definitionList.indexOf(definitionToDelete)
//        assert(index >= 0)
//        if (definitionList.size == 1) {
//            val parent: PsiElement = spec.getParent()
//            if (parent is VlangVarDeclaration) {
//                (parent as VlangVarDeclaration).deleteSpec(spec)
//            } else {
//                spec.delete()
//            }
//            return
//        }
//        val value: VlangExpression = definitionToDelete.getValue()
//        if (value != null && spec.getRightExpressionsList().size() <= 1) {
//            val assign: PsiElement = spec.getAssign()
//            if (assign != null) {
//                assign.delete()
//            }
//        }
//        deleteElementFromCommaSeparatedList(value)
//        deleteElementFromCommaSeparatedList(definitionToDelete)
//    }
//
//    fun deleteDefinition(spec: VlangConstSpec, definitionToDelete: VlangConstDefinition) {
//        val definitionList: List<VlangConstDefinition> = spec.getConstDefinitionList()
//        val index = definitionList.indexOf(definitionToDelete)
//        assert(index >= 0)
//        if (definitionList.size == 1) {
//            val parent: PsiElement = spec.getParent()
//            if (parent is VlangConstDeclaration) {
//                (parent as VlangConstDeclaration).deleteSpec(spec)
//            } else {
//                spec.delete()
//            }
//            return
//        }
//        val value: VlangExpression = definitionToDelete.getValue()
//        if (value != null && spec.getExpressionList().size() <= 1) {
//            val assign: PsiElement = spec.getAssign()
//            if (assign != null) {
//                assign.delete()
//            }
//        }
//        deleteElementFromCommaSeparatedList(value)
//        deleteElementFromCommaSeparatedList(definitionToDelete)
//    }
//
//    private fun deleteElementFromCommaSeparatedList(element: PsiElement?) {
//        if (element == null) {
//            return
//        }
//        val prevVisibleLeaf = PsiTreeUtil.prevVisibleLeaf(element)
//        val nextVisibleLeaf = PsiTreeUtil.nextVisibleLeaf(element)
//        if (prevVisibleLeaf != null && prevVisibleLeaf.textMatches(",")) {
//            prevVisibleLeaf.delete()
//        } else if (nextVisibleLeaf != null && nextVisibleLeaf.textMatches(",")) {
//            nextVisibleLeaf.delete()
//        }
//        element.delete()
//    }
//
//    private fun hasNewLineBefore(anchor: PsiElement): Boolean {
//        var prevSibling = anchor.prevSibling
//        while (prevSibling is PsiWhiteSpace) {
//            if (prevSibling.textContains('\n')) {
//                return true
//            }
//            prevSibling = prevSibling.getPrevSibling()
//        }
//        return false
//    }
//
//    fun getValue(definition: VlangVarDefinition): VlangExpression? {
//        val parent: PsiElement = definition.getParent()
//        if (parent is VlangVarSpec) {
//            val index: Int = (parent as VlangVarSpec).getVarDefinitionList().indexOf(definition)
//            return getByIndex((parent as VlangVarSpec).getRightExpressionsList(), index)
//        }
//        if (parent is VlangTypeSwitchGuard) {
//            return (parent as VlangTypeSwitchGuard).getExpression()
//        }
//        LOG.error(
//            "Cannot find value for variable definition: " + definition.getText(),
//            AttachmentFactory.createAttachment(definition.getContainingFile().getVirtualFile())
//        )
//        return null
//    }
//
//    fun getValue(definition: VlangConstDefinition): VlangExpression? {
//        val parent: PsiElement = definition.getParent()
//        assert(parent is VlangConstSpec)
//        val index: Int = (parent as VlangConstSpec).getConstDefinitionList().indexOf(definition)
//        return getByIndex((parent as VlangConstSpec).getExpressionList(), index)
//    }
//
//    private fun <T> getByIndex(list: List<T>, index: Int): T? {
//        return if (0 <= index && index < list.size) list[index] else null
//    }
//
//    fun getTypeSpecSafe(type: VlangType): VlangTypeSpec? {
//        val stub: VlangTypeStub = type.getStub()
//        val parent: PsiElement = if (stub == null) type.getParent() else stub.getParentStub().getPsi()
//        return ObjectUtils.tryCast(parent, VlangTypeSpec::class.java)
//    }
//
//    fun canBeAutoImported(file: VlangFile, allowMain: Boolean, module: Module?): Boolean {
//        return if (isBuiltinFile(file) || !allowMain && StringUtil.equals(
//                file.getPackageName(),
//                VlangConstants.MAIN
//            )
//        ) {
//            false
//        } else allowed(file, null, module) && !VlangUtil.isExcludedFile(file)
//    }
//
//    @Contract("null, _ -> null")
//    fun <T : PsiElement?> getNonStrictTopmostParentOfType(element: PsiElement?, aClass: Class<T>): T? {
//        val first = PsiTreeUtil.getNonStrictParentOfType(element, aClass)
//        val topMost = PsiTreeUtil.getTopmostParentOfType(first, aClass)
//        return ObjectUtils.chooseNotNull<T>(topMost, first)
//    }
//
//    fun getExpression(slice: VlangIndexOrSliceExpr): VlangExpression? {
//        return ContainerUtil.getFirstItem(getExpressionsBefore(slice.getExpressionList(), slice.getLbrack()))
//    }
//
//    fun getLeftExpressionsList(rangeClause: VlangRangeClause): List<VlangExpression> {
//        return getExpressionsBefore(rangeClause.getExpressionList(), rangeClause.getRange())
//    }
//
//    fun getLeftExpressionsList(recvStatement: VlangRecvStatement): List<VlangExpression> {
//        return getExpressionsBefore(
//            recvStatement.getExpressionList(),
//            ObjectUtils.chooseNotNull(recvStatement.getAssign(), recvStatement.getVarAssign())
//        )
//    }
//
//    fun getIndices(slice: VlangIndexOrSliceExpr): Trinity<VlangExpression, VlangExpression?, VlangExpression?> {
//        val start: VlangExpression
//        var end: VlangExpression? = null
//        var max: VlangExpression? = null
//        val colons: Array<ASTNode> = slice.getNode().getChildren(TokenSet.create(VlangTypes.COLON))
//        val exprList: List<VlangExpression> = slice.getExpressionList()
//        start = ContainerUtil.getFirstItem(
//            getExpressionsInRange(
//                exprList,
//                slice.getLbrack(),
//                if (colons.size > 0) colons[0].psi else null
//            )
//        )
//        if (colons.size == 1) {
//            end = ContainerUtil.getFirstItem(getExpressionsInRange(exprList, colons[0].psi, slice.getRbrack()))
//        }
//        if (colons.size == 2) {
//            end = ContainerUtil.getFirstItem(getExpressionsInRange(exprList, colons[0].psi, colons[1].psi))
//            max = ContainerUtil.getFirstItem(getExpressionsInRange(exprList, colons[1].psi, slice.getRbrack()))
//        }
//        return Trinity.create<VlangExpression, VlangExpression?, VlangExpression?>(start, end, max)
//    }
//
//    fun getRightExpressionsList(varSpec: VlangVarSpec): List<VlangExpression> {
//        return varSpec.getExpressionList()
//    }
//
//    fun getRightExpressionsList(rangeClause: VlangRangeClause): List<VlangExpression> {
//        return ContainerUtil.createMaybeSingletonList(rangeClause.getRangeExpression())
//    }
//
//    fun getRightExpressionsList(recvStatement: VlangRecvStatement): List<VlangExpression> {
//        return ContainerUtil.createMaybeSingletonList(recvStatement.getRecvExpression())
//    }
//
//    fun getRangeExpression(rangeClause: VlangRangeClause): VlangExpression? {
//        return getLastExpressionAfter(rangeClause.getExpressionList(), rangeClause.getRange())
//    }
//
//    fun getRecvExpression(recvStatement: VlangRecvStatement): VlangExpression? {
//        return getLastExpressionAfter(
//            recvStatement.getExpressionList(),
//            ObjectUtils.chooseNotNull(recvStatement.getAssign(), recvStatement.getVarAssign())
//        )
//    }
//
//    fun getSendExpression(sendStatement: VlangSendStatement): VlangExpression? {
//        return getLastExpressionAfter(sendStatement.getExpressionList(), sendStatement.getSendChannel())
//    }
//
//    private fun getLastExpressionAfter(list: List<VlangExpression>, anchor: PsiElement?): VlangExpression? {
//        if (anchor == null) return null
//        val last: VlangExpression = ContainerUtil.getLastItem(list)
//        return if (last != null && last.getTextRange().getStartOffset() >= anchor.textRange.endOffset) last else null
//    }
//
//    private fun getExpressionsInRange(
//        list: List<VlangExpression>,
//        start: PsiElement?,
//        end: PsiElement?
//    ): List<VlangExpression> {
//        return if (start == null && end == null) {
//            list
//        } else ContainerUtil.filter(list) { expression ->
//            (end == null || expression.getTextRange().getEndOffset() <= end.textRange.startOffset) &&
//                    (start == null || expression.getTextRange().getStartOffset() >= start.textRange.endOffset)
//        }
//    }
//
//    private fun getExpressionsBefore(list: List<VlangExpression>, anchor: PsiElement?): List<VlangExpression> {
//        return getExpressionsInRange(list, null, anchor)
//    }
//
//    fun getReadWriteAccess(referenceExpression: VlangReferenceExpression): ReadWriteAccessDetector.Access {
//        var expression: VlangExpression = getConsiderableExpression(referenceExpression)
//        var parent: PsiElement = expression.getParent()
//        if (parent is VlangSelectorExpr) {
//            if (expression.equals((parent as VlangSelectorExpr).getRight())) {
//                expression = getConsiderableExpression(parent as VlangSelectorExpr)
//                parent = expression.getParent()
//            } else {
//                return ReadWriteAccessDetector.Access.Read
//            }
//        }
//        if (parent is VlangIncDecStatement) {
//            return ReadWriteAccessDetector.Access.Write
//        }
//        if (parent is VlangLeftHandExprList) {
//            val grandParent = parent.parent
//            if (grandParent is VlangAssignmentStatement) {
//                return if ((grandParent as VlangAssignmentStatement).getAssignOp()
//                        .getAssign() == null
//                ) ReadWriteAccessDetector.Access.ReadWrite else ReadWriteAccessDetector.Access.Write
//            }
//            return if (grandParent is VlangSendStatement) {
//                ReadWriteAccessDetector.Access.Write
//            } else ReadWriteAccessDetector.Access.Read
//        }
//        if (parent is VlangSendStatement && parent.parent is VlangCommCase) {
//            return if (expression.equals((parent as VlangSendStatement).getSendExpression())) ReadWriteAccessDetector.Access.Read else ReadWriteAccessDetector.Access.ReadWrite
//        }
//        if (parent is VlangRangeClause) {
//            return if (expression.equals((parent as VlangRangeClause).getRangeExpression())) ReadWriteAccessDetector.Access.Read else ReadWriteAccessDetector.Access.Write
//        }
//        return if (parent is VlangRecvStatement) {
//            if (expression.equals((parent as VlangRecvStatement).getRecvExpression())) ReadWriteAccessDetector.Access.Read else ReadWriteAccessDetector.Access.Write
//        } else ReadWriteAccessDetector.Access.Read
//    }
//
//    private fun getConsiderableExpression(element: VlangExpression): VlangExpression {
//        var result: VlangExpression = element
//        while (true) {
//            val parent: PsiElement = result.getParent() ?: return result
//            if (parent is VlangParenthesesExpr) {
//                result = parent as VlangParenthesesExpr
//                continue
//            }
//            if (parent is VlangUnaryExpr) {
//                val unaryExpr: VlangUnaryExpr = parent as VlangUnaryExpr
//                if (unaryExpr.getMul() != null || unaryExpr.getBitAnd() != null || unaryExpr.getSendChannel() != null) {
//                    result = parent as VlangUnaryExpr
//                    continue
//                }
//            }
//            return result
//        }
//    }
//
//    fun getDecodedText(o: VlangStringLiteral): String {
//        val builder = StringBuilder()
//        val range = ElementManipulators.getManipulator<PsiElement>(o).getRangeInElement(o)
//        o.createLiteralTextEscaper().decode(range, builder)
//        return builder.toString()
//    }
//
//    fun getOperator(o: VlangUnaryExpr): PsiElement? {
//        return getNotNullElement(
//            o.getNot(),
//            o.getMinus(),
//            o.getPlus(),
//            o.getBitAnd(),
//            o.getBitXor(),
//            o.getMul(),
//            o.getSendChannel()
//        )
//    }
//
//    fun getOperator(o: VlangBinaryExpr): PsiElement? {
//        if (o is VlangAndExpr) return (o as VlangAndExpr).getCondAnd()
//        if (o is VlangOrExpr) return (o as VlangOrExpr).getCondOr()
//        if (o is VlangSelectorExpr) return (o as VlangSelectorExpr).getDot()
//        if (o is VlangConversionExpr) return (o as VlangConversionExpr).getComma()
//        if (o is VlangMulExpr) {
//            val m: VlangMulExpr = o as VlangMulExpr
//            return getNotNullElement(
//                m.getMul(), m.getQuotient(), m.getRemainder(), m.getShiftRight(), m.getShiftLeft(), m.getBitAnd(),
//                m.getBitClear()
//            )
//        }
//        if (o is VlangAddExpr) {
//            val a: VlangAddExpr = o as VlangAddExpr
//            return getNotNullElement(a.getBitXor(), a.getBitOr(), a.getMinus(), a.getPlus())
//        }
//        if (o is VlangConditionalExpr) {
//            val c: VlangConditionalExpr = o as VlangConditionalExpr
//            return getNotNullElement(
//                c.getEq(),
//                c.getNotEq(),
//                c.getGreater(),
//                c.getGreaterOrEqual(),
//                c.getLess(),
//                c.getLessOrEqual()
//            )
//        }
//        return null
//    }
//
//    private fun getNotNullElement(vararg elements: PsiElement): PsiElement? {
//        if (elements == null) return null
//        for (e in elements) {
//            if (e != null) return e
//        }
//        return null
//    }
//
//    fun isSingleCharLiteral(literal: VlangStringLiteral): Boolean {
//        return literal.getDecodedText().length() === 1
//    }
//
//    fun getRightExpression(assignment: VlangAssignmentStatement, leftExpression: VlangExpression): VlangExpression? {
//        val fieldIndex: Int = assignment.getLeftHandExprList().getExpressionList().indexOf(leftExpression)
//        return getByIndex(assignment.getExpressionList(), fieldIndex)
//    }
}