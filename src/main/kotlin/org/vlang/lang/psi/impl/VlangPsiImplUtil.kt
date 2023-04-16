package org.vlang.lang.psi.impl

import com.intellij.codeInsight.highlighting.ReadWriteAccessDetector.Access
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Condition
import com.intellij.openapi.util.Conditions
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.RecursionManager
import com.intellij.psi.*
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet
import com.intellij.psi.impl.source.tree.LeafElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.scope.PsiScopeProcessor
import com.intellij.psi.util.*
import org.vlang.configurations.VlangConfiguration
import org.vlang.ide.codeInsight.VlangAttributesUtil
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.ide.codeInsight.VlangGenericInferer
import org.vlang.ide.codeInsight.VlangTypeInferenceUtil
import org.vlang.lang.VlangTypes
import org.vlang.lang.VlangTypes.SAFE_DOT
import org.vlang.lang.codeInsight.controlFlow.VlangControlFlow
import org.vlang.lang.codeInsight.controlFlow.VlangControlFlowUtil
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangConditionInstruction
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangInstruction
import org.vlang.lang.codeInsight.controlFlow.instructions.VlangInstructionProcessor
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangReferenceBase.Companion.LOCAL_RESOLVE
import org.vlang.lang.psi.impl.VlangReferenceBase.Companion.NOT_PROCESS_EMBEDDED_DEFINITION
import org.vlang.lang.psi.impl.imports.VlangModuleReference
import org.vlang.lang.psi.types.*
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.isGeneric
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.unwrapAlias
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.unwrapArray
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.unwrapPointer
import org.vlang.lang.sql.VlangSqlUtil
import org.vlang.utils.childOfType
import org.vlang.utils.inside
import org.vlang.utils.parentNth
import org.vlang.utils.stubOrPsiParentOfType

object VlangPsiImplUtil {
    @JvmStatic
    fun getName(o: VlangFunctionDeclaration): String {
        val stub = o.stub
        if (stub != null) {
            return stub.name ?: ""
        }

        return o.getIdentifier().text ?: ""
    }

    @JvmStatic
    fun isDefinition(o: VlangFunctionDeclaration): Boolean {
        return o.getBlock() == null
    }

    @JvmStatic
    fun isNoReturn(o: VlangFunctionDeclaration): Boolean {
        return o.attributes?.attributeList?.any {
            VlangAttributesUtil.isNoReturn(it)
        } == true
    }

    @JvmStatic
    fun isGeneric(o: VlangFunctionDeclaration): Boolean {
        return o.genericParameters != null
    }

    @JvmStatic
    fun isCompileTime(o: VlangFunctionDeclaration): Boolean {
        return o.name.startsWith("$")
    }

    @JvmStatic
    fun scope(o: VlangFunctionDeclaration): VlangScope {
        return VlangScopeImpl(o)
    }

