package org.vlang.lang.psi.impl

import com.intellij.lang.parser.GeneratedParserUtilBase
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.openapi.util.Condition
import com.intellij.openapi.util.Conditions
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.RecursionManager
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.*
import com.intellij.psi.scope.PsiScopeProcessor
import com.intellij.psi.util.*
import org.vlang.lang.psi.*
import org.vlang.sdk.VlangSdkUtil

object VlangPsiImplUtil {
    @JvmStatic
    fun getName(o: VlangFunctionDeclaration): String {
        return o.getIdentifier().text ?: ""
    }

    @JvmStatic
    fun getIdentifier(o: VlangMethodDeclaration): PsiElement? {
        return o.methodName.identifier
    }

    @JvmStatic
    fun getIdentifier(o: VlangInterfaceDeclaration): PsiElement? {
        return o.interfaceType.identifier
    }

    @JvmStatic
    fun getIdentifier(o: VlangEnumDeclaration): PsiElement? {
        return o.enumType.identifier
    }

    @JvmStatic
    fun getName(o: VlangStructDeclaration): String {
        return o.getIdentifier()?.text ?: ""
    }

    @JvmStatic
    fun getName(o: VlangUnionDeclaration): String {
        return o.getIdentifier()?.text ?: ""
    }

    @JvmStatic
    fun getName(o: VlangEnumDeclaration): String {
        return o.getIdentifier()?.text ?: ""
    }

    @JvmStatic
    fun getName(o: VlangInterfaceDeclaration): String {
        return o.interfaceType.identifier?.text ?: ""
    }

    @JvmStatic
    fun getName(o: VlangConstDefinition): String {
        return o.getIdentifier().text ?: ""
    }

    @JvmStatic
    fun getName(o: VlangTypeAliasDeclaration): String {
        return o.getIdentifier()?.text ?: ""
    }

    @JvmStatic
    fun getName(o: VlangImportSpec): String {
        return o.getIdentifier().text ?: ""
    }

    @JvmStatic
    fun getLastPart(o: VlangImportSpec): String {
        return o.name.split(".").last()
    }

    @JvmStatic
    fun getIdentifier(o: VlangStructDeclaration): PsiElement? {
        return o.structType.identifier
    }

    @JvmStatic
    fun getIdentifier(o: VlangType): PsiElement? {
        return o.typeReferenceExpressionList.lastOrNull()?.getIdentifier()
    }

    @JvmStatic
    fun getIdentifier(o: VlangFieldName): PsiElement? {
        return o.referenceExpression.getIdentifier()
    }

    @JvmStatic
    fun getQualifier(o: VlangFieldName): VlangReferenceExpressionBase? {
        return null
    }

    @JvmStatic
    fun getReference(o: VlangReferenceExpression): VlangReference {
        return VlangReference(o)
    }

    @JvmStatic
    fun getQualifier(o: VlangReferenceExpression): VlangReferenceExpression? {
        val parentDot = o.parentOfType<VlangDotExpression>() ?: return null
        val qualifier = parentDot.expression as? VlangReferenceExpression
        if (qualifier == o) {
            return null
        }

        return qualifier
    }

    @JvmStatic
    fun getQualifier(o: VlangTypeReferenceExpression): VlangTypeReferenceExpression? {
        return PsiTreeUtil.getChildOfType(o, VlangTypeReferenceExpression::class.java)
    }

    @JvmStatic
    fun getReceiverType(o: VlangMethodDeclaration): VlangType? {
        return o.receiver.type
    }

    fun getTypeReference(o: VlangType?): VlangTypeReferenceExpression? {
        if (o is VlangPointerType) {
            return PsiTreeUtil.findChildOfAnyType(o, VlangTypeReferenceExpression::class.java)
        }
        return o?.typeReferenceExpressionList?.firstOrNull()
    }

    @JvmStatic
    fun resolveType(type: VlangType): VlangType? {
        if (type !is VlangTypeImpl) {
            return type
        }

        val resolved = type.typeReferenceExpressionList.firstOrNull()?.resolve()
        val typeChild = resolved?.childrenOfType<VlangStructType>()?.firstOrNull()
        if (typeChild != null) {
            return typeChild
        }

        return type
    }

