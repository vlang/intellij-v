package org.vlang.lang.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.util.*
import com.intellij.ui.IconManager
import com.intellij.util.PlatformIcons
import org.vlang.ide.ui.VIcons
import org.vlang.lang.psi.*
import org.vlang.lang.psi.VlangPsiTreeUtil.getChildOfType
import org.vlang.lang.psi.VlangPsiTreeUtil.parentStubOfType
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

    override fun isPublic(): Boolean {
        val stub = stub
        if (stub != null) {
            return stub.isPublic
        }
        return getSymbolVisibility()?.pub != null
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
        val stub = stub
        if (stub != null) {
            val name = stub.name ?: "anon"
            val fileStub = stub.parentStubOfType<VlangFileStub>()
            val moduleName = fileStub?.getModuleQualifiedName()
            return VlangPsiImplUtil.getFqn(moduleName, name)
        }

        val name = name ?: return null
        val moduleName = containingFile.getModuleQualifiedName()
        return VlangPsiImplUtil.getFqn(moduleName, name)
    }

    override fun getOwner(): PsiElement? {
        return parentOfTypes(
            VlangInterfaceDeclaration::class,
            VlangStructDeclaration::class,
            VlangUnionDeclaration::class,
            VlangFile::class,
        )
    }

    override fun setName(name: String): PsiElement? {
        val identifier = getIdentifier()
        val newIdentifier = VlangElementFactory.createIdentifierFromText(project, name) ?: return null
        identifier?.replace(newIdentifier)
        return this
    }

    override fun getType(context: ResolveState?): VlangType? {
        val inner = getTypeInner(context)
        if (inner != null) {
            return inner
        }

        return CachedValuesManager.getCachedValue(this) {
            CachedValueProvider.Result
                .create(
                    getTypeInner(VlangPsiImplUtil.createContextOnElement(this)),
                    PsiModificationTracker.MODIFICATION_COUNT
                )
        }
    }

    protected open fun getTypeInner(context: ResolveState?) = findSiblingType()

    open fun findSiblingType(): VlangType? {
        val stub = stub
        return if (stub != null) {
            VlangPsiTreeUtil.getStubChildOfType(parentByStub, VlangType::class.java)
        } else {
            PsiTreeUtil.getNextSiblingOfType(this, VlangType::class.java)
        }
    }

    override fun getPresentation(): ItemPresentation? {
        return object : VlangItemPresentation<VlangNamedElement>(this) {
            override fun getPresentableText() = element.name
        }
    }

    override fun getIcon(flags: Int): Icon? {
        val icon = when (this) {
            is VlangStructDeclaration         -> VIcons.Struct
            is VlangInterfaceDeclaration      -> VIcons.Interface
            is VlangEnumDeclaration           -> VIcons.Enum
            is VlangUnionDeclaration          -> VIcons.Union
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
}