    @JvmStatic
    fun controlFlow(o: VlangFunctionDeclaration): VlangControlFlow {
        return o.scope().controlFlow()
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
    fun getTypeInner(o: VlangInterfaceDeclaration, context: ResolveState?): VlangTypeEx {
        return o.interfaceType.toEx()
    }

    @JvmStatic
    fun getIdentifier(o: VlangEnumDeclaration): PsiElement? {
        return o.enumType.identifier
    }

    @JvmStatic
    fun getName(o: VlangStructDeclaration): String {
        val stub = o.stub
        if (stub != null) {
            return stub.name ?: ""
        }

        return o.getIdentifier()?.text ?: ""
    }

    @JvmStatic
    fun isUnion(o: VlangStructDeclaration): Boolean {
        val stub = o.stub
        if (stub != null) {
            return stub.isUnion
        }

        return o.structType.isUnion
    }

    @JvmStatic
    fun getKindName(o: VlangStructDeclaration): String {
        return if (o.isUnion) "union" else "struct"
    }

    @JvmStatic
    fun isAttribute(o: VlangStructDeclaration): Boolean {
        return o.attributes?.attributeList?.any {
            VlangAttributesUtil.isAttributeStruct(it)
        } == true
    }

    @JvmStatic
    fun addField(o: VlangStructDeclaration, name: String, type: String, mutable: Boolean): PsiElement? {
        val fieldDeclaration = VlangElementFactory.createFieldDeclaration(o.project, name, type)
        val groups = o.structType.fieldsGroupList

        if (groups.isEmpty()) {
            val lbrace = o.structType.lbrace
            var toDelete = lbrace?.nextSibling
            while (toDelete != null && toDelete != o.structType.rbrace) {
                toDelete.delete()
                toDelete = toDelete.nextSibling
            }

            val nl = o.structType.addAfter(VlangElementFactory.createNewLine(o.project), lbrace)
            val newGroup = VlangElementFactory.createFieldsGroup(o.project, mutable, name, type)
            val addedGroup = o.structType.addAfter(newGroup, nl)
            o.structType.addBefore(VlangElementFactory.createNewLine(o.project), addedGroup)
            return newGroup.fieldDeclarationList.firstOrNull()
        }

        val group = if (mutable) {
            groups.find {
                it.memberModifiers != null && it.memberModifiers!!.text.contains("mut")
            }
        } else {
            groups.find { it.memberModifiers == null || !it.memberModifiers!!.text.contains("mut") }
        }

        return if (group != null) {
            group.add(VlangElementFactory.createNewLine(o.project))
            val decl = group.add(fieldDeclaration)
            group.addBefore(VlangElementFactory.createSpace(o.project), decl)
        } else {
            val newGroup = VlangElementFactory.createFieldsGroup(o.project, mutable, name, type)
            val addedGroup = o.structType.addBefore(newGroup, o.structType.rbrace)
            o.structType.addAfter(VlangElementFactory.createNewLine(o.project), addedGroup)
            newGroup.fieldDeclarationList.firstOrNull()
        }
    }

    @JvmStatic
    fun isPrimary(o: VlangFieldDefinition): Boolean {
        val decl = o.parent as? VlangFieldDeclaration ?: return false
        return decl.attribute?.let { VlangAttributesUtil.isPrimaryField(it) } ?: false
    }

    @JvmStatic
    fun getName(o: VlangEnumDeclaration): String {
        val stub = o.stub
        if (stub != null) {
            return stub.name ?: ""
        }

        return o.getIdentifier()?.text ?: ""
    }

    @JvmStatic
    fun getName(o: VlangInterfaceDeclaration): String {
        val stub = o.stub
        if (stub != null) {
            return stub.name ?: ""
        }

        return o.interfaceType.identifier?.text ?: ""
    }

    @JvmStatic
    fun getName(o: VlangEmbeddedDefinition): String {
        val stub = o.stub
        if (stub != null) {
            return stub.name ?: ""
        }

        return o.getIdentifier()?.text ?: ""
    }

    @JvmStatic
    fun getIdentifier(o: VlangEmbeddedDefinition): PsiElement? {
        return o.type.identifier
    }

    @JvmStatic
    fun isPublic(o: VlangConstDefinition): Boolean {
        val decl = o.parent as? VlangConstDeclaration ?: return true
        val visibility = VlangPsiTreeUtil.getChildOfType(decl, VlangSymbolVisibility::class.java)
        return visibility?.pub != null
    }

    @JvmStatic
    fun isCompileTime(o: VlangConstDefinition): Boolean {
        return o.name.startsWith("@")
    }

    @JvmStatic
    fun getName(o: VlangConstDefinition): String {
        val stub = o.stub
        if (stub != null) {
            return stub.name ?: ""
        }

        return o.getIdentifier().text ?: ""
    }

    @JvmStatic
    fun getName(o: VlangTypeAliasDeclaration): String {
        val stub = o.stub
        if (stub != null) {
            return stub.name ?: ""
        }

        return o.getIdentifier()?.text ?: ""
    }

    @JvmStatic
    fun getTypeInner(o: VlangTypeAliasDeclaration, context: ResolveState?): VlangTypeEx {
        return o.aliasType.toEx()
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
    fun getAliasName(o: VlangImportSpec): String? {
        return o.importAlias?.name
    }

    @JvmStatic
    fun getPathName(o: VlangImportSpec): String {
        return o.importPath.lastPart
    }

    @JvmStatic
    fun resolve(o: VlangImportSpec): List<VlangModule> {
        return o.importPath.importNameList.lastOrNull()?.resolve() ?: emptyList()
    }

    @JvmStatic
    fun getQualifiedName(o: VlangImportPath): String {
        if (o.importNameList.isEmpty()) {
            // TODO: looks like hack
            return o.firstChild?.text ?: ""
        }

        return o.importNameList.joinToString(".") { it.text }
    }

    @JvmStatic
    fun getLastPart(o: VlangImportPath): String {
        return o.lastPartPsi.text
    }

    @JvmStatic
    fun getLastPartPsi(o: VlangImportPath): PsiElement {
        return o.importNameList.lastOrNull() ?: o.firstChild
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
    fun getParameters(o: VlangGenericParameters): List<VlangGenericParameter> {
        return o.childrenOfType<VlangGenericParameterList>().firstOrNull()?.genericParameterList ?: emptyList()
    }

    @JvmStatic
    fun getTypeArguments(o: VlangGenericArguments): List<VlangType> {
        return o.childrenOfType<VlangTypeListNoPin>().firstOrNull()?.typeList ?: emptyList()
    }

    @JvmStatic
    fun getIdentifier(o: VlangFieldName): PsiElement {
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
    fun getReference(o: VlangIsRefTypeCallExpr) = VlangBuiltinReference(o)

    @JvmStatic
    fun getReference(o: VlangSizeOfCallExpr) = VlangBuiltinReference(o)

    @JvmStatic
    fun getReference(o: VlangTypeOfCallExpr) = VlangBuiltinReference(o)

    @JvmStatic
    fun getReference(o: VlangOffsetOfCallExpr) = VlangBuiltinReference(o)

    @JvmStatic
    fun getReference(o: VlangDumpCallExpr) = VlangBuiltinReference(o)

    @JvmStatic
    fun getReference(o: VlangReferenceExpression): VlangReference {
        return VlangReference(o)
    }

    @JvmStatic
    fun getReference(o: VlangImportName): VlangModuleReference<VlangImportName> {
        return VlangModuleReference(o)
    }

    @JvmStatic
    fun resolve(o: VlangImportName): List<VlangModule> {
        val target = o.reference.resolve() as? VlangModule.VlangPomTargetPsiElement ?: return emptyList()
        return listOf(target.target)
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
        val identifier = o.getIdentifier()
        identifier.replace(VlangElementFactory.createIdentifier(o.project, newName))
        return o
    }

    @JvmStatic
    fun getName(o: VlangImportName): String {
        val stub = o.stub
        if (stub != null) {
            return stub.name ?: ""
        }
        return o.getIdentifier().text
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
    fun getQualifiedName(o: VlangImportName): String {
        val qualifier = o.qualifier
        if (qualifier.isEmpty()) {
            return o.name
        }
        return qualifier + "." + o.name
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
    fun isMutable(o: VlangCapture): Boolean {
        return o.varModifiers?.isMutable ?: false
    }

    @JvmStatic
    fun makeMutable(o: VlangCapture) {
        makeMutable(o.project, o.varModifiers)
    }

    @JvmStatic
    fun isMutable(o: VlangVarModifiers): Boolean {
        return o.varModifierList.any { it.text == "mut" }
    }

    @JvmStatic
    fun getOwnFieldList(o: VlangAnonymousStructType): List<VlangFieldDefinition> {
        return o.fieldsGroupList.flatMap { it.fieldDeclarationList }.mapNotNull { it.fieldDefinition }
    }

    @JvmStatic
    fun getFieldList(o: VlangAnonymousStructType): List<VlangFieldDefinition> {
        return getOwnFieldList(o)
    }

    @JvmStatic
    fun getOwnFieldList(o: VlangStructType): List<VlangFieldDefinition> {
        return o.fieldsGroupList.flatMap { it.fieldDeclarationList }.mapNotNull { it.fieldDefinition }
    }

    @JvmStatic
    fun getFieldList(o: VlangStructType): List<VlangFieldDefinition> {
        val ownFields = getOwnFieldList(o)

        val embeddedStructFields = o.embeddedStructs
            .map { it.toEx().unwrapAlias() }
            .filterIsInstance<VlangStructTypeEx>()
            .mapNotNull { it.anchor() as? VlangStructType }
            .flatMap { it.fieldList }

        return ownFields + embeddedStructFields
    }

    @JvmStatic
    fun getEmbeddedStructList(o: VlangStructType): List<VlangEmbeddedDefinition> {
        return o.fieldsGroupList.flatMap { it.fieldDeclarationList }.mapNotNull { it.embeddedDefinition }
    }

    @JvmStatic
    fun getEmbeddedStructs(o: VlangStructType): List<VlangStructType> {
        return o.embeddedStructList
            .map { it.type.toEx().unwrapAlias() }
            .filterIsInstance<VlangStructTypeEx>()
            .mapNotNull { it.anchor() as? VlangStructType }
    }

    @JvmStatic
    fun isUnion(o: VlangStructType): Boolean {
        return o.union != null
    }

    @JvmStatic
    fun getMemberModifierList(o: VlangFieldsGroup): List<VlangMemberModifier> {
        return o.memberModifiers?.memberModifierList ?: emptyList()
    }

    @JvmStatic
    fun getMemberModifierList(o: VlangMembersGroup): List<VlangMemberModifier> {
        return o.memberModifiers?.memberModifierList ?: emptyList()
    }

    @JvmStatic
    fun getType(o: VlangEmbeddedDefinition, context: ResolveState?): VlangTypeEx {
        return o.type.toEx()
    }

    @JvmStatic
    fun getEmbeddedInterfacesList(o: VlangInterfaceType): List<VlangEmbeddedDefinition> {
        return o.membersGroupList.flatMap { it.fieldDeclarationList }.mapNotNull { it.embeddedDefinition }
    }

    @JvmStatic
    fun getEmbeddedInterfaces(o: VlangInterfaceType): List<VlangInterfaceType> {
        return o.embeddedInterfacesList
            .map { it.type.toEx().unwrapAlias() }
            .filterIsInstance<VlangInterfaceTypeEx>()
            .mapNotNull { it.anchor() as? VlangInterfaceType }
    }

    @JvmStatic
    fun getOwnFieldList(o: VlangInterfaceType): List<VlangFieldDefinition> {
        return o.membersGroupList.flatMap { it.fieldDeclarationList }.mapNotNull { it.fieldDefinition }
    }

    @JvmStatic
    fun getFieldList(o: VlangInterfaceType): List<VlangFieldDefinition> {
        val ownFields = getOwnFieldList(o)

        val embeddedFields = o.embeddedInterfaces
            .flatMap { it.fieldList }

        return ownFields + embeddedFields
    }

    @JvmStatic
    fun getMethodList(o: VlangInterfaceType): List<VlangInterfaceMethodDefinition> {
        val ownMethods = o.membersGroupList.flatMap { it.interfaceMethodDeclarationList }.map { it.interfaceMethodDefinition }

        val embeddedMethods = o.embeddedInterfaces
            .flatMap { it.methodList }

        return ownMethods + embeddedMethods
    }

    @JvmStatic
    fun getFieldList(o: VlangEnumType): List<VlangEnumFieldDefinition> {
        return o.enumFieldDeclarationList.map { it.enumFieldDefinition }
    }

    @JvmStatic
    fun constantValue(o: VlangEnumFieldDefinition): Long? {
        return CachedValuesManager.getCachedValue(o) {
            val value = constantValueImpl(o)
            CachedValueProvider.Result.create(value, o)
        }
    }

    private fun constantValueImpl(o: VlangEnumFieldDefinition): Long? {
        val decl = o.parent as? VlangEnumFieldDeclaration ?: return null
        val enumType = o.parentNth<VlangEnumType>(2) ?: return null
        val enumDecl = enumType.parent as? VlangEnumDeclaration ?: return null
        val binary = enumDecl.isFlag
        val fields = enumType.enumFieldDeclarationList

        if (decl.expression != null) {
            val value = calculateValue(decl.expression)
            if (!binary && value != null) {
                return value
            }
            return null
        }

        val indexOf = fields.indexOf(decl)
        if (indexOf == 0) {
            if (fields.first().expression != null) {
                val value = calculateValue(fields.first().expression)
                if (value != null) {
                    return value
                }
                return null
            }
            return if (binary) 1 else 0
        }

        val prevField = fields[indexOf - 1].enumFieldDefinition.constantValue()
        if (prevField != null) {
            if (binary) {
                return prevField * 2
            }
            return prevField + 1
        }

        return null
    }

    private fun calculateValue(value: VlangExpression?): Long? {
        if (value is VlangLiteral && value.isNumeric) {
            val radix = when {
                value.bin != null -> 2
                value.oct != null -> 8
                value.hex != null -> 16
                else              -> 10
            }
            val text = value.text.removePrefix("0b").removePrefix("0o").removePrefix("0x")
            return try {
                text.toLong(radix)
            } catch (e: NumberFormatException) {
                null
            }
        }
        if (value is VlangUnaryExpr && value.minus != null) {
            val calculatedValue = calculateValue(value.expression) ?: return null
            return -calculatedValue
        }
        if (value is VlangShiftLeftExpr) {
            val left = calculateValue(value.left) ?: return null
            val right = calculateValue(value.right) ?: return null
            return left shl right.toInt()
        }

        return null
    }

    @JvmStatic
    fun isPublic(o: VlangEnumFieldDefinition): Boolean {
        return o.parentOfType<VlangEnumDeclaration>()?.isPublic() ?: false
    }

    @JvmStatic
    fun getTypeInner(o: VlangEnumFieldDefinition, context: ResolveState?): VlangTypeEx {
        return o.parentOfType<VlangEnumType>().toEx()
    }

    @JvmStatic
    fun isPublic(o: VlangInterfaceMethodDefinition): Boolean {
        return true
    }

    @JvmStatic
    fun isMutable(o: VlangInterfaceMethodDefinition): Boolean {
        return isMutableMember(o)
    }

    private fun isMutableMember(member: VlangNamedElement): Boolean {
        val group = member.parentOfType<VlangMemberModifiersOwner>()
        val modifiers = group?.memberModifierList
        return modifiers?.any { it.text == "mut" } ?: false
    }

    @JvmStatic
    fun isMutable(o: VlangFieldDefinition): Boolean {
        if (isMutableMember(o)) {
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
    fun makeImmutable(o: VlangFieldDefinition) {
        // TODO: implement
    }

    @JvmStatic
    fun isPublic(o: VlangFieldDefinition): Boolean {
        if (o.parentOfType<VlangInterfaceType>() != null) {
            return true
        }

        val group = o.parentOfType<VlangFieldsGroup>()
        val modifiers = group?.memberModifiers?.memberModifierList
        if (modifiers != null && modifiers.any { it.text == "pub" }) {
            return true
        }

        val parentStruct = o.getOwner() as? VlangStructDeclaration ?: return false
        return VlangAttributesUtil.isParamsStruct(parentStruct)
    }

    @JvmStatic
    fun getOwner(o: VlangFieldDefinition): VlangNamedElement {
        return o.stubOrPsiParentOfType()
            ?: error("Can't find owner for field ${o.name}, field definition must be inside a struct, union or interface")
    }

    @JvmStatic
    fun getOwner(o: VlangInterfaceMethodDefinition): VlangInterfaceDeclaration {
        return o.stubOrPsiParentOfType()
            ?: error("Can't find owner for method ${o.name}, interface method definition must be inside interface")
    }

    @JvmStatic
    fun getOwner(o: VlangMethodDeclaration): VlangNamedElement? {
        val type = o.receiverType.toEx()
        val unwrappedType = if (type is VlangPointerTypeEx) type.inner else type
        if (unwrappedType is VlangResolvableTypeEx<*>) {
            return unwrappedType.resolve(o.project)
        }
        return null
    }

    @JvmStatic
    fun isMutable(o: VlangMethodDeclaration): Boolean {
        val receiver = o.receiver
        return receiver.isMutable()
    }

    @JvmStatic
    fun byReference(o: VlangMethodDeclaration): Boolean {
        return o.receiver.takeReference()
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

        if (type.inside<VlangReceiver>()) {
            // like T or U
            val text = type.text
            if (text.length == 1 && text[0].isUpperCase()) {
                return VlangLightType.VlangGenericType(text, type)
            }
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

        if (resolved is VlangGenericParameter) {
            return VlangLightType.VlangGenericType(resolved.name!!, resolved)
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
        val owner = o.parentOfTypes(VlangStructDeclaration::class, VlangInterfaceDeclaration::class, VlangAnonymousStructType::class)

        if (owner is VlangNamedElement) {
            return owner.getQualifiedName() + "." + o.name
        }

        if (owner is VlangAnonymousStructType) {
            val name = o.name ?: return null
            val parts = mutableListOf<String>()

            val containingStruct = owner.parentOfType<VlangStructDeclaration>()
            if (containingStruct != null) {
                val qualifiedName = containingStruct.getQualifiedName()
                if (qualifiedName != null) {
                    parts.add(qualifiedName)
                }
            } else {
                val containingFile = owner.containingFile as? VlangFile ?: return null
                val moduleName = containingFile.getModuleQualifiedName()
                if (moduleName.isNotEmpty()) {
                    parts.add(moduleName)
                }
            }

            parts.add("<anonymous struct>")
            parts.add(name)
            return parts.joinToString(".")
        }

        return null
    }

    @JvmStatic
    fun getQualifiedName(o: VlangMethodDeclaration): String? {
        val owner = o.getOwner()

        if (owner is VlangNamedElement) {
            return owner.getQualifiedName() + "." + o.name
        }

        return (o as? VlangNamedElementImpl<*>)?.getQualifiedNameBase()
    }

    @JvmStatic
    fun getQualifiedName(o: VlangInterfaceMethodDefinition): String {
        val owner = o.getOwner()
        return owner.getQualifiedName() + "." + o.name
    }

    @JvmStatic
    fun resolve(o: VlangReferenceExpression): PsiElement? {
        return o.reference.resolve()
    }

    @JvmStatic
    fun safeAccess(o: VlangReferenceExpression): Boolean {
        val leafs = o.childrenOfType<LeafPsiElement>()
        return leafs.any { it.elementType == SAFE_DOT }
    }

    @JvmStatic
    fun getReadWriteAccess(o: VlangReferenceExpression): Access {
        val expression = getConsiderableExpression(o)
        val parent = expression.parent

        if (parent is VlangLeftHandExprList) {
            val grandParent = parent.getParent()
            if (grandParent is VlangAssignmentStatement) {
                // += or =
                return if (grandParent.assignOp.assign == null) Access.ReadWrite else Access.Write
            }

            return Access.Read
        }

        return when (parent) {
            // a++
            is VlangIncDecExpression -> Access.ReadWrite
            // a <- 10
            is VlangSendExpr         -> Access.Write
            // for
            is VlangRangeClause      -> if (expression == parent.expression) Access.Read else Access.Write
            is VlangAppendStatement  -> Access.Write
            else                     -> Access.Read
        }
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
    fun getSize(o: VlangFixedSizeArrayType): Int {
        val expr = o.expression
        if (expr is VlangLiteral) {
            if (expr.isNumeric) {
                val text = expr.text
                if (text != null) {
                    return text.toInt()
                }
            }
        }
        return 0
    }

    @JvmStatic
    fun getVariablesList(o: VlangRangeClause): List<VlangVarDefinition> {
        return o.varDefinitionList
    }

    @JvmStatic
    fun getOwner(o: VlangReturnStatement): VlangSignatureOwner? {
        return o.parentOfType()
    }

    @JvmStatic
    fun getLeft(o: VlangAppendStatement): VlangExpression? {
        return o.childrenOfType<VlangExpression>().firstOrNull()
    }

    @JvmStatic
    fun getReference(o: VlangLabelRef): VlangLabelReference {
        return VlangLabelReference(o)
    }

    @JvmStatic
    fun getReference(o: VlangAttributeIdentifier): VlangAttributeReference {
        return VlangAttributeReference(o)
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

    fun prevNumber(e: PsiElement?): Boolean {
        val prev = if (e == null) null else PsiTreeUtil.prevVisibleLeaf(e)
        if (prev !is LeafElement) {
            return false
        }
        return prev.elementType in listOf(
            VlangTypes.INT,
            VlangTypes.FLOAT,
            VlangTypes.BIN,
            VlangTypes.OCT,
            VlangTypes.HEX,
        )
    }

    fun prevAngleParen(e: PsiElement?): Boolean {
        val prev = if (e == null) null else PsiTreeUtil.prevVisibleLeaf(e)
        return prev is LeafElement && (prev as LeafElement).elementType === VlangTypes.LESS
    }

    fun prevLeftBracket(e: PsiElement?): Boolean {
        val prev = if (e == null) null else PsiTreeUtil.prevVisibleLeaf(e)
        return prev is LeafElement && (prev as LeafElement).elementType === VlangTypes.LBRACK
    }

    fun prevComma(e: PsiElement?): Boolean {
        val prev = if (e == null) null else PsiTreeUtil.prevVisibleLeaf(e)
        return prev is LeafElement && (prev as LeafElement).elementType === VlangTypes.COMMA
    }

    @JvmStatic
    fun addImport(file: VlangFile, list: VlangImportList?, name: String, alias: String?): VlangImportSpec {
        val project = file.project

        val decl = VlangElementFactory.createImportDeclaration(project, name, alias)!!
        if (list == null) {
            var importList = VlangElementFactory.createImportList(project, name, alias)!!
            val modulePsi = file.getModule()
            val shebangPsi = file.getShebang()
            val anchor = modulePsi ?: shebangPsi

            importList = if (anchor == null) {
                file.addBefore(VlangElementFactory.createDoubleNewLine(project), file.firstChild)
                file.addBefore(importList, file.firstChild) as VlangImportList
            } else {
                val listPsi = file.addAfter(importList, anchor) as VlangImportList
                file.addBefore(VlangElementFactory.createDoubleNewLine(project), listPsi)

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
    fun getQualifiedName(o: VlangImportAlias): String {
        return o.name
    }

    @JvmStatic
    fun getReference(o: VlangImportAliasName): VlangModuleReference<VlangImportAliasName> {
        return VlangModuleReference(o)
    }

    @JvmStatic
    fun getName(o: VlangModuleClause): String {
        val stub = o.stub
        if (stub != null) {
            return stub.name ?: ""
        }

        return o.getIdentifier()?.text ?: "<unknown>"
    }

    @JvmStatic
    fun getName(o: VlangImportAlias): String {
        val stub = o.stub
        if (stub != null) {
            return stub.name ?: ""
        }

        return o.importAliasName?.identifier?.text ?: ""
    }

    @JvmStatic
    fun getName(o: VlangParamDefinition): String? {
        val stub = o.stub
        if (stub != null) {
            return stub.name ?: ""
        }

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
    fun resultCount(o: VlangSignature): Pair<Int, Int> {
        return when (val type = o.result?.type) {
            is VlangTupleType -> type.typeListNoPin.typeList.size to type.typeListNoPin.typeList.size
            is VlangResultType,
            is VlangOptionType,
                              -> {
                val inner = type.type ?: return 0 to 1
                if (inner is VlangTupleType) return 1 to inner.typeListNoPin.typeList.size

                1 to 1
            }

            null              -> 0 to 0
            else              -> 1 to 1
        }
    }

    @JvmStatic
    fun isVoid(o: VlangResult): Boolean {
        val type = o.type
        if (type is VlangTupleType) {
            return type.typeListNoPin.typeList.isEmpty()
        }
        return false
    }

    @JvmStatic
    fun isVariadic(o: VlangParamDefinition): Boolean {
        return o.tripleDot != null
    }

    @JvmStatic
    fun isSlice(o: VlangIndexOrSliceExpr): Boolean {
        val lbrack = o.lbrack ?: o.hashLbrack ?: return false
        return o.emptySlice != null ||
                PsiTreeUtil.findSiblingForward(lbrack, VlangTypes.RANGE, false, null) != null ||
                PsiTreeUtil.findSiblingForward(lbrack, VlangTypes.RANGE_EXPR, false, null) != null
    }

    @JvmStatic
    fun getExpression(o: VlangIndexOrSliceExpr): PsiElement? {
        return o.expressionList.firstOrNull()
    }

    @JvmStatic
    fun getRange(o: VlangIndexOrSliceExpr): Pair<PsiElement?, PsiElement?>? {
        if (o.emptySlice != null) return null to null

        val rangeExpr = o.childOfType<VlangRangeExpr>()
        if (rangeExpr != null) {
            return rangeExpr.left to rangeExpr.right
        }

        val lbrack = o.lbrack ?: o.hashLbrack ?: return null
        val rbrack = o.rbrack

        // arr[..10]
        val rangeVariant1 = lbrack.nextSibling
        // arr[10..]
        val rangeVariant2 = rbrack.prevSibling

        if (rangeVariant1.textMatches("..")) {
            // arr[..<10]
            return null to rangeVariant2
        } else if (rangeVariant2.textMatches("..")) {
            // arr[10≤..]
            return rangeVariant1 to null
        }

        return null
    }

    @JvmStatic
    fun getSliceStart(o: VlangIndexOrSliceExpr): PsiElement? {
        return getRange(o)?.first
    }

    @JvmStatic
    fun getSliceEnd(o: VlangIndexOrSliceExpr): PsiElement? {
        return getRange(o)?.second
    }

    @JvmStatic
    fun getParameters(o: VlangCallExpr): List<VlangExpression> {
        return o.argumentList.elementList.mapNotNull { it?.value?.expression }
    }

    /**
     * For example, for `foo.bar()` it returns `bar`
     *
     * For `foo()` it returns `foo`
     */
    @JvmStatic
    fun getIdentifier(o: VlangCallExpr): PsiElement? {
        val refExpr = o.expression as? VlangReferenceExpression ?: return null
        return refExpr.getIdentifier()
    }

    /**
     * For example, for `foo.bar()` it returns `foo`
     *
     * For `foo()` it returns null
     *
     * For `foo.bar.baz()` it returns `foo.bar`
     */
    @JvmStatic
    fun getQualifier(o: VlangCallExpr): VlangReferenceExpression? {
        val refExpr = o.expression as? VlangReferenceExpression ?: return null
        return refExpr.getQualifier() as? VlangReferenceExpression
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
        return o.guardVarDeclaration != null
    }

    private fun getNotNullElement(vararg elements: PsiElement?): PsiElement? {
        for (e in elements) {
            if (e != null) return e
        }
        return null
    }

    @JvmStatic
    fun getOperator(o: VlangBinaryExpr): PsiElement? {
        if (o is VlangAndExpr) return o.condAnd
        if (o is VlangOrExpr) return o.condOr
        if (o is VlangShiftLeftExpr) return o.shiftLeftOp

        if (o is VlangMulExpr) {
            return getNotNullElement(o.mul, o.quotient, o.remainder, o.bitAnd, o.bitClear)
        }
        if (o is VlangAddExpr) {
            return getNotNullElement(o.bitXor, o.bitOr, o.minus, o.plus)
        }
        if (o is VlangConditionalExpr) {
            return getNotNullElement(o.eq, o.notEq, o.greater, o.greaterOrEqual, o.less, o.lessOrEqual)
        }

        return null
    }

    @JvmStatic
    fun getReference(o: VlangEnumFetch): VlangReference {
        return VlangReference(o, false)
    }

    @JvmStatic
    fun getQualifier(o: VlangEnumFetch): VlangCompositeElement? {
        return null
    }

    @JvmStatic
    fun resolve(o: VlangEnumFetch): PsiElement? {
        return o.reference.resolve()
    }

    @JvmStatic
    fun isFixedSize(o: VlangArrayCreation): Boolean {
        return o.not != null
    }

    @JvmStatic
    fun getArms(o: VlangMatchExpression): List<VlangMatchArm> {
        val matchArms = o.matchArms ?: return emptyList()
        return matchArms.matchArmList
    }

    @JvmStatic
    fun getExpressionArms(o: VlangMatchExpression): List<VlangExpression> {
        val matchArms = o.matchArms ?: return emptyList()
        return matchArms.matchArmList
            .flatMap { VlangPsiTreeUtil.getChildrenOfTypeAsList(it, VlangExpression::class.java) }
    }

    @JvmStatic
    fun getTypeArms(o: VlangMatchExpression): List<VlangType> {
        val matchArms = o.matchArms ?: return emptyList()
        return matchArms.matchArmList
            .flatMap { VlangPsiTreeUtil.getChildrenOfTypeAsList(it, VlangType::class.java) }
    }

    @JvmStatic
    fun getElseArm(o: VlangMatchExpression): VlangMatchElseArmClause? {
        val matchArms = o.matchArms ?: return null
        return matchArms.matchElseArmClauseList.firstOrNull()
    }

    @JvmStatic
    fun withElse(o: VlangMatchExpression): Boolean {
        return o.elseArm != null
    }

    @JvmStatic
    fun getParameterList(o: VlangMatchArm): List<VlangCompositeElement> {
        return VlangPsiTreeUtil.getChildrenOfTypeAsList(o, VlangCompositeElement::class.java)
            .filter { !PsiTreeUtil.isAncestor(o.block, it, false) }
    }

    @JvmStatic
    fun getType(o: VlangSqlExpression, context: ResolveState?): VlangTypeEx? {
        val lastStatement = o.sqlBlock.sqlBlockStatementList.lastOrNull() ?: return null
        if (lastStatement is VlangSqlSelectStatement) {
            if (lastStatement.sqlSelectCountClause != null) {
                return VlangPrimitiveTypeEx.INT
            }

            val tableRef = VlangSqlUtil.getTable(lastStatement) ?: return null
            val table = tableRef.typeReferenceExpression.resolve() as? VlangNamedElement ?: return null
            val type = table.getType(context) ?: return null

            return VlangResultTypeEx(
                VlangArrayTypeEx(type, table),
                table
            )
        }
        return null
    }

    @JvmStatic
    fun getName(o: VlangReceiver): String? {
        val stub = o.stub
        if (stub != null) {
            return stub.name ?: ""
        }

        return o.getIdentifier().text
    }

    @JvmStatic
    fun takeReference(o: VlangReceiver): Boolean {
        return o.type is VlangPointerType
    }

    @JvmStatic
    fun getTypeInner(o: VlangReceiver, context: ResolveState?): VlangTypeEx {
        return o.type.toEx()
    }

    @JvmStatic
    fun getTypeInner(o: VlangFieldDefinition, context: ResolveState?): VlangTypeEx {
        val fieldDeclaration = o.parent as? VlangFieldDeclaration
        return fieldDeclaration?.type.toEx()
    }

    @JvmStatic
    fun isMultiline(o: VlangConstDeclaration): Boolean {
        return o.lparen != null
    }

    @JvmStatic
    fun isMultiline(o: VlangGlobalVariableDeclaration): Boolean {
        return o.lparen != null
    }

    @JvmStatic
    fun getTypeInner(o: VlangConstDefinition, context: ResolveState?): VlangTypeEx? {
        val expr = o.expression ?: return null
        if (expr.text == o.name) return null
        return getTypeInner(expr, context)
    }

    @JvmStatic
    fun getTypeInner(o: VlangStructDeclaration, context: ResolveState?): VlangTypeEx {
        return o.structType.toEx()
    }

    @JvmStatic
    fun getTypeInner(o: VlangEnumDeclaration, context: ResolveState?): VlangTypeEx {
        return o.enumType.toEx()
    }

    @JvmStatic
    fun isFlag(o: VlangEnumDeclaration): Boolean {
        return o.attributes?.attributeList?.any {
            VlangAttributesUtil.isFlagEnum(it)
        } == true
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
    fun getType(o: VlangExpression, context: ResolveState?): VlangTypeEx? {
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

    @JvmStatic
    fun getTypeInner(param: VlangGenericParameter, context: ResolveState?): VlangTypeEx? {
        if (param.name == null) {
            return null
        }
        return VlangGenericTypeEx(param.name!!, param)
    }

    private fun unwrapParType(o: VlangExpression, c: ResolveState?): VlangTypeEx? {
        // TODO: Paren type?
        return getTypeInner(o, c)
    }

    private fun getTypeInner(expr: VlangExpression, context: ResolveState?): VlangTypeEx? {
        if (isBoolExpr(expr)) {
            return VlangPrimitiveTypeEx.BOOL
        }

        if (expr is VlangUnaryExpr) {
            if (expr.expression == null) return null
            val exprType = expr.expression!!.getType(context) ?: return null
            when {
                expr.not != null         -> return VlangPrimitiveTypeEx.BOOL
                expr.bitAnd != null      -> return VlangPointerTypeEx(exprType, expr.expression!!)
                expr.mul != null         -> return unwrapPointerType(exprType)
                expr.sendChannel != null -> return unwrapChannelType(exprType)
            }
            return exprType
        }

        if (expr is VlangShiftLeftExpr) {
            return VlangPrimitiveTypeEx.INT
        }

        if (expr is VlangIncDecExpression) {
            return expr.expression.getType(context)
        }

        // 0..10 -> int[]
        if (expr is VlangRangeExpr && expr.tripleDot == null) {
            return VlangArrayTypeEx(VlangPrimitiveTypeEx.INT, expr)
        }

        // `a`...`z` -> typeof(`a`)
        if (expr is VlangRangeExpr && expr.tripleDot != null) {
            return expr.expressionList.firstOrNull()?.getType(context)
        }

        // dump(type) -> type
        if (expr is VlangDumpCallExpr) {
            return expr.expression?.getType(null)
        }

        if (expr is VlangBuiltinCallOwner) {
            val reference = expr.getReference()
            val resolved = reference.resolve()
            if (resolved is VlangFunctionDeclaration) {
                val signature = resolved.getSignature() ?: return null
                return processSignatureReturnType(signature, expr, resolved, false)
            }
        }

        // ?type or {} -> type
        // !type or {} -> type
        if (expr is VlangOrBlockExpr) {
            if (expr.expression == null) return null
            return unwrapOptionOrResultType(expr.expression!!.getType(context))
        }

        // (type) -> type
        if (expr is VlangParenthesesExpr) {
            return expr.expression?.getType(context)
        }

        // "" -> string
        if (expr is VlangStringLiteral) {
            return VlangStringTypeEx.INSTANCE
        }

        if (expr is VlangLiteral) {
            // `a` -> rune
            if (expr.char != null) return VlangPrimitiveTypeEx.RUNE
            // 1 -> int
            if (expr.int != null || expr.hex != null || expr.oct != null || expr.bin != null) return VlangPrimitiveTypeEx.INT
            // 1.0 -> float
            if (expr.float != null) return VlangPrimitiveTypeEx.F64
            // true -> bool
            if (expr.`true` != null || expr.`false` != null) return VlangPrimitiveTypeEx.BOOL
            // nil -> nil
            if (expr.nil != null) return VlangPrimitiveTypeEx.NIL
            // none -> none
            if (expr.none != null) return VlangNoneTypeEx.INSTANCE
        }

        // type1 + type2 -> type1
        if (expr is VlangAddExpr) {
            return expr.left.getType(context)
        }

        if (expr is VlangMulExpr) {
            val left = expr.left
            if (left !is VlangLiteral) return left.getType(context)
            val right = expr.right
            if (right != null) return right.getType(context)
        }

        if (expr is VlangReferenceExpression) {
            // 'it' variable in map/filter/any/all methods of array
            // 'a', 'b' variables in sort method of array
            if (VlangCodeInsightUtil.isItVariable(expr.getIdentifier()) || VlangCodeInsightUtil.isSortABVariable(expr.getIdentifier())) {
                val resolved = expr.reference.resolve()
                if (resolved is VlangTypeOwner) {
                    val inferred = VlangGenericInferer.inferGenericIt(expr)
                    if (inferred != null) {
                        return inferred
                    }
                }
            }

            // expr or { err }
            // if a := foo() { ... } else { err }
            if (VlangCodeInsightUtil.isErrVariable(expr.getIdentifier()) &&
                (VlangCodeInsightUtil.insideOrGuard(expr) || VlangCodeInsightUtil.insideElseBlockIfGuard(expr))
            ) {
                return VlangInterfaceTypeEx.iError(expr)
            }

            val resolved = expr.reference.resolve()
            if (resolved is VlangTypeOwner) {
                val typeFromCasts = typeFromCasts(expr)
                if (typeFromCasts != null) {
                    return typeFromCasts
                }

                val type = typeOrParameterType(resolved, context)
                if (type != null && type.isGeneric()) {
                    return VlangGenericInferer.inferGenericFetch(resolved, expr, type)
                }

                return type
            }
        }

        // <int_array>[1..10] -> int[]
        // <int_array>[1] -> int
        if (expr is VlangIndexOrSliceExpr) {
            val indexExpr = expr.expressionList.firstOrNull() ?: return null
            val indexType = indexExpr.getType(context)?.unwrapPointer()?.unwrapAlias() ?: return null

            // [type, type][0..1] -> type[]
            if (expr.isSlice) {
                if (indexType is VlangFixedSizeArrayTypeEx) {
                    // [3]int -> []int
                    return VlangArrayTypeEx(indexType.inner, indexType.anchor()!!)
                }
                return indexType
            }

            return inferTypeForIndexOrSlice(indexType)
        }

        // type{...} -> type
        if (expr is VlangLiteralValueExpression) {
            return expr.type.toEx()
        }

        // json.decode(type) -> type
        if (expr is VlangJsonCallExpr) {
            return VlangResultTypeEx(expr.jsonArgumentList.type.toEx(), expr)
        }

        // <type>(<expr>) -> type
        if (expr is VlangTypeCastExpression) {
            return expr.type.toEx()
        }

        if (expr is VlangCallExpr) {
            val needUnwrapOptional = expr is VlangCallExprWithPropagate

            val exprType = expr.expression?.getType(context) ?: return null
            val unwrapped = exprType.unwrapAlias()
            if (unwrapped !is VlangFunctionTypeEx) {
                // most probably `Foo[int](100)` cast
                if (expr.genericArguments != null && expr.argumentList.elementList.size == 1) {
                    val specialization = expr.genericArguments!!.typeArguments.map { it.toEx() }
                    return VlangGenericInstantiationEx(exprType, specialization, expr)
                }

                return unwrapOptionOrResultTypeIf(exprType, needUnwrapOptional)
            }

            val signature = unwrapped.signature
            val owner = signature.parent as VlangSignatureOwner

            if (owner is VlangMethodDeclaration) {
                val type = processMapArrayMethodCall(owner, signature, expr)
                if (type != null) return unwrapOptionOrResultTypeIf(type, needUnwrapOptional)

                val type2 = processThreadPoolMethodCall(owner, signature, expr)
                if (type2 != null) return unwrapOptionOrResultTypeIf(type2, needUnwrapOptional)

                val type3 = processThreadMethodCall(owner, signature, expr)
                if (type3 != null) return unwrapOptionOrResultTypeIf(type3, needUnwrapOptional)
            }

            return processSignatureReturnType(signature, expr, owner, needUnwrapOptional)
        }

        if (expr is VlangDotExpression) {
            if (expr.resultPropagationExpression != null || expr.optionPropagationExpression != null) {
                val type = expr.expression?.getType(context)
                return unwrapOptionOrResultType(type)
            }
        }

        // fn () -> type(fn())
        if (expr is VlangFunctionLit) {
            return VlangFunctionTypeEx.from(expr)
        }

        if (expr is VlangUnsafeExpression) {
            val block = expr.block
            val lastStatement = block?.statementList?.lastOrNull() ?: return null
            val lastExpressionList = lastStatement.childrenOfType<VlangLeftHandExprList>().lastOrNull()
            return getTypeInner(lastExpressionList, context)
        }

        // [type, type] -> type[]
        if (expr is VlangArrayCreation) {
            val expressionList = expr.arrayCreationList?.expressionList ?: return null
            val firstItem = expressionList.firstOrNull()
            val type = firstItem?.getType(context) ?: return null
            if (expr.isFixedSize) {
                return VlangFixedSizeArrayTypeEx(type, expressionList.size, firstItem)
            }

            return VlangArrayTypeEx(type, firstItem)
        }

        if (expr is VlangMapInitExpr) {
            val keyValues = expr.keyValues?.keyValueList ?: return null
            val first = keyValues.firstOrNull() ?: return null
            val value = first.valueExpr ?: return null
            val type = value.getType(null) ?: return null

            return VlangMapTypeEx(VlangStringTypeEx.INSTANCE, type, value)
        }

        if (expr is VlangIfExpression) {
            val ifBody = expr.block
            val elseBody = expr.elseBranch?.block

            val ifType = getTypeOfBlock(ifBody, context)
            val elseType = getTypeOfBlock(elseBody, context)

            if (ifType == null) return elseType
            if (elseType == null) return ifType
            if (ifType.toString() == elseType.toString()) return ifType

            // TODO: union type of if and else types
            return ifType
        }

        if (expr is VlangCompileTimeIfExpression) {
            val ifBody = expr.block
            val elseBody = expr.compileTimeElseBranch?.block

            val ifType = getTypeOfBlock(ifBody, context)
            val elseType = getTypeOfBlock(elseBody, context)

            if (ifType == null) return elseType
            if (elseType == null) return ifType
            if (ifType.toString() == elseType.toString()) return ifType

            return ifType
        }

        // type.red -> type
        if (expr is VlangEnumFetch) {
            val field = expr.reference.resolve() as? VlangEnumFieldDefinition
            return field?.getTypeInner(context)
        }

        if (expr is VlangMatchExpression) {
            val block = expr.matchArms?.matchArmList?.firstOrNull()?.block
            return getTypeOfBlock(block, context)
        }

        if (expr is VlangAsExpression) {
            return expr.type.toEx()
        }

        if (expr is VlangSpawnExpression) {
            return VlangThreadTypeEx(expr.expression?.getType(context), expr)
        }

        if (expr is VlangUnpackingExpression) {
            return expr.expression?.getType(context)
        }

        return null
    }

    private fun typeFromCasts(expr: VlangReferenceExpression): VlangTypeEx? {
        val scope = expr.parentOfType<VlangScopeHolder>()?.scope()
        val controlFlow = scope?.controlFlow()
        val instruction = controlFlow?.getInstruction(expr, VlangInstruction::class.java) ?: return null

        var foundType: VlangTypeEx? = null

        VlangControlFlowUtil.processPredecessors(instruction, object : VlangInstructionProcessor() {
            override fun processConditionInstruction(instruction: VlangConditionInstruction): Boolean {

                // match cast
                if (instruction.condition is VlangType) {
                    var matchExpr = expr.parentOfType<VlangMatchExpression>()
                    while (matchExpr != null) {

                        // when `match expr.foo { ... }` need find next match expression
                        if (PsiTreeUtil.isAncestor(matchExpr.expression, expr, false)) {
                            matchExpr = matchExpr.parentOfType()
                        }

                        val expression = matchExpr?.expression
                        val unwrapped = if (expression is VlangMutExpression) expression.expression else expression
                        if (unwrapped?.text == expr.text) {
                            foundType = (instruction.condition as VlangType).toEx()
                            return false
                        }

                        matchExpr = matchExpr?.parentOfType()
                    }
                }

                // if cast
                if (instruction.condition is VlangIsExpression) {
                    if (PsiTreeUtil.isAncestor(instruction.condition, expr, false)) {
                        return true
                    }

                    val isExpr = instruction.condition as VlangIsExpression
                    val isLeftExpr = isExpr.expression
                    val unwrapped = if (isLeftExpr is VlangMutExpression) isLeftExpr.expression else isLeftExpr
                    if (unwrapped?.text == expr.text) {
                        foundType = isExpr.type?.toEx()
                        return false
                    }
                }

                return super.processConditionInstruction(instruction)
            }
        })

        return foundType
    }

    private fun processSignatureReturnType(
        signature: VlangSignature,
        expr: VlangExpression,
        owner: VlangSignatureOwner,
        needUnwrapOptional: Boolean,
    ): VlangTypeEx? {
        val result = signature.result ?: return VlangVoidTypeEx.INSTANCE
        val resultType = result.type.toEx()
        if (resultType.isGeneric() && expr is VlangGenericArgumentsOwner) {
            return unwrapOptionOrResultTypeIf(
                VlangGenericInferer.inferGenericCall(expr, owner, resultType),
                needUnwrapOptional
            )
        }

        return unwrapOptionOrResultTypeIf(resultType, needUnwrapOptional)
    }

    private fun processMapArrayMethodCall(
        owner: VlangMethodDeclaration,
        signature: VlangSignature,
        expr: VlangCallExpr,
    ): VlangTypeEx? {
        val type = processArrayMethodCall(owner, signature, expr)
        if (type != null) {
            return type
        }

        val mapType = processMapMethodCall(owner, signature, expr)
        if (mapType != null) {
            return mapType
        }

        return null
    }

    private fun inferTypeForIndexOrSlice(indexType: VlangTypeEx): VlangTypeEx {
        // [type, type][0] -> type
        if (indexType is VlangArrayTypeEx) {
            return indexType.inner
        }
        // ["1": type, "2": type]["1"] -> type
        if (indexType is VlangMapTypeEx) {
            return indexType.value
        }
        // "1"[0] -> u8
        if (indexType is VlangStringTypeEx) {
            return VlangPrimitiveTypeEx.U8
        }
        // &u8[0] -> u8
        if (indexType is VlangPointerTypeEx) {
            return indexType.inner
        }
        // byteptr(...)[0] -> byte
        if (indexType == VlangPrimitiveTypeEx.BYTEPTR) {
            return VlangPrimitiveTypeEx.BYTE
        }
        // charptr(...)[0] -> byte
        if (indexType == VlangPrimitiveTypeEx.CHARPTR) {
            return VlangPrimitiveTypeEx.CHAR
        }

        return VlangAnyTypeEx.INSTANCE
    }

    private fun getTypeOfBlock(
        body: VlangBlock?,
        context: ResolveState?,
    ): VlangTypeEx? {
        val lastIfStatement = body?.statementList?.lastOrNull()
        val lastIfExpressionList = lastIfStatement?.childrenOfType<VlangLeftHandExprList>()?.lastOrNull()
        return getTypeInner(lastIfExpressionList, context)
    }

    private fun getTypeInner(expr: VlangLeftHandExprList?, context: ResolveState?): VlangTypeEx? {
        if (expr == null) return null

        val types = expr.expressionList.map { it.getType(context) ?: VlangAnyTypeEx.INSTANCE }
        if (types.size == 1) {
            return types.first()
        }
        return VlangTupleTypeEx(types, expr)
    }

    private fun isBoolExpr(expr: VlangExpression) = expr is VlangConditionalExpr ||
            expr is VlangInExpression ||
            expr is VlangNotInExpression ||
            expr is VlangIsExpression ||
            expr is VlangNotIsExpression ||
            expr is VlangAndExpr ||
            expr is VlangOrExpr ||
            expr is VlangSelectExpression

    private fun unwrapPointerType(inner: VlangTypeEx): VlangTypeEx {
        if (inner is VlangPointerTypeEx) {
            return inner.inner
        }

        return inner
    }

    private fun unwrapChannelType(inner: VlangTypeEx): VlangTypeEx {
        if (inner is VlangChannelTypeEx) {
            return inner.inner
        }

        return inner
    }

    private fun processMapMethodCall(resolved: VlangMethodDeclaration, signature: VlangSignature?, expr: VlangCallExpr): VlangTypeEx? {
        val receiverTypeEx = resolved.receiverType.toEx()
        if (!VlangTypeInferenceUtil.builtinMapOrPointerTo(receiverTypeEx)) return null

        val callerType = VlangTypeInferenceUtil.callerType(expr)
        if (callerType !is VlangMapTypeEx) {
            return null
        }

        return when (resolved.name) {
            "keys"          -> VlangArrayTypeEx(callerType.key, expr)
            "values"        -> VlangArrayTypeEx(callerType.value, expr)
            "clone", "move" -> callerType
            else            -> null
        }
    }

    private fun processArrayMethodCall(resolved: VlangMethodDeclaration, signature: VlangSignature?, expr: VlangCallExpr): VlangTypeEx? {
        val receiverType = resolved.receiverType.toEx()
        if (!VlangTypeInferenceUtil.builtinArrayOrPointerTo(receiverType)) return null

        val returnType = signature?.result?.type.toEx()

        // like `first() voidptr`
        if (returnType is VlangVoidPtrTypeEx) {
            val callerType = VlangTypeInferenceUtil.callerType(expr)
            if (callerType is VlangArrayTypeEx) {
                return callerType.inner
            }
        }

        // like `filter(...) array`
        if (returnType is VlangBuiltinArrayTypeEx) {
            if (resolved.name == VlangTypeInferenceUtil.ARRAY_MAP_METHOD) {
                val firstArg = expr.parameters.firstOrNull()
                val firstArgType = firstArg?.getType(null) ?: return null

                // map(fn (int) <type> { ... }) -> array<type>
                if (firstArgType is VlangFunctionTypeEx) {
                    val innerType = firstArgType.result
                    if (innerType != null) {
                        return VlangArrayTypeEx(innerType, firstArg)
                    }
                }

                // map(it > 10) -> array<bool>
                return VlangArrayTypeEx(firstArgType, expr)
            }

            return VlangTypeInferenceUtil.callerType(expr)
        }

        return null
    }

    private fun processThreadPoolMethodCall(
        resolved: VlangMethodDeclaration,
        signature: VlangSignature?,
        expr: VlangCallExpr,
    ): VlangTypeEx? {
        val receiverType = resolved.receiverType.toEx()
        if (!VlangTypeInferenceUtil.stubThreadPool(receiverType)) return null

        val returnType = signature?.result?.type.toEx()

        if (returnType is VlangArrayTypeEx) {
            val callerType = VlangTypeInferenceUtil.callerType(expr)?.unwrapArray()
            if (callerType is VlangThreadTypeEx && callerType.inner != null) {
                return VlangArrayTypeEx(callerType.inner, expr)
            }
        }

        return null
    }

    private fun processThreadMethodCall(resolved: VlangMethodDeclaration, signature: VlangSignature?, expr: VlangCallExpr): VlangTypeEx? {
        val receiverType = resolved.receiverType.toEx()
        if (!VlangTypeInferenceUtil.stubThread(receiverType)) return null

        val returnType = signature?.result?.type.toEx()

        if (returnType is VlangGenericTypeEx) {
            val callerType = VlangTypeInferenceUtil.callerType(expr)
            if (callerType is VlangThreadTypeEx && callerType.inner != null) {
                return callerType.inner
            }
        }

        return null
    }

    fun unwrapOptionOrResultTypeIf(type: VlangTypeEx?, condition: Boolean): VlangTypeEx? {
        if (!condition) return type
        return unwrapOptionOrResultType(type)
    }

    private fun unwrapOptionOrResultType(type: VlangTypeEx?): VlangTypeEx? {
        if (type is VlangOptionTypeEx) {
            return type.inner ?: VlangVoidTypeEx.INSTANCE
        }
        if (type is VlangResultTypeEx) {
            return type.inner ?: VlangVoidTypeEx.INSTANCE
        }

        return type
    }

    fun unwrapPointerOrOptionOrResultType(type: VlangTypeEx?): VlangTypeEx? {
        if (type is VlangPointerTypeEx) {
            return type.inner
        }

        return unwrapOptionOrResultType(type)
    }

    private fun typeOrParameterType(typeOwner: VlangTypeOwner, context: ResolveState?): VlangTypeEx? {
        val type = typeOwner.getType(context)
        if (typeOwner is VlangParamDefinition && typeOwner.isVariadic && type != null) {
            return VlangArrayTypeEx(type, typeOwner)
        }
        return type
    }

    @JvmStatic
    fun isNumeric(o: VlangLiteral): Boolean {
        return o.int != null || o.bin != null || o.hex != null || o.oct != null || o.float != null
    }

    @JvmStatic
    fun isBoolean(o: VlangLiteral): Boolean {
        return o.`true` != null || o.`false` != null
    }

    @JvmStatic
    fun getInitializer(o: VlangVarDefinition): VlangExpression? {
        val parent = o.parent
        if (parent is VlangRangeClause) return null
        if (parent is VlangVarDeclaration) {
            val defineIndex = parent.varDefinitionList.indexOf(o)
            val exprList = parent.expressionList
            return exprList.getOrNull(defineIndex)
        }

        return null
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
        val text = modifiers.text
        return text.contains("mut") || text.contains("shared")
    }

    @JvmStatic
    fun makeMutable(o: VlangParamDefinition) {
        makeMutable(o.project, o.varModifiers)
    }

    @JvmStatic
    fun makeImmutable(o: VlangParamDefinition) {
        makeImmutable(o.varModifiers)
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
    fun makeImmutable(o: VlangVarDefinition) {
        makeImmutable(o.varModifiers)
    }

    @JvmStatic
    fun isPublic(o: VlangVarDefinition): Boolean = true

    @JvmStatic
    fun isMutable(o: VlangVarDefinition): Boolean {
        val inFor = o.parentNth<VlangForClause>(3) != null
        if (inFor) {
            // in for, variable is implicitly mutable
            return true
        }
        val modifiers = o.varModifiers ?: return false
        val text = modifiers.text
        return text.contains("mut") || text.contains("shared")
    }

    @JvmStatic
    fun getReference(o: VlangVarDefinition): PsiReference {
        return VlangVarReference(o)
    }

    @JvmStatic
    fun makeMutable(o: VlangReceiver) {
        makeMutable(o.project, o.varModifiers)

        val type = o.type
        // need remove `&` from type
        // a &Foo -> mut a Foo
        if (type is VlangPointerType) {
            val innerType = type.type
            if (innerType != null) {
                type.replace(innerType)
            }
        }
    }

    @JvmStatic
    fun makeImmutable(o: VlangReceiver) {
        makeImmutable(o.varModifiers)
    }

    private fun makeMutable(project: Project, varModifiers: VlangVarModifiers?) {
        val modifiers = varModifiers ?: return
        val mutModifier = VlangElementFactory.createVarModifiers(project, "mut")
        val space = VlangElementFactory.createSpace(project)
        if (modifiers.firstChild == null) {
            val newModifiers = modifiers.replace(mutModifier)
            newModifiers.parent.addAfter(space, newModifiers)
        } else if (modifiers.firstChild.text != "mut") {
            modifiers.add(space)
            modifiers.add(mutModifier.firstChild)
        }
    }

    private fun makeImmutable(varModifiers: VlangVarModifiers?) {
        val modifiers = varModifiers ?: return
        val modifier = modifiers.varModifierList.find { it.text == "mut" }
        modifier?.delete()
    }

    @JvmStatic
    fun isMutable(o: VlangReceiver): Boolean {
        val modifiers = o.varModifiers ?: return false
        val text = modifiers.text
        return text.contains("mut") || text.contains("shared")
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
    fun getTypeInner(o: VlangSignatureOwner, context: ResolveState?): VlangTypeEx? {
        return VlangFunctionTypeEx.from(o)
    }

    @JvmStatic
    fun getTypeInner(o: VlangGlobalVariableDefinition, context: ResolveState?): VlangTypeEx? {
        if (o.type != null) {
            return o.type?.toEx()
        }
        return o.expression?.getType(context)
    }

    @JvmStatic
    fun getTypeInner(o: VlangVarDefinition, context: ResolveState?): VlangTypeEx? {
        val parent = PsiTreeUtil.getStubOrPsiParent(o)
        if (parent is VlangRangeClause) {
            return processRangeClause(o, parent, context)
        }

        if (parent is VlangVarDeclaration) {
            if (parent.parent is VlangIfExpression) {
                val type = getTypeInVarSpec(o, parent, context)
                return unwrapOptionOrResultType(type)
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

    private fun processRangeClause(o: VlangVarDefinition, decl: VlangRangeClause, context: ResolveState?): VlangTypeEx? {
        val rightType = decl.expression?.getType(context)?.unwrapAlias()
        if (rightType is VlangStringTypeEx) {
            return VlangPrimitiveTypeEx.U8
        }

        if (rightType is VlangStructTypeEx) {
            return processIteratorStruct(o, rightType)
        }

        if (rightType is VlangGenericInstantiationEx) {
            val inner = rightType.inner
            val specMap = rightType.specializationMap(o.project)
            if (inner is VlangStructTypeEx) {
                val type = processIteratorStruct(o, inner)
                if (type != null && type.isGeneric()) {
                    return type.substituteGenerics(specMap)
                }
                return type
            }
        }

        val varList = decl.varDefinitionList
        if (varList.size == 1) {
            if (rightType is VlangArrayTypeEx) {
                return rightType.inner
            }
            if (rightType is VlangMapTypeEx) {
                return rightType.value
            }

            return VlangAnyTypeEx.INSTANCE
        }

        val defineIndex = varList.indexOf(o)
        if (defineIndex == 0) {
            if (rightType is VlangMapTypeEx) {
                return rightType.key
            }
            return VlangPrimitiveTypeEx.INT
        }

        if (defineIndex == 1) {
            if (rightType is VlangArrayTypeEx) {
                return rightType.inner
            }
            if (rightType is VlangMapTypeEx) {
                return rightType.value
            }

            return VlangAnyTypeEx.INSTANCE
        }

        return VlangAnyTypeEx.INSTANCE
    }

    private fun processIteratorStruct(
        o: VlangVarDefinition,
        rightType: VlangTypeEx,
    ): VlangTypeEx? {
        val method = rightType.findMethod(o.project, "next") ?: return VlangAnyTypeEx.INSTANCE
        val result = method.getSignature()?.result ?: return VlangAnyTypeEx.INSTANCE
        val resultType = result.type.toEx()
        return unwrapOptionOrResultType(resultType)
    }

    private fun getTypeInVarSpec(o: VlangVarDefinition, decl: VlangVarDeclaration, context: ResolveState?): VlangTypeEx? {
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
            if (type is VlangTupleTypeEx) {
                if (defineIndex >= type.types.size) {
                    return VlangAnyTypeEx.INSTANCE
                }

                return type.types[defineIndex]
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
        return processParameters(processor, signature.parameters)
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
            if (!condition.value(definition))
                continue
            if (!definition.isValid || checkContainingFile)
                continue

            if (definition is VlangEmbeddedDefinition && state.get(NOT_PROCESS_EMBEDDED_DEFINITION) == true)
                continue

            if (!processor.execute(definition, state.put(LOCAL_RESOLVE, localResolve)))
                return false
        }
        return true
    }

    fun canBeAutoImported(file: VlangFile): Boolean {
        val moduleName = file.getModuleName() ?: return false
        return canBeAutoImported(moduleName)
    }

    fun canBeAutoImported(moduleName: String): Boolean {
        return !moduleName.endsWith(".main") && moduleName != "main" && moduleName != "builtin" && moduleName != "stubs"
    }
}
