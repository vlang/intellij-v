package org.vlang.lang.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.util.*
import com.intellij.ui.IconManager
import com.intellij.util.PlatformIcons
import org.vlang.ide.codeInsight.VlangDeprecationsUtil
import org.vlang.ide.ui.VIcons
import org.vlang.lang.doc.psi.VlangDocComment
import org.vlang.lang.psi.*
import org.vlang.lang.psi.VlangPsiTreeUtil.getChildOfType
import org.vlang.lang.psi.VlangPsiTreeUtil.parentStubOfType
import org.vlang.lang.psi.types.VlangBaseTypeEx.Companion.toEx
import org.vlang.lang.psi.types.VlangTypeEx
import org.vlang.lang.stubs.VlangFileStub
import org.vlang.lang.stubs.VlangNamedStub
import javax.swing.Icon

abstract class VlangNamedElementImpl<T : VlangNamedStub<*>> :
    VlangStubbedElementImpl<T>,
    VlangCompositeElement,
    VlangNamedElement {

    constructor(stub: T, nodeType: IStubElementType<*, *>) : super(stub, nodeType)
    constructor(node: ASTNode) : super(node)

    override fun isBlank(): Boolean = name == "_"

    override fun isDeprecated(): Boolean = VlangDeprecationsUtil.isDeprecated(this)

    override fun isPublic(): Boolean {
        val stub = stub
        if (stub != null) {
            return stub.isPublic
        }
        return getSymbolVisibility()?.pub != null
    }

    override fun makePublic() {
        val newVisibility = VlangElementFactory.createVisibilityModifiers(project, "pub")

        var element = this as PsiElement
        val (visibility, child) = if (this is VlangConstDefinition) {
            val declaration = parent as VlangConstDeclaration
            element = declaration
            declaration.symbolVisibility to declaration.firstChild
        } else {
            val anchor = when (this) {
                is VlangFunctionOrMethodDeclaration -> this.getFn()
                is VlangStructDeclaration           -> this.structType
                is VlangInterfaceDeclaration        -> this.interfaceType
                is VlangEnumDeclaration             -> this.enumType
                is VlangTypeAliasDeclaration        -> this.type_
                else                                -> return
            }
            getSymbolVisibility() to anchor
        }

        if (visibility != null) {
            visibility.replace(newVisibility)
        } else {
            val space = VlangElementFactory.createSpace(project)
            element.addAfter(
                space,
                element.addBefore(newVisibility, child)
            )
        }
    }

    override fun makePrivate() {
        val visibility = getSymbolVisibility()
        if (visibility?.builtinGlobal != null) {
            val newVisibility = VlangElementFactory.createVisibilityModifiers(project, "__builtin")
            visibility.replace(newVisibility)
            return
        }

        visibility?.delete()
    }

    override fun isGlobal(): Boolean {
        return getSymbolVisibility()?.builtinGlobal != null
    }

    override fun getSymbolVisibility(): VlangSymbolVisibility? {
        return getChildOfType(this, VlangSymbolVisibility::class.java)
    }

    override fun getName(): String? {
        val stub = stub
        if (stub != null) {
            return stub.name
        }
        val identifier = getIdentifier()
        return identifier?.text
    }

    override fun getQualifiedName(): String? {
        return getQualifiedNameBase()
    }

    fun getQualifiedNameBase(): String? {
        val stub = stub
        if (stub != null) {
            val name = stub.name ?: "anon"
            val fileStub = stub.parentStubOfType<VlangFileStub>()
            val moduleName = fileStub?.getModuleQualifiedName()
            return VlangPsiImplUtil.getFqn(moduleName, name)
        }

        val name = name ?: return null
        val moduleName = containingFile.getModuleQualifiedName()
        if (this is VlangModuleClause) {
            return moduleName
        }

        return VlangPsiImplUtil.getFqn(moduleName, name)
    }

    override fun getOwner(): PsiElement? {
        return parentOfTypes(
            VlangInterfaceDeclaration::class,
            VlangStructDeclaration::class,
            VlangFile::class,
        )
    }

    override fun getModuleName(): String? {
        val stub = stub
        if (stub != null) {
            val fileStub = stub.parentStubOfType<VlangFileStub>()
            return fileStub?.getModuleQualifiedName()
        }

        return containingFile.getModuleQualifiedName()
    }

    override fun isCaptured(context: PsiElement): Boolean {
        val functionLit = context.parentOfType<VlangFunctionLit>()
        val captureList = functionLit?.captureList?.captureList ?: emptyList()
        return captureList.find { it.referenceExpression.text == name } != null
    }

    override fun getCapturePlace(context: PsiElement): VlangCapture? {
        val functionLit = context.parentOfType<VlangFunctionLit>()
        val captureList = functionLit?.captureList?.captureList ?: emptyList()
        return captureList.find { it.referenceExpression.text == name }
    }

    override fun setName(name: String): PsiElement? {
        val identifier = getIdentifier()
        val newIdentifier = VlangElementFactory.createIdentifier(project, name)
        identifier?.replace(newIdentifier)
        return this
    }

    override fun getType(context: ResolveState?): VlangTypeEx? {
        return CachedValuesManager.getCachedValue(this) {
            CachedValueProvider.Result
                .create(
                    getTypeInner(null),
                    PsiModificationTracker.MODIFICATION_COUNT
                )
        }
    }

    protected open fun getTypeInner(context: ResolveState?) = findSiblingType()

    open fun findSiblingType(): VlangTypeEx? {
        val stub = stub
        return if (stub != null) {
            VlangPsiTreeUtil.getStubChildOfType(parentByStub, VlangType::class.java).toEx()
        } else {
            PsiTreeUtil.getChildOfType(this, VlangType::class.java).toEx()
        }
    }

    override fun getPresentation(): ItemPresentation? {
        return object : VlangItemPresentation<VlangNamedElement>(this) {
            override fun getPresentableText() = element.name
        }
    }

    override fun getIcon(flags: Int): Icon? {
        val icon = when (this) {
            is VlangStructDeclaration         -> if (isUnion) VIcons.Union else VIcons.Struct
            is VlangInterfaceDeclaration      -> VIcons.Interface
            is VlangEnumDeclaration           -> VIcons.Enum
            is VlangMethodDeclaration         -> VIcons.Method
            is VlangFunctionDeclaration       -> VIcons.Function
            is VlangVarDefinition             -> VIcons.Variable
            is VlangConstDefinition           -> VIcons.Constant
            is VlangInterfaceMethodDefinition -> VIcons.Method
            is VlangFieldDefinition           -> VIcons.Field
            is VlangEnumFieldDefinition       -> VIcons.Field
            is VlangParamDefinition           -> VIcons.Parameter
            is VlangTypeAliasDeclaration      -> VIcons.Alias
            is VlangLabelDefinition           -> null
            else                              -> null
        } ?: return super.getIcon(flags)

        if (flags and ICON_FLAG_VISIBILITY == 0) {
            return icon
        }

        val rowIcon = IconManager.getInstance().createLayeredIcon(this, icon, flags)
        if (rowIcon.iconCount <= 1) return rowIcon

        rowIcon.setIcon(
            if (isPublic())
                PlatformIcons.PUBLIC_ICON
            else
                PlatformIcons.PRIVATE_ICON, 1
        )

        return rowIcon
    }

    override fun getTextOffset(): Int {
        val identifier = getIdentifier()
        return identifier?.textOffset ?: super.getTextOffset()
    }

    override fun getNameIdentifier(): PsiElement? {
        return getIdentifier()
    }

    override fun getDocumentation(): VlangDocComment? {
        return PsiTreeUtil.getPrevSiblingOfType(this, VlangDocComment::class.java)
    }
}