    @JvmStatic
    fun resolve(o: VlangTypeReferenceExpression): PsiElement? {
        return o.getReference().resolve()
    }

    @JvmStatic
    fun getQualifier(o: VlangFieldDefinition): VlangReferenceExpression? {
        return null
    }

    @JvmStatic
    fun resolve(o: VlangReferenceExpression): PsiElement? {
        return o.getReference().resolve()
    }

    @JvmStatic
    fun getReference(o: VlangTypeReferenceExpression): VlangReference {
        return VlangReference(o)
    }

    @JvmStatic
    fun getReference(o: VlangLabelRef): VlangLabelReference {
        return VlangLabelReference(o)
    }

    @JvmStatic
    fun getName(o: VlangLabelRef): String {
        return o.identifier.text
    }

    @JvmStatic
    fun getIdentifier(o: VlangImportSpec): PsiElement {
        return o.firstChild
    }

    @JvmStatic
    fun getIdentifier(o: VlangImportPath): PsiElement? {
        return o.referenceExpressionList.firstOrNull()
    }

    @JvmStatic
    fun getQualifier(o: VlangImportPath): VlangReferenceExpressionBase? {
        return null
    }

    @JvmStatic
    fun getReference(o: VlangImportSpec): VlangReference {
        return VlangReference(o.importPath)
    }

    @JvmStatic
    fun getName(o: VlangModuleClause): String {
        return o.identifier?.text ?: "<unknown>"
    }

    @JvmStatic
    fun getName(o: VlangParamDefinition): String {
        return o.getIdentifier().text ?: ""
    }

    @JvmStatic
    fun getName(o: VlangReceiver): String? {
        return o.getIdentifier()?.text
    }

    @JvmStatic
    fun getTypeInner(o: VlangReceiver, context: ResolveState?): VlangType {
        return o.type
    }

    @JvmStatic
    fun getTypeInner(o: VlangEnumDeclaration, context: ResolveState?): VlangType {
        return o.enumType
    }

    @JvmStatic
    fun getUnderlyingType(o: VlangType): PsiElement? {
        return null // TODO
    }

    fun getParentVlangValue(element: PsiElement): VlangValue? {
        var place: PsiElement? = element
        while (PsiTreeUtil.getParentOfType(place, VlangLiteralValueExpression::class.java).also { place = it } != null) {
            if (place?.parent is VlangValue) {
                return place?.parent as? VlangValue
            }
        }
        return null
    }

    fun getFqn(packageName: String?, elementName: String): String {
        return if (StringUtil.isNotEmpty(packageName)) "$packageName.$elementName" else elementName
    }

    @JvmStatic
    fun getType(o: VlangExpression, context: ResolveState?): VlangType? {
        return RecursionManager.doPreventingRecursion(o, true) {
            if (context != null) return@doPreventingRecursion unwrapParType(o, context)

            CachedValuesManager.getCachedValue(o) {
                CachedValueProvider.Result
                    .create(
                        unwrapParType(o, createContextOnElement(o)),
                        PsiModificationTracker.MODIFICATION_COUNT
                    )
            }
        }
    }

    val CONTEXT = Key.create<SmartPsiElementPointer<PsiElement>>("CONTEXT")

    fun createContextOnElement(element: PsiElement): ResolveState {
        return ResolveState.initial().put(
            CONTEXT,
            SmartPointerManager.getInstance(element.project).createSmartPsiElementPointer(element)
        )
    }

    private fun unwrapParType(o: VlangExpression, c: ResolveState?): VlangType? {
        val inner = getTypeInner(o, c)
        return /*if (inner is VlangParType) (inner as VlangParType).getActualType() else*/ inner
    }

