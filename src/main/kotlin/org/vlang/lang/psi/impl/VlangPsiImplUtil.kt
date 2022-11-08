package org.vlang.lang.psi.impl

import com.intellij.codeInsight.highlighting.ReadWriteAccessDetector.Access
import com.intellij.lang.parser.GeneratedParserUtilBase
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Condition
import com.intellij.openapi.util.Conditions
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.RecursionManager
import com.intellij.psi.*
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet
import com.intellij.psi.impl.source.tree.LeafElement
import com.intellij.psi.scope.PsiScopeProcessor
import com.intellij.psi.util.*
import org.vlang.configurations.VlangConfiguration
import org.vlang.ide.codeInsight.VlangAttributesUtil
import org.vlang.ide.codeInsight.VlangBuiltinTypesUtil
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.ide.codeInsight.VlangTypeInferenceUtil
import org.vlang.lang.VlangTypes
import org.vlang.lang.completion.VlangCompletionUtil
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangReferenceBase.Companion.LOCAL_RESOLVE
import org.vlang.lang.psi.impl.imports.VlangImportReference
import org.vlang.lang.psi.types.*
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import org.vlang.lang.sql.VlangSqlUtil
import org.vlang.utils.parentNth

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
    fun getTypeInner(o: VlangInterfaceDeclaration, context: ResolveState?): VlangType {
        return o.interfaceType
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
    fun isUnion(o: VlangStructDeclaration): Boolean {
        return o.structType.isUnion
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
    fun isPublic(o: VlangConstDefinition): Boolean {
        val decl = o.parent as VlangConstDeclaration
        val visibility = VlangPsiTreeUtil.getChildOfType(decl, VlangSymbolVisibility::class.java)
        return visibility?.pub != null
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
    fun getTypeInner(o: VlangTypeAliasDeclaration, context: ResolveState?): VlangType? {
        return o.aliasType
    }

    @JvmStatic
    fun getIdentifier(o: VlangTypeAliasDeclaration): PsiElement? {
        return o.aliasType?.identifier
    }

    @JvmStatic
    fun isAlias(o: VlangAliasType): Boolean {
        return o.typeUnionList?.typeList?.size == 1
    }

    @JvmStatic
    fun getAliasType(o: VlangAliasType): VlangType? {
        return o.typeUnionList?.typeList?.firstOrNull()
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
    fun resolve(o: VlangFieldName): PsiElement? {
        return o.referenceExpression.resolve()
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
    fun addCapture(o: VlangFunctionLit, name: String): PsiElement {
        val captureList = o.captureList
        if (captureList == null) {
            val newCaptureList = VlangElementFactory.createCaptureList(o.project, name)
            o.addAfter(newCaptureList, o.firstChild)
            o.addAfter(VlangElementFactory.createSpace(o.project), o.firstChild)
            return newCaptureList
        }
        return captureList.addCapture(name)
    }

    @JvmStatic
    fun addCapture(o: VlangCaptureList, name: String): PsiElement {
        val captureList = VlangElementFactory.createCaptureList(o.project, name)
        val capture = captureList.captureList.first()
        o.rbrack?.delete()

        val lastChild = o.lastChild
        if (lastChild.elementType == VlangTypes.COMMA) {
            o.add(VlangElementFactory.createSpace(o.project))
            o.add(capture)
            o.add(VlangElementFactory.createRBrack(o.project))
            return o
        }

        if (lastChild.elementType == VlangTypes.CAPTURE) {
            o.add(VlangElementFactory.createComma(o.project))
            o.add(VlangElementFactory.createSpace(o.project))
            o.add(capture)
            o.add(VlangElementFactory.createRBrack(o.project))
            return o
        }

        o.add(capture)
        o.add(VlangElementFactory.createRBrack(o.project))
        return o
    }

    @JvmStatic
    fun getFieldList(o: VlangStructType): List<VlangFieldDefinition> {
        return o.fieldsGroupList.flatMap { it.fieldDeclarationList }.mapNotNull { it.fieldDefinition }
    }

    @JvmStatic
    fun getEmbeddedStructList(o: VlangStructType): List<VlangEmbeddedDefinition> {
        return o.fieldsGroupList.flatMap { it.fieldDeclarationList }.mapNotNull { it.embeddedDefinition }
    }

    @JvmStatic
    fun isUnion(o: VlangStructType): Boolean {
        return o.union != null
    }

    @JvmStatic
    fun getType(o: VlangEmbeddedDefinition, context: ResolveState?): VlangType {
        return o.type
    }

    @JvmStatic
    fun getFieldList(o: VlangInterfaceType): List<VlangFieldDefinition> {
        return o.membersGroupList.flatMap { it.fieldDeclarationList }.mapNotNull { it.fieldDefinition }
    }

    @JvmStatic
    fun getMethodList(o: VlangInterfaceType): List<VlangInterfaceMethodDefinition> {
        return o.membersGroupList.flatMap { it.interfaceMethodDeclarationList }.map { it.interfaceMethodDefinition }
    }

    @JvmStatic
    fun getFieldList(o: VlangEnumType): List<VlangEnumFieldDefinition> {
        return o.enumFieldDeclarationList.map { it.enumFieldDefinition }
    }

    @JvmStatic
    fun isPublic(o: VlangEnumFieldDefinition): Boolean {
        return o.parentOfType<VlangEnumDeclaration>()?.isPublic() ?: false
    }

    @JvmStatic
    fun getTypeInner(o: VlangEnumFieldDefinition, context: ResolveState?): VlangType {
        return o.parentOfType<VlangEnumType>()!!
    }

    @JvmStatic
    fun isPublic(o: VlangInterfaceMethodDefinition): Boolean {
        return true
    }

    @JvmStatic
    fun isMutable(o: VlangFieldDefinition): Boolean {
        val group = o.parentOfType<VlangFieldsGroup>()
        val modifiers = group?.memberModifiers?.memberModifierList
        val withMutModifier = modifiers?.any { it.text == "mut" } ?: false
        if (withMutModifier) {
            return true
        }

        val parentStruct = o.parentOfType<VlangStructDeclaration>() ?: return false
        return VlangAttributesUtil.isMinifiedStruct(parentStruct)
    }

    @JvmStatic
    fun makeMutable(o: VlangFieldDefinition) {
        // TODO: implement
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
    fun getOwner(o: VlangFieldDefinition): VlangNamedElement {
        return o.parentOfType()
            ?: error("Can't find owner for field ${o.name}, field definition must be inside a struct, union or interface")
    }

    @JvmStatic
    fun getOwner(o: VlangInterfaceMethodDefinition): VlangNamedElement {
        return o.parentOfType() ?: error("Can't find owner for method ${o.name}, interface method definition must be inside interface")
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
    fun getReceiverType(o: VlangMethodDeclaration): VlangType? {
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
    fun getQualifiedName(o: VlangFieldDefinition): String? {
        val owner = o.parentOfType<VlangStructDeclaration>() ?: o.parentOfType<VlangInterfaceDeclaration>() ?: return o.getQualifiedName()
        return owner.getQualifiedName() + "." + o.name
    }

    @JvmStatic
    fun resolve(o: VlangReferenceExpression): PsiElement? {
        return o.reference.resolve()
    }

    @JvmStatic
    fun getReadWriteAccess(o: VlangReferenceExpression): Access {
        val expression = getConsiderableExpression(o)
        val parent = expression.parent

        if (parent is VlangLeftHandExprList) {
            val grandParent = parent.getParent()
            if (grandParent is VlangAssignmentStatement) {
                return if (grandParent.assignOp.assign == null) Access.ReadWrite else Access.Write
            }
            if (grandParent is VlangSendStatement) {
                return Access.Write
            }

            return Access.Read
        }

        if (parent is VlangRangeClause) {
            return if (expression == parent.expression) Access.Read else Access.Write
        }

        return Access.Read
    }

    private fun getConsiderableExpression(element: VlangExpression): VlangExpression {
        var result = element
        while (true) {
            val parent = result.parent ?: return result
            if (parent is VlangParenthesesExpr) {
                result = parent
                continue
            }
            if (parent is VlangUnaryExpr) {
                if (parent.mul != null || parent.bitAnd != null || parent.sendChannel != null) {
                    result = parent
                    continue
                }
            }
            return result
        }
    }

    @JvmStatic
    fun getReference(o: VlangTypeReferenceExpression): VlangReference {
        return VlangReference(o, forTypes = true)
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
    fun getName(o: VlangParamDefinition): String? {
        return o.getIdentifier()?.text
    }

    @JvmStatic
    fun isPublic(o: VlangParamDefinition): Boolean = true

    class VlangLiteralFileReferenceSet(
        str: String,
        element: VlangStringLiteral,
        startOffset: Int,
        isCaseSensitive: Boolean,
    ) : FileReferenceSet(str, element, startOffset, null, isCaseSensitive)

    @JvmStatic
    fun getReferences(o: VlangStringLiteral): Array<out PsiReference> {
        if (o.textLength < 2) return PsiReference.EMPTY_ARRAY

        val fs = o.containingFile.originalFile.virtualFile.fileSystem
        val literalValue = o.contents
        return VlangLiteralFileReferenceSet(literalValue, o, 1, fs.isCaseSensitive).allReferences
    }

    @JvmStatic
    fun isValidHost(o: VlangStringLiteral): Boolean {
        return true
    }

    @JvmStatic
    fun updateText(o: VlangStringLiteral, text: String): VlangStringLiteral {
        if (text.length <= 2) {
            return o
        }
        val newLiteral = VlangElementFactory.createStringLiteral(o.project, text)
        o.replace(newLiteral)
        return newLiteral
    }

    @JvmStatic
    fun createLiteralTextEscaper(o: VlangStringLiteral): StringLiteralEscaper<VlangStringLiteral> {
        return StringLiteralEscaper(o)
    }

    @JvmStatic
    fun getContents(o: VlangStringLiteral): String {
        return o.text.substring(1, o.text.length - 1)
    }

    @JvmStatic
    fun isVariadic(o: VlangParamDefinition): Boolean {
        return o.tripleDot != null
    }

    @JvmStatic
    fun getParameters(o: VlangCallExpr): List<VlangExpression> {
        return o.argumentList.elementList.mapNotNull { it?.value?.expression }
    }

    @JvmStatic
    fun resolve(o: VlangCallExpr): PsiElement? {
        return (o.expression as? VlangReferenceExpression)?.resolve()
    }

    @JvmStatic
    fun paramIndexOf(o: VlangCallExpr, pos: PsiElement): Int {
        val element = pos.parentOfType<VlangElement>()
        val args = o.argumentList.elementList
        return args.indexOf(element)
    }

    @JvmStatic
    fun isGuard(o: VlangIfExpression): Boolean {
        return o.varDeclaration != null
    }

    @JvmStatic
    fun getType(o: VlangSqlExpression, context: ResolveState?): VlangType? {
        val lastStatement = o.sqlBlock?.sqlBlockStatementList?.lastOrNull() ?: return null
        if (lastStatement is VlangSqlSelectStatement) {
            if (lastStatement.sqlSelectCountClause != null) {
                return getBuiltinType("int", lastStatement)
            }

            val tableRef = VlangSqlUtil.getTable(lastStatement) ?: return null
            val table = tableRef.typeReferenceExpression.resolve() as? VlangNamedElement ?: return null
            val type = table.getType(context) ?: return null
            return VlangLightType.LightArrayType(type)
        }
        return null
    }

    @JvmStatic
    fun getName(o: VlangReceiver): String? {
        return o.getIdentifier()?.text
    }

    @JvmStatic
    fun getTypeInner(o: VlangReceiver, context: ResolveState?): VlangType? {
        return o.type
    }

    @JvmStatic
    fun getTypeInner(o: VlangFieldDefinition, context: ResolveState?): VlangType? {
        return (o.parent as VlangFieldDeclaration).type
    }

    @JvmStatic
    fun getTypeInner(o: VlangConstDefinition, context: ResolveState?): VlangType? {
        val expr = o.expression ?: return null
        return getTypeInner(expr, context)
    }

    @JvmStatic
    fun getTypeInner(o: VlangStructDeclaration, context: ResolveState?): VlangType {
        return o.structType
    }

    @JvmStatic
    fun getTypeInner(o: VlangEnumDeclaration, context: ResolveState?): VlangType {
        return o.enumType
    }

    @JvmStatic
    fun getUnderlyingType(o: VlangType): VlangType? {
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
        // TODO: Paren type?
        return getTypeInner(o, c)
    }

    private fun getTypeInner(expr: VlangExpression, context: ResolveState?): VlangType? {
        val builtinTypes = VlangBuiltinTypesUtil.getInstance(expr.project)
        when (expr) {
            is VlangConditionalExpr, is VlangInExpression, is VlangNotInExpression, is VlangAndExpr, is VlangOrExpr -> {
                return builtinTypes.bool
            }

            is VlangUnaryExpr                                                                                       -> {
                if (expr.not != null) {
                    return builtinTypes.bool
                }
                if (expr.bitAnd != null) {
                    if (expr.expression == null) return null
                    val inner = getType(expr.expression!!, context) ?: return null
                    return VlangLightType.LightPointerType(inner)
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

        if (expr is VlangSelectExpression) {
            return builtinTypes.bool
        }

        if (expr is VlangDumpCallExpr) {
            return expr.expression?.getType(null)
        }

        if (expr is VlangOrBlockExpr) {
            if (expr.expression == null) return null

//            val isEndCall = lastExpression is VlangCallExpr && (VlangCodeInsightUtil.isExitCall(lastExpression) || VlangCodeInsightUtil.isPanicCall(lastExpression))
//            if (lastStatement is VlangReturnStatement || isEndCall) {
//                if (exType is VlangNullableTypeEx) {
//                    val inner = exType.inner ?: return builtinTypes.void
//                    return inner.raw()
//                }
//                if (exType is VlangNotNullableTypeEx) {
//                    val inner = exType.inner ?: return builtinTypes.void
//                    return inner.raw()
//                }
//                return null
//            }
//
            return unwrapNullableType(getType(expr.expression!!, context))
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
                return builtinTypes.string
            }

            // expr or { err }
            // if a := foo() { ... } else { err }
            if (expr.getIdentifier().text == "err" &&
                (VlangCodeInsightUtil.insideOrGuard(expr) || VlangCodeInsightUtil.insideElseBlockIfGuard(expr))
            ) {
                return builtinTypes.iError
            }

            val reference = expr.reference
            val resolve = reference.resolve()
            if (resolve is VlangTypeOwner)
                return typeOrParameterType(resolve, context)
        } else if (expr is VlangParenthesesExpr) {
            return expr.expression?.getType(context)
        } else if (expr is VlangStringLiteral) {
            return builtinTypes.string
        }

        if (expr is VlangLiteral) {
            if (expr.char != null) return builtinTypes.rune
            if (expr.int != null || expr.hex != null || expr.oct != null)
                return builtinTypes.int
            if (expr.float != null) return builtinTypes.f64
        }

        if (expr is VlangIndexOrSliceExpr) {
            val indexExpr = expr.expressionList.firstOrNull() ?: return null
            val lbrack = expr.lbrack ?: return null
            val isRange = expr.emptySlice != null ||
                    PsiTreeUtil.findSiblingForward(lbrack, VlangTypes.RANGE, false, null) != null ||
                    PsiTreeUtil.findSiblingForward(lbrack, VlangTypes.RANGE_EXPR, false, null) != null
            if (isRange) {
                return indexExpr.getType(null)
            }

            val inner = indexExpr.getType(null)
            if (inner is VlangArrayType) {
                return inner.type
            }
            if (inner is VlangMapType) {
                return inner.valueType
            }
            if (inner?.text == "string") {
                return builtinTypes.rune
            }
        }

        if (expr is VlangLiteralValueExpression) {
            val type = expr.type.typeReferenceExpression?.resolve()
            if (type?.firstChild is VlangStructType) {
                return type.firstChild as VlangStructType
            }
            return expr.type
        }

        // json.decode() call
        if (expr is VlangJsonCallExpr) {
            return expr.jsonArgumentList.type
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

            val signature = resolved.getSignature()

            val type = processArrayMethodCall(resolved, signature, expr)
            if (type != null) {
                return type
            }

            val result = signature?.result ?: return builtinTypes.void
            return result.type
        }

        if (expr is VlangDotExpression) {
            if (expr.errorPropagationExpression != null || expr.forceNoErrorPropagationExpression != null) {
                val type = expr.expression?.getType(context)
                val exType = type.toEx()

                if (exType is VlangNullableTypeEx) {
                    val inner = exType.inner ?: return builtinTypes.void
                    return inner.raw()
                }
                if (exType is VlangNotNullableTypeEx) {
                    val inner = exType.inner ?: return builtinTypes.void
                    return inner.raw()
                }
                return type
            }
        }

        if (expr is VlangFunctionLit) {
            return VlangLightType.LightFunctionType(expr)
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
            val type = firstItem?.getType(context) ?: return null

            return VlangLightType.LightArrayType(type)
        }

        if (expr is VlangMapInitExpr) {
            val keyValues = expr.keyValues?.keyValueList ?: return null
            val first = keyValues.firstOrNull() ?: return null
            val value = first.valueExpr ?: return null
            val type = value.getType(null) ?: return null

            return VlangLightType.LightMapType(builtinTypes.string!!, type)
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
            return builtinTypes.any
        }

        if (expr is VlangLiteral) {
            return when {
                expr.`true` != null   -> builtinTypes.bool
                expr.`false` != null  -> builtinTypes.bool
                expr.int != null      -> builtinTypes.int
                expr.bin != null      -> builtinTypes.int
                expr.hex != null      -> builtinTypes.int
                expr.oct != null      -> builtinTypes.int
                expr.float != null    -> builtinTypes.f64
                expr.char != null     -> builtinTypes.rune
                expr.nil != null      -> builtinTypes.nil
                else                  -> null
            }
        }

        return null
    }

    private fun processArrayMethodCall(resolved: PsiElement?, signature: VlangSignature?, expr: VlangCallExpr): VlangType? {
        if (resolved !is VlangMethodDeclaration) return null

        val receiverTypeEx = resolved.receiverType.toEx()
        if (receiverTypeEx !is VlangBuiltinArrayTypeEx && receiverTypeEx !is VlangPointerTypeEx) return null

        if (!VlangTypeInferenceUtil.builtInArrayOrPointerTo(receiverTypeEx)) return null

        val returnTypeEx = signature?.result?.type.toEx()

        // like `first() voidptr`
        if (returnTypeEx is VlangVoidPtrTypeEx) {
            val callerTypeEx = VlangTypeInferenceUtil.callerType(expr).toEx()
            if (callerTypeEx is VlangArrayTypeEx) {
                return callerTypeEx.inner?.raw()
            }
        }

        // like `filter(...) array`
        if (returnTypeEx is VlangBuiltinArrayTypeEx) {
            if (resolved.name == VlangTypeInferenceUtil.ARRAY_MAP_METHOD) {
                val firstArg = expr.parameters.firstOrNull()
                val firstArgTypeEx = firstArg?.getType(null).toEx()

                if (firstArgTypeEx is VlangFunctionTypeEx) {
                    val innerType = firstArgTypeEx.result?.raw()
                    if (innerType != null) {
                        return VlangLightType.LightArrayType(innerType)
                    }
                }
            }

            return VlangTypeInferenceUtil.callerType(expr)
        }

        return null
    }

    private fun unwrapNullableType(type: VlangType?): VlangType? {
        if (type == null) return null

        val exType = type.toEx()

        if (exType is VlangNullableTypeEx) {
            val inner = exType.inner ?: return getBuiltinType("void", type)
            return inner.raw()
        }
        if (exType is VlangNotNullableTypeEx) {
            val inner = exType.inner ?: return getBuiltinType("void", type)
            return inner.raw()
        }

        return null
    }

    private fun typeOrParameterType(resolve: VlangTypeOwner, context: ResolveState?): VlangType? {
        val type = resolve.getType(context) ?: return null
        if (resolve is VlangParamDefinition && resolve.isVariadic) {
            return VlangLightType.LightArrayType(type)
        }
        return type
    }

    @JvmStatic
    fun isNumeric(o: VlangLiteral): Boolean {
        return o.int != null || o.bin != null || o.hex != null || o.oct != null || o.float != null
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
    fun isMutable(o: VlangParamDefinition): Boolean {
        val modifiers = o.varModifiers ?: return false
        return modifiers.text.contains("mut")
    }

    @JvmStatic
    fun makeMutable(o: VlangParamDefinition) {
        makeMutable(o.project, o.varModifiers)
    }

    @JvmStatic
    fun getName(o: VlangVarDefinition): String {
        return o.getIdentifier().text
    }

    @JvmStatic
    fun makeMutable(o: VlangVarDefinition) {
        makeMutable(o.project, o.varModifiers)
    }

    @JvmStatic
    fun isPublic(o: VlangVarDefinition): Boolean = true

    @JvmStatic
    fun isCaptured(o: VlangVarDefinition, original: PsiElement): Boolean {
        val functionLit = original.parentOfType<VlangFunctionLit>()
        val captureList = functionLit?.captureList?.captureList ?: emptyList()
        return captureList.find { it.referenceExpression.text == o.name } != null
    }

    @JvmStatic
    fun isMutable(o: VlangVarDefinition): Boolean {
        val inFor = o.parentNth<VlangForClause>(3) != null
        if (inFor) {
            // in for, variable is mutable
            return true
        }
        val modifiers = o.varModifiers ?: return false
        return modifiers.text.contains("mut")
    }

    @JvmStatic
    fun getReference(o: VlangVarDefinition): PsiReference? {
        val createRef = o.parentOfTypes(
            VlangBlock::class,
            VlangForStatement::class,
            VlangIfStatement::class,
        ) != null
        return if (createRef) VlangVarReference(o) else null
    }

    @JvmStatic
    fun makeMutable(o: VlangReceiver) {
        makeMutable(o.project, o.varModifiers)
    }

    private fun makeMutable(project: Project, varModifiers: VlangVarModifiers?) {
        val modifiers = varModifiers ?: return
        val mutModifier = VlangElementFactory.createVarModifiers(project, "mut")
        val space = VlangElementFactory.createSpace(project)
        if (modifiers.firstChild == null) {
            modifiers.add(mutModifier.firstChild)
            modifiers.add(space)
        } else {
            modifiers.add(space)
            modifiers.add(mutModifier.firstChild)
        }
    }

    @JvmStatic
    fun isMutable(o: VlangReceiver): Boolean {
        val modifiers = o.varModifiers ?: return false
        return modifiers.text.contains("mut")
    }

    @JvmStatic
    fun isPublic(o: VlangReceiver): Boolean = true

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
        return VlangLightType.LightFunctionType(o)
    }

    @JvmStatic
    fun getTypeInner(o: VlangGlobalVariableDefinition, context: ResolveState?): VlangType? {
        return o.expression?.getType(context)
    }

    @JvmStatic
    fun getTypeInner(o: VlangVarDefinition, context: ResolveState?): VlangType? {
        val parent = PsiTreeUtil.getStubOrPsiParent(o)
        if (parent is VlangRangeClause) {
            return processRangeClause(o, parent, context)
        }

        if (parent is VlangVarDeclaration) {
            if (parent.parent is VlangIfExpression) {
                val type = getTypeInVarSpec(o, parent, context)
                return unwrapNullableType(type)
            }

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

    private fun processRangeClause(o: VlangVarDefinition, decl: VlangRangeClause, context: ResolveState?): VlangType? {
        val builtinTypes = VlangBuiltinTypesUtil.getInstance(o.project)
        val rightType = decl.expression?.getType(context)
        val varList = decl.varDefinitionList
        if (varList.size == 1) {
            if (rightType is VlangArrayType) {
                return rightType.type
            }
            if (rightType is VlangMapType) {
                return rightType.valueType
            }

            return builtinTypes.any
        }

        val defineIndex = varList.indexOf(o)
        if (defineIndex == 0) {
            if (rightType is VlangMapType) {
                return rightType.keyType
            }
            return builtinTypes.int
        }

        if (defineIndex == 1) {
            if (rightType is VlangArrayType) {
                return rightType.type
            }
            if (rightType is VlangMapType) {
                return rightType.valueType
            }

            return builtinTypes.any
        }

        return builtinTypes.any
    }

    private fun getTypeInVarSpec(o: VlangVarDefinition, decl: VlangVarDeclaration, context: ResolveState?): VlangType? {
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
        return processNamedElements(processor, ResolveState.initial(), parameters.paramDefinitionList, true)
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
        for (definition in elements) {
            if (!condition.value(definition)) continue
            if (!definition.isValid || checkContainingFile)
                continue
            if (!processor.execute(definition, state.put(LOCAL_RESOLVE, localResolve)))
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