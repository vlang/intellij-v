package org.vlang.lang.psi.impl

import com.intellij.lang.parser.GeneratedParserUtilBase
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.openapi.util.Condition
import com.intellij.openapi.util.Conditions
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.RecursionManager
import com.intellij.psi.*
import com.intellij.psi.impl.source.tree.LeafElement
import com.intellij.psi.scope.PsiScopeProcessor
import com.intellij.psi.util.*
import org.vlang.configurations.VlangConfiguration
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.lang.VlangTypes
import org.vlang.lang.completion.VlangCompletionUtil
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.imports.VlangImportReference
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import org.vlang.lang.psi.types.VlangNotNullableTypeEx
import org.vlang.lang.psi.types.VlangNullableTypeEx
import org.vlang.lang.psi.types.VlangPointerTypeEx

object VlangPsiImplUtil {
    @JvmStatic
    fun getName(o: VlangFunctionDeclaration): String {
        return o.getIdentifier().text ?: ""
    }

    @JvmStatic
    fun isDefinition(o: VlangFunctionDeclaration): Boolean {
        return o.getBlock() == null
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
    fun getIdentifier(o: VlangUnionDeclaration): PsiElement? {
        return o.unionType.identifier
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
    fun getIdentifier(o: VlangTypeAliasDeclaration): PsiElement? {
        return o.aliasType?.identifier
    }

    @JvmStatic
    fun getIdentifier(o: VlangAliasType): PsiElement? {
        return o.type.typeReferenceExpression?.getIdentifier()
    }

    @JvmStatic
    fun isAlias(o: VlangAliasType): Boolean {
        return o.typeUnionList?.typeList?.size == 1
    }

    @JvmStatic
    fun getName(o: VlangImportSpec): String {
        return o.importPath.qualifiedName
    }

    @JvmStatic
    fun getImportedName(o: VlangImportSpec): String {
        return o.importAlias?.name ?: o.importPath.lastPart
    }

    @JvmStatic
    fun getQualifiedName(o: VlangImportPath): String {
        return o.importNameList.joinToString(".") { it.text }
    }

    @JvmStatic
    fun getLastPart(o: VlangImportPath): String {
        return o.lastPartPsi.text
    }

    @JvmStatic
    fun getLastPartPsi(o: VlangImportPath): PsiElement {
        return o.importNameList.last()
    }

    @JvmStatic
    fun getLastPart(o: VlangImportSpec): String {
        return o.importPath.lastPart
    }

    @JvmStatic
    fun getLastPartPsi(o: VlangImportSpec): PsiElement {
        return o.importPath.lastPartPsi
    }

    @JvmStatic
    fun getIdentifier(o: VlangStructDeclaration): PsiElement? {
        return o.structType.identifier
    }

    @JvmStatic
    fun getIdentifier(o: VlangType): PsiElement? {
        return o.typeReferenceExpression?.getIdentifier()
    }

    @JvmStatic
    fun getIdentifier(o: VlangFieldName): PsiElement? {
        return o.referenceExpression.getIdentifier()
    }

    @JvmStatic
    fun getQualifier(o: VlangFieldName): VlangCompositeElement? {
        return null
    }

    @JvmStatic
    fun getReference(o: VlangReferenceExpression): VlangReference {
        return VlangReference(o)
    }

    @JvmStatic
    fun getReference(o: VlangImportName): VlangImportReference<VlangImportName> {
        return VlangImportReference(o, o.parentOfType()!!)
    }

    @JvmStatic
    fun resolve(o: VlangImportName): PsiElement? {
        return o.reference.resolve()
    }

    @JvmStatic
    fun getNameIdentifier(o: VlangImportName): PsiElement {
        return o.getIdentifier()
    }

    @JvmStatic
    fun getTextOffset(o: VlangImportName): Int {
        return getNameIdentifier(o).textOffset
    }

    @JvmStatic
    fun setName(o: VlangImportName, newName: String): PsiElement {
        val identifier = o.identifier
        identifier.replace(VlangElementFactory.createIdentifierFromText(o.project, newName))
        return o
    }

    @JvmStatic
    fun getName(o: VlangImportName): String? {
        val stub = o.stub
        if (stub != null) {
            return stub.name
        }
        return o.identifier.text
    }

    @JvmStatic
    fun getQualifier(o: VlangImportName): String {
        val parts = mutableListOf<String>()
        var sibling = o.prevSibling
        while (sibling != null) {
            if (sibling is VlangImportName) {
                parts.add(sibling.text)
            }
            sibling = sibling.prevSibling
        }

        return parts.reversed().joinToString(".")
    }

    @JvmStatic
    fun getFieldList(o: VlangStructType): List<VlangFieldDefinition> {
        return o.fieldsGroupList.flatMap { it.fieldDeclarationList }.flatMap { it.fieldDefinitionList }
    }

    @JvmStatic
    fun getFieldList(o: VlangUnionType): List<VlangFieldDefinition> {
        return o.fieldsGroupList.flatMap { it.fieldDeclarationList }.flatMap { it.fieldDefinitionList }
    }

    @JvmStatic
    fun getFieldList(o: VlangInterfaceType): List<VlangFieldDefinition> {
        return o.membersGroupList.flatMap { it.fieldDeclarationList }.flatMap { it.fieldDefinitionList }
    }

    @JvmStatic
    fun getMethodList(o: VlangInterfaceType): List<VlangInterfaceMethodDefinition> {
        return o.membersGroupList.flatMap { it.interfaceMethodDeclarationList }.map { it.interfaceMethodDefinition }
    }

    @JvmStatic
    fun getFieldList(o: VlangEnumType): List<VlangEnumFieldDefinition> {
        return o.enumFields?.enumFieldDeclarationList?.map { it.enumFieldDefinition } ?: emptyList()
    }

    @JvmStatic
    fun isPublic(o: VlangEnumFieldDefinition): Boolean {
        return o.parentOfType<VlangEnumDeclaration>()?.isPublic() ?: false
    }

    @JvmStatic
    fun isPublic(o: VlangInterfaceMethodDefinition): Boolean {
        return true
    }

    @JvmStatic
    fun isPublic(o: VlangFieldDefinition): Boolean {
        if (o.parentOfType<VlangInterfaceType>() != null) {
            return true
        }

        val group = o.parentOfType<VlangFieldsGroup>() ?: return false
        val modifiers = group.memberModifiers?.memberModifierList ?: return false
        return modifiers.any { it.text == "pub" }
    }

    @JvmStatic
    fun getQualifier(o: VlangReferenceExpression): VlangCompositeElement? {
        return PsiTreeUtil.getChildOfType(o, VlangExpression::class.java)
    }

    @JvmStatic
    fun getQualifier(o: VlangTypeReferenceExpression): VlangCompositeElement? {
        return PsiTreeUtil.getChildOfType(o, VlangTypeReferenceExpression::class.java)
    }

    @JvmStatic
    fun getReceiverType(o: VlangMethodDeclaration): VlangType {
        return o.receiver.type
    }

    fun getTypeReference(o: VlangType?): VlangTypeReferenceExpression? {
        if (o is VlangPointerType) {
            return PsiTreeUtil.findChildOfAnyType(o, VlangTypeReferenceExpression::class.java)
        }
        return o?.typeReferenceExpression
    }

    @JvmStatic
    fun resolveType(type: VlangType): VlangType {
        if (type.javaClass != VlangTypeImpl::class.java) {
            return type
        }

        val resolved = type.typeReferenceExpression?.resolve() ?: return type
        val structType = resolved.childrenOfType<VlangStructType>().firstOrNull()
        if (structType != null) {
            return structType
        }

        val interfaceType = resolved.childrenOfType<VlangInterfaceType>().firstOrNull()
        if (interfaceType != null) {
            return interfaceType
        }

        val enumType = resolved.childrenOfType<VlangEnumType>().firstOrNull()
        if (enumType != null) {
            return enumType
        }

        val unionType = resolved.childrenOfType<VlangUnionType>().firstOrNull()
        if (unionType != null) {
            return unionType
        }

        val aliasType = resolved.childrenOfType<VlangAliasType>().firstOrNull()
        if (aliasType is VlangAliasType) {
            return aliasType
        }

        return type
    }

    @JvmStatic
    fun resolve(o: VlangTypeReferenceExpression): PsiElement? {
        return o.reference.resolve()
    }

    @JvmStatic
    fun getQualifier(o: VlangFieldDefinition): VlangCompositeElement? {
        return null
    }

    @JvmStatic
    fun resolve(o: VlangReferenceExpression): PsiElement? {
        return o.reference.resolve()
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
    fun getParametersList(o: VlangParameters): List<VlangParamDefinition> {
        return o.parameterDeclarationList.flatMap { decl -> decl.paramDefinitionList }
    }

    @JvmStatic
    fun getParametersListWithTypes(o: VlangParameters): List<Pair<VlangParamDefinition?, VlangType>> {
        return o.parameterDeclarationList.flatMap { decl ->
            if (decl.paramDefinitionList.isEmpty()) {
                return@flatMap listOf(null to decl.type)
            }
            decl.paramDefinitionList.map { it to decl.type }
        }
    }

    @JvmStatic
    fun getIdentifier(o: VlangImportSpec): PsiElement {
        return o.firstChild
    }

    fun prevDot(e: PsiElement?): Boolean {
        val prev = if (e == null) null else PsiTreeUtil.prevVisibleLeaf(e)
        return prev is LeafElement && (prev as LeafElement).elementType === VlangTypes.DOT
    }

    @JvmStatic
    fun addImport(file: VlangFile, list: VlangImportList?, name: String, alias: String?): VlangImportSpec {
        val decl = VlangElementFactory.createImportDeclaration(file.project, name, alias)!!
        if (list == null) {
            var importList = VlangElementFactory.createImportList(file.project, name, alias)!!
            val modulePsi = file.getModule()

            importList = if (modulePsi == null) {
                file.addBefore(importList, file.firstChild) as VlangImportList
            } else {
                val listPsi = file.addAfter(importList, modulePsi) as VlangImportList
                file.addBefore(VlangElementFactory.createDoubleNewLine(file.project), listPsi)

                listPsi
            }

            return importList.importDeclarationList.first().importSpec!!
        }
        return addImportDeclaration(list, decl)
    }

    private fun addImportDeclaration(importList: VlangImportList, newImportDeclaration: VlangImportDeclaration): VlangImportSpec {
        val lastImport = importList.importDeclarationList.last()
        val importDeclaration = importList.addAfter(newImportDeclaration, lastImport) as VlangImportDeclaration
        val importListNextSibling = importList.nextSibling
        if (importListNextSibling !is PsiWhiteSpace) {
            importList.addAfter(VlangElementFactory.createNewLine(importList.project), importDeclaration)
            if (importListNextSibling != null) {
                // double new line if there is something valuable after import list
                importList.addAfter(VlangElementFactory.createNewLine(importList.project), importDeclaration)
            }
        }
        importList.addBefore(VlangElementFactory.createNewLine(importList.project), importDeclaration)
        return importDeclaration.importSpec!!
    }

    @JvmStatic
    fun getIdentifier(o: VlangImportAlias): PsiElement? {
        return o.importAliasName?.identifier
    }

    @JvmStatic
    fun getReference(o: VlangImportAliasName): VlangImportReference<VlangImportAliasName> {
        return VlangImportReference(o, o.parent.parent as VlangImportSpec)
    }

    @JvmStatic
    fun getName(o: VlangModuleClause): String {
        return o.identifier?.text ?: "<unknown>"
    }

    @JvmStatic
    fun getName(o: VlangImportAlias): String {
        return o.importAliasName?.identifier?.text ?: ""
    }

    @JvmStatic
    fun getName(o: VlangParamDefinition): String {
        return o.getIdentifier().text ?: ""
    }

    @JvmStatic
    fun getContents(o: VlangStringLiteral): String {
        return o.text.substring(1, o.text.length - 1)
    }

    @JvmStatic
    fun isVariadic(o: VlangParamDefinition): Boolean {
        return (o.parent as? VlangParameterDeclaration)?.isVariadic ?: false
    }

    @JvmStatic
    fun isVariadic(o: VlangParameterDeclaration): Boolean {
        return o.tripleDot != null
    }

    @JvmStatic
    fun getParameters(o: VlangCallExpr): List<VlangExpression> {
        return o.argumentList.elementList.mapNotNull { it?.value?.expression }
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

    fun getFqn(moduleName: String?, elementName: String): String {
        return if (moduleName.isNullOrEmpty()) elementName else "$moduleName.$elementName"
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
        when (expr) {
            is VlangConditionalExpr, is VlangInExpression, is VlangNotInExpression, is VlangAndExpr, is VlangOrExpr -> {
                return getBuiltinType("bool", expr)
            }

            is VlangUnaryExpr                                                                                       -> {
                if (expr.not != null) {
                    return getBuiltinType("bool", expr)
                }
                if (expr.bitAnd != null) {
                    if (expr.expression == null) return null
                    val inner = getType(expr.expression!!, context)?.text ?: return null
                    return getBuiltinType("&$inner", expr)
                }

                if (expr.mul != null) {
                    if (expr.expression == null) return null
                    val inner = getType(expr.expression!!, context)
                    val innerEx = inner.toEx()
                    if (innerEx is VlangPointerTypeEx) {
                        return innerEx.inner?.raw()
                    }

                    return inner
                }

                if (expr.expression == null) return null
                return getType(expr.expression!!, context)
            }
        }

        if (expr is VlangOrBlockExpr) {
            if (expr.expression == null) return null

            val type = getType(expr.expression!!, context) ?: return null
            val exType = type.toEx()

            if (exType is VlangNullableTypeEx) {
                val inner = exType.inner ?: return getBuiltinType("void", expr)
                return inner.raw()
            }
            if (exType is VlangNotNullableTypeEx) {
                val inner = exType.inner ?: return getBuiltinType("void", expr)
                return inner.raw()
            }

//            val isEndCall = lastExpression is VlangCallExpr && (VlangCodeInsightUtil.isExitCall(lastExpression) || VlangCodeInsightUtil.isPanicCall(lastExpression))
//            if (lastStatement is VlangReturnStatement || isEndCall) {
//                if (exType is VlangNullableTypeEx) {
//                    val inner = exType.inner ?: return getBuiltinType("void", expr)
//                    return inner.raw()
//                }
//                if (exType is VlangNotNullableTypeEx) {
//                    val inner = exType.inner ?: return getBuiltinType("void", expr)
//                    return inner.raw()
//                }
//                return null
//            }
//
            return null
        }

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
            if (VlangCompletionUtil.isCompileTimeIdentifier(expr.getIdentifier())) {
                return getBuiltinType("string", expr)
            }

            // expr or { err }
            if (expr.getIdentifier().text == "err" && expr.parentOfType<VlangOrBlockExpr>() != null) {
                return getBuiltinType("IError", expr)
            }

            val reference = expr.reference
            val resolve = reference.resolve()
            if (resolve is VlangTypeOwner)
                return typeOrParameterType(resolve, context)
        } else if (expr is VlangParenthesesExpr) {
            return expr.expression?.getType(context)
        } else if (expr is VlangStringLiteral) {
            return getBuiltinType("string", expr)
        }

        if (expr is VlangLiteral) {
            if (expr.char != null) return getBuiltinType("rune", expr)
            if (expr.int != null || expr.hex != null || expr.oct != null)
                return getBuiltinType("int", expr)
            if (expr.float != null) return getBuiltinType("f64", expr)
            if (expr.floati != null) return getBuiltinType("complex64", expr)
            if (expr.decimali != null) return getBuiltinType("complex128", expr)
        }

        if (expr is VlangIndexOrSliceExpr) {
            val indexExpr = expr.expressionList.firstOrNull() ?: return null
            val lbrack = expr.lbrack ?: return null
            val isRange = PsiTreeUtil.findSiblingForward(lbrack, VlangTypes.RANGE, false, null) != null ||
                    PsiTreeUtil.findSiblingForward(lbrack, VlangTypes.RANGE_EXPR, false, null) != null
            if (isRange) {
                return indexExpr.getType(null)
            }

            val inner = indexExpr.getType(null)
            if (inner is VlangArrayOrSliceType) {
                return inner.type
            }
            if (inner is VlangMapType) {
                return inner.valueType
            }
            if (inner?.text == "string") {
                return getBuiltinType("rune", expr)
            }
        }

        if (expr is VlangLiteralValueExpression) {
            val type = expr.type.typeReferenceExpression?.resolve()
            if (type?.firstChild is VlangStructType) {
                return type.firstChild as VlangStructType
            }
            return expr.type
        }

        if (expr is VlangCallExpr) {
            val callRef = expr.expression as? VlangReferenceExpression
            if (VlangCodeInsightUtil.isTypeCast(expr)) {
                val callName = callRef?.getIdentifier()?.text
                if (callName != null) {
                    val builtinType = getBuiltinType(callName, expr)?.resolveType()
                    if (builtinType is VlangAliasType) {
                        // TODO:
                        return builtinType.typeUnionList?.typeList?.firstOrNull()
                    }
                    return builtinType
                }
            }

            val resolved = callRef?.reference?.resolve()
            if (resolved !is VlangSignatureOwner) {
                if (callRef is VlangReferenceExpression) {
                    return getTypeInner(callRef, null)
                }
                return null
            }

            val result = resolved.getSignature()?.result ?: return getBuiltinType("void", expr)
            return result.type
        }

        if (expr is VlangDotExpression) {
            if (expr.errorPropagationExpression != null || expr.forceNoErrorPropagationExpression != null) {
                val type = expr.expression.getType(context)
                val exType = type.toEx()

                if (exType is VlangNullableTypeEx) {
                    val inner = exType.inner ?: return getBuiltinType("void", expr)
                    return inner.raw()
                }
                if (exType is VlangNotNullableTypeEx) {
                    val inner = exType.inner ?: return getBuiltinType("void", expr)
                    return inner.raw()
                }
                return type
            }
        }

        if (expr is VlangUnsafeExpression) {
            val block = expr.block
            val lastStatement = block?.statementList?.lastOrNull() ?: return null
            val lastExpressionList = lastStatement.childrenOfType<VlangLeftHandExprList>().lastOrNull()
            val lastExpression = lastExpressionList?.expressionList?.lastOrNull()
            return lastExpression?.getType(context)
        }

        if (expr is VlangArrayCreation) {
            val firstItem = expr.arrayCreationList?.expressionList?.firstOrNull()
            val type = firstItem?.getType(context)?.text ?: "any"
            return getBuiltinType("[]$type", expr)
        }

        if (expr is VlangIfExpression) {
            val ifBody = expr.block
            val elseBody = expr.elseStatement?.block

            val lastIfStatement = ifBody?.statementList?.lastOrNull()
            val lastElseStatement = elseBody?.statementList?.lastOrNull()

            val lastIfExpressionList = lastIfStatement?.childrenOfType<VlangLeftHandExprList>()?.lastOrNull()
            val lastElseExpressionList = lastElseStatement?.childrenOfType<VlangLeftHandExprList>()?.lastOrNull()

            val lastIfExpression = lastIfExpressionList?.expressionList?.lastOrNull()
            val lastElseExpression = lastElseExpressionList?.expressionList?.lastOrNull()

            val ifType = lastIfExpression?.getType(context)
            val elseType = lastElseExpression?.getType(context)

            if (ifType == null) return elseType
            if (elseType == null) return ifType
            if (ifType.text == elseType.text) return ifType

            // TODO: union type of if and else types
            return getBuiltinType("any", expr)
        }

        if (expr is VlangLiteral) {
            return when {
                expr.`true` != null   -> getBuiltinType("bool", expr)
                expr.`false` != null  -> getBuiltinType("bool", expr)
                expr.int != null      -> getBuiltinType("int", expr)
                expr.bin != null      -> getBuiltinType("int", expr)
                expr.hex != null      -> getBuiltinType("int", expr)
                expr.oct != null      -> getBuiltinType("int", expr)
                expr.float != null    -> getBuiltinType("f64", expr)
                expr.floati != null   -> getBuiltinType("complex64", expr)
                expr.decimali != null -> getBuiltinType("complex128", expr)
                expr.char != null     -> getBuiltinType("rune", expr)
                expr.nil != null      -> getBuiltinType("nil", expr)
                else                  -> null
            }
        }

        return null
    }

    private fun typeOrParameterType(resolve: VlangTypeOwner, context: ResolveState?): VlangType? {
        val type = resolve.getType(context)
        if (resolve is VlangParamDefinition && resolve.isVariadic) {
            return if (type == null) null else getBuiltinType("[]${type.text}", resolve)
        }
//        return if (resolve is VlangSignatureOwner) {
//            LightFunctionType(resolve as VlangSignatureOwner)
//        } else type
        return type
    }

    @JvmStatic
    fun isNumeric(o: VlangLiteral): Boolean {
        return o.int != null || o.bin != null || o.hex != null || o.oct != null || o.float != null || o.floati != null || o.decimali != null
    }

    @JvmStatic
    fun getSymbolVisibility(o: VlangVarDefinition): VlangSymbolVisibility? {
        return null
    }

    @JvmStatic
    fun getSymbolVisibility(o: VlangConstDefinition): VlangSymbolVisibility? {
        return (o.parent as? VlangConstDeclaration)?.symbolVisibility
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
        val builtin = VlangConfiguration.getInstance(context.project).builtinLocation
        if (builtin != null) {
            print("")
        }

        val file = VlangElementFactory.createFileFromText(context.project, "fn f(a $name)")

        val element = file.findElementAt(8)
        return element?.findTopmostParentOfType()
    }

    @JvmStatic
    fun getTypeInner(o: VlangSignatureOwner, context: ResolveState?): VlangType? {
        val signature = o.getSignature() ?: return null
        val parameters = signature.parameters.parametersListWithTypes
        val result = signature.result

        val text = buildString {
            append("fn ")
            append(
                parameters.joinToString(",", "(", ")") { (def, type) ->
                    buildString {
                        if (def != null) {
                            append(def.text)
                            append(" ")
                        }
                        append(type.text)
                    }
                }
            )
            val resultType = result?.type?.text
            if (resultType != null) {
                append("")
                append(resultType)
            }
        }

        return getBuiltinType(text, o)
    }

    @JvmStatic
    fun getTypeInner(o: VlangVarDefinition, context: ResolveState?): VlangType? {
        val parent = PsiTreeUtil.getStubOrPsiParent(o)
//        if (parent is VlangRangeClause) {
//            return processRangeClause(o, parent as VlangRangeClause?, context)
//        }
        if (parent is VlangVarDeclaration) {
            return getTypeInVarSpec(o, parent, context)
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

    private fun getTypeInVarSpec(o: VlangVarDefinition, decl: VlangVarDeclaration, context: ResolveState?): VlangType? {
        if (decl is VlangRangeClause) {
            val rightType = decl.expression?.getType(context)
            val varList = decl.varDefinitionList
            if (varList.size == 1) {
                if (rightType is VlangArrayOrSliceType) {
                    return rightType.type
                }

                return getBuiltinType("any", o)
            }

            val defineIndex = varList.indexOf(o)
            if (defineIndex == 0) {
                return getBuiltinType("int", o)
            } else if (defineIndex == 1) {
                if (rightType is VlangArrayOrSliceType) {
                    return rightType.type
                }

                return getBuiltinType("any", o)
            }

            return getBuiltinType("any", o)
        }


        val defineIndex = decl.varDefinitionList.indexOf(o)
        val varList = decl.varDefinitionList
        val exprList = decl.expressionList

        // if a := call()
        if (varList.size == 1 && exprList.size == 1) {
            return exprList[0].getType(context)
        }

        // if a, b := call()
        if (exprList.size == 1) {
            val expr = exprList.first()
            val type = expr.getType(context)
            if (type is VlangTupleType) {
                return type.typeListNoPin.typeList.getOrNull(defineIndex)
            }
        }

        // if a, b := 10, 20
        val neededExpr = exprList.getOrNull(defineIndex)
        if (neededExpr != null) {
            return neededExpr.getType(context)
        }

        return null
    }

    fun processSignatureOwner(o: VlangSignatureOwner, processor: VlangScopeProcessorBase): Boolean {
        val signature = o.getSignature() ?: return true
        if (!processParameters(processor, signature.parameters)) {
            return false
        }

        return true
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
            if (!processor.execute(definition, state))
                return false
        }
        return true
    }

    fun traverser(): SyntaxTraverser<PsiElement?> {
        return SyntaxTraverser.psiTraverser()
            .forceDisregardTypes(Conditions.equalTo(GeneratedParserUtilBase.DUMMY_BLOCK))
    }

    fun canBeAutoImported(file: VlangFile, allowMain: Boolean, module: Module?): Boolean {
        return file.getModuleName() != "main"
//        return if (VlangPsiImplUtil.isBuiltinFile(file) || !allowMain && file.packageName == "main") {
//            false
//        } else VlangPsiImplUtil.allowed(file, null, module) && !VlangUtil.isExcludedFile(file)
    }
}