    private fun getTypeInner(expr: VlangExpression, context: ResolveState?): VlangType? {
        if (expr is VlangUnaryExpr) {
            val e = expr.expression ?: return null
            val type = e.getType(context)
            val base = if (type == null/* || type.getTypeReferenceExpression() == null*/) type else type.underlyingType
//            if (o.bitAnd != null) return LightPointerType(type)
            if (expr.sendChannel != null) return if (base is VlangChannelType) base.type else type
            return if (expr.mul != null) if (base is VlangPointerType) base.type else type else type
        }
        if (expr is VlangAddExpr) {
            return expr.left.getType(context)
        }
        if (expr is VlangMulExpr) {
            val left = expr.left
            if (left !is VlangLiteral) return left.getType(context)
            val right = (expr as VlangBinaryExpr).right
            if (right != null) return right.getType(context)
        } else if (expr is VlangReferenceExpression) {
            val reference = expr.reference
            val resolve = reference.resolve()
            if (resolve is VlangTypeOwner)
                return typeOrParameterType(resolve, context)
        } else if (expr is VlangParenthesesExpr) {
            return expr.expression?.getType(context)
        } else if (expr is VlangStringLiteral) {
            return getBuiltinType("string", expr)
        } else if (expr is VlangLiteral) {
            if (expr.char != null) return getBuiltinType("rune", expr)
            if (expr.int != null || expr.hex != null || expr.oct != null)
                return getBuiltinType("i64", expr)
            if (expr.float != null) return getBuiltinType("f64", expr)
            if (expr.floati != null) return getBuiltinType("complex64", expr)
            if (expr.decimali != null) return getBuiltinType("complex128", expr)
        } else if (expr is VlangConditionalExpr) {
            return getBuiltinType("bool", expr)
        }

        if (expr is VlangLiteralValueExpression) {
            val type = expr.type.typeReferenceExpressionList.firstOrNull()?.resolve()
            if (type?.firstChild is VlangStructType) {
                return type.firstChild as VlangStructType
            }
            return expr.type
        }

        return null
    }

    private fun typeOrParameterType(resolve: VlangTypeOwner, context: ResolveState?): VlangType? {
        val type = resolve.getType(context)
//        if (resolve is VlangParamDefinition && (resolve as VlangParamDefinition).isVariadic()) {
//            return if (type == null) null else LightArrayType(type)
//        }
//        return if (resolve is VlangSignatureOwner) {
//            LightFunctionType(resolve as VlangSignatureOwner)
//        } else type
        return type
    }

    @JvmStatic
    fun getSymbolVisibility(o: VlangVarDefinition): VlangSymbolVisibility? {
        return null
    }

    @JvmStatic
    fun getName(o: VlangVarDefinition): String {
        return o.getIdentifier().text
    }

    @JvmStatic
    fun getReference(o: VlangVarDefinition): PsiReference? {
        val createRef = PsiTreeUtil.getParentOfType(
            o,
            VlangBlock::class.java,
            VlangForStatement::class.java,
            VlangIfStatement::class.java,
//            VlangSwitchStatement::class.java,
//            VlangSelectStatement::class.java
        ) is VlangBlock
        return if (createRef) VlangVarReference(o) else null
    }

    fun getBuiltinType(name: String, context: PsiElement): VlangType? {
        val builtin = VlangSdkUtil.findBuiltinDir(context)
        if (builtin != null) {
            print("")
        }

        val file = VlangElementFactory.createFileFromText(context.project, "fn f(a $name)")

        val type = file.findElementAt(9)?.parent?.parent as? VlangType

        return type
    }

    @JvmStatic
    fun getTypeInner(o: VlangVarDefinition, context: ResolveState?): VlangType? {
        // see http://Vlanglang.org/ref/spec#RangeClause
        val parent = PsiTreeUtil.getStubOrPsiParent(o)
//        if (parent is VlangRangeClause) {
//            return processRangeClause(o, parent as VlangRangeClause?, context)
//        }
        if (parent is VlangVarDeclaration) {
            return findTypeInVarSpec(o, context)
        }
        val literal = PsiTreeUtil.getNextSiblingOfType(o, VlangLiteral::class.java)
        if (literal != null) {
            return literal.getType(context)
        }
//        val siblingType: VlangType = o.findSiblingType()
//        if (siblingType != null) return siblingType
//        if (parent is VlangTypeSwitchGuard) {
//            val switchStatement: VlangTypeSwitchStatement = ObjectUtils.tryCast(parent!!.parent, VlangTypeSwitchStatement::class.java)
//            if (switchStatement != null) {
//                val typeCase: VlangTypeCaseClause = getTypeCaseClause(
//                    getContextElement(context),
//                    switchStatement
//                )
//                return if (typeCase != null) {
//                    if (typeCase.getDefault() != null) (parent as VlangTypeSwitchGuard?).getExpression()
//                        .getVlangType(context) else typeCase.getType()
//                } else (parent as VlangTypeSwitchGuard?).getExpression().getVlangType(null)
//            }
//        }
        return null
    }

    private fun findTypeInVarSpec(o: VlangVarDefinition, context: ResolveState?): VlangType? {
        val parent = PsiTreeUtil.getStubOrPsiParent(o) as? VlangVarDeclaration ?: return null
//        val commonType = (parent.varAssign as VlangTypeOwner).getType(context)
//        if (commonType != null) {
//            return commonType
//        }

        return parent.expressionList.first().getType(context)

//        val varList: List<VlangVarDefinition> = parent.varDefinitionList
//        var i = varList.indexOf(o)
//        i = if (i == -1) 0 else i
//        val exprs: List<VlangExpression> = parent.expressionList
//
//        if (exprs.size == 1 && exprs[0] is VlangCallExpr) {
//            val call: VlangExpression = exprs[0]
//            val fromCall: VlangType = call.getVlangType(context)
//            val canDecouple = varList.size > 1
//            val underlyingType: VlangType = if (canDecouple && fromCall is VlangSpecType) (fromCall as VlangSpecType).getType() else fromCall
//            val byRef: VlangType = typeFromRefOrType(underlyingType)
//            val type: VlangType =
//                funcType(if (canDecouple && byRef is VlangSpecType) (byRef as VlangSpecType).getType() else byRef)
//                    ?: return fromCall
//            if (type is VlangTypeList) {
//                if ((type as VlangTypeList).getTypeList().size() > i) {
//                    return (type as VlangTypeList).getTypeList().get(i)
//                }
//            }
//            return type
//        }
//        return if (exprs.size <= i) null else exprs[i].getVlangType(context)
    }

    fun processSignatureOwner(o: VlangSignatureOwner, processor: VlangScopeProcessorBase): Boolean {
        val signature = o.getSignature() ?: return true
        if (!processParameters(processor, signature.parameters)) {
            return false
        }

        val resultParameters = signature.result?.parameters
        return resultParameters == null || processParameters(processor, resultParameters)
    }

    private fun processParameters(processor: VlangScopeProcessorBase, parameters: VlangParameters): Boolean {
        for (declaration in parameters.parameterDeclarationList) {
            if (!processNamedElements(
                    processor,
                    ResolveState.initial(),
                    declaration.paramDefinitionList,
                    true
                )
            ) {
                return false
            }
        }
        return true
    }

    fun processNamedElements(
        processor: PsiScopeProcessor,
        state: ResolveState,
        elements: Collection<VlangNamedElement>,
        localResolve: Boolean,
    ): Boolean {
        return processNamedElements(processor, state, elements, Conditions.alwaysTrue(), localResolve, false)
    }

    fun processNamedElements(
        processor: PsiScopeProcessor,
        state: ResolveState,
        elements: Collection<VlangNamedElement>,
        localResolve: Boolean,
        checkContainingFile: Boolean,
    ): Boolean {
        return processNamedElements(processor, state, elements, Conditions.alwaysTrue(), localResolve, checkContainingFile)
    }

    fun processNamedElements(
        processor: PsiScopeProcessor,
        state: ResolveState,
        elements: Collection<VlangNamedElement>,
        condition: Condition<Any>,
        localResolve: Boolean,
        checkContainingFile: Boolean,
    ): Boolean {
        val contextFile = if (checkContainingFile) VlangReference.getContextFile(state) else null
        val module = if (contextFile != null) ModuleUtilCore.findModuleForPsiElement(contextFile) else null
        for (definition in elements) {
            if (!condition.value(definition)) continue
            if (!definition.isValid() || checkContainingFile
            /*&& !VlangPsiImplUtil.allowed(
                definition.getContainingFile(),
                contextFile,
                module
            )*/
            ) continue
            if ((localResolve || definition.isPublic()) && !processor.execute(definition, state))
                return false
        }
        return true
    }

    fun traverser(): SyntaxTraverser<PsiElement?> {
        return SyntaxTraverser.psiTraverser()
            .forceDisregardTypes(Conditions.equalTo(GeneratedParserUtilBase.DUMMY_BLOCK))
    }

